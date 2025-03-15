package org.zerock.mallapi.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
public class Member {
    @Id
    private String email;

    private String pw;
    private String nickname;
    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    //method
    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

    public void clearRole() {
        memberRoleList.clear();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePw(String pw) {
        this.pw = pw;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }
}
/*
Hibernate:
create table member (
        email varchar(255) not null,
nickname varchar(255),
pw varchar(255),
social bit not null,
primary key (email)
    ) engine=InnoDB
Hibernate:
create table member_member_role_list (
        member_email varchar(255) not null,
member_role_list tinyint check (member_role_list between 0 and 2)
    ) engine=InnoDB
Hibernate:
alter table if exists member_member_role_list
add constraint FK2cojwm6nbbasi0xkedqjjagap
foreign key (member_email)
references member (email)*/
