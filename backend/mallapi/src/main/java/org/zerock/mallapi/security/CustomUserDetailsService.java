package org.zerock.mallapi.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.repository.MemberRepository;

import java.util.stream.Collectors;

//UserDetailsService 커스텀을 위한 클래스
@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("-------CustomUserDetailsService.loadUserByUsername--------");
        //입력된 email(username)으로 DB의 사용자 정보 가져오기
        Member member = memberRepository.getWithRoles(username);
        if(member == null){
            throw new UsernameNotFoundException("Not Found");
        }

        //MemberDTO 형태로 가져온 정보 변경
        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );
        log.info("dto 변환 사용자 정보:"+memberDTO);
        return memberDTO;
    }
}
