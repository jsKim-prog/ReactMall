package org.zerock.mallapi.service;

import jakarta.transaction.Transactional;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.dto.MemberModifyDTO;

import java.util.stream.Collectors;

@Transactional
public interface MemberService {
    //로그인 : AccessToken -> MemberDTO
    MemberDTO getKakaoMember(String accessToken);

    //회원정보 변경(시큐리티와 관련 없는 DTO 사용)
    void modifyMember(MemberModifyDTO memberModifyDTO);

    //entity -> DTO : 회원정보는 MemberDTO로 처리됨
    default MemberDTO entityToDTO(Member member){
        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );
        return dto;
    }
}
