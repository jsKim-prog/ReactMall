package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.dto.MemberModifyDTO;
import org.zerock.mallapi.repository.MemberRepository;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; //패스워드 임의생성시를 위한 암호화

    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        String email = getEmailFromKakaoAccessToken(accessToken);
        log.info("getKakaoMember--email:"+email);
        Optional<Member> result = memberRepository.findById(email);
        //기존회원(이메일이 있는 경우)
        if(result.isPresent()){
            MemberDTO memberDTO = entityToDTO(result.get());
            return memberDTO;
        }

        // 신규회원(이메일이 없는 경우)
        Member socialMember = makeSocialMember(email);
        memberRepository.save(socialMember);

        MemberDTO memberDTO = entityToDTO(socialMember);
        return memberDTO;
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());
        Member member = result.orElseThrow();
        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeSocial(false);
        member.changeNickname(memberModifyDTO.getNickname());

        memberRepository.save(member);
    }

    //Kakao accessToken-> 계정검색-> email 반환
    private String getEmailFromKakaoAccessToken(String accessToken){
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me"; //사용자정보를 요청할 url

        if(accessToken==null){
            throw new RuntimeException("Access Token is Null");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        //	Authorization: Bearer ${ACCESS_TOKEN}
        headers.add("Authorization", "Bearer "+accessToken);
        //Content-Type: application/x-www-form-urlencoded;charset=utf-8
        headers.add("Content-Type","application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("KakaoService...getEmail:"+response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("KakaoService...getEmail--------------");
        log.info(bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

        log.info("kakaoAccount : "+kakaoAccount);
        return kakaoAccount.get("email");

    }

    //임시 패스워드 생성:신규 회원 추가시
    private String makeTempPassword(){
        StringBuffer buffer = new StringBuffer();

        for (int i=0; i<10;i++){
            buffer.append((char) ((int)(Math.random()*55)+65));
        }
        return buffer.toString();
    }

    //신규회원 추가시 새로운 Member(domain) 객체 생성
    private Member makeSocialMember(String email){
        String tempPassword = makeTempPassword();
        log.info("임시비밀번호 for Kakao register:"+tempPassword);
        String nickname = "소셜회원";
        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();
        member.addRole(MemberRole.USER);
        return member;
    }
}
