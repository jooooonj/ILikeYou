package com.ll.gramgram.domain.member.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.domain.instaMember.entity.InstaMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Builder // Member.builder().providerTypeCode(providerTypeCode) .. 이런식으로 쓸 수 있게 해주는
@NoArgsConstructor // @Builder 붙이면 이거 필수
@AllArgsConstructor // @Builder 붙이면 이거 필수
@ToString(callSuper = true)
@Entity // 아래 클래스는 member 테이블과 대응되고, 아래 클래스의 객체는 테이블의 row와 대응된다.
@Getter // 아래 필드에 대해서 전부다 게터를 만든다. private Long id; => public Long getId() { ... }
public class Member extends BaseEntity {
    private String providerTypeCode; // 일반회원인지, 카카오로 가입한 회원인지, 구글로 가입한 회원인지
    @Column(unique = true)
    private String username;
    private String password;
    @OneToOne // 1:1
    @Setter // memberService::updateInstaMember 함수 때문에
    private InstaMember instaMember;

    // 이 함수 자체는 만들어야 한다. 스프링 시큐리티 규격
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 멤버는 member 권한을 가진다.
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        // username이 admin인 회원은 추가로 admin 권한도 가진다.
        if (isAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }


    public void connectInstaMember(InstaMember instaMember){
        this.instaMember = instaMember;
    }
    public void disconnectInstaMember(){
        instaMember = null;
    }

    // 이 회원이 본인의 인스타ID를 등록했는지 안했는지
    public boolean hasConnectedInstaMember() {
        return instaMember != null;
    }


    public boolean isAdmin() {
        return "admin".equals(username);
    }
}
