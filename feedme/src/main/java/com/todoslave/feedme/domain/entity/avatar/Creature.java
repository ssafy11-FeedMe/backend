package com.todoslave.feedme.domain.entity.avatar;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "creature")
public class Creature {

    // 크리쳐 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //회원 ID
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    // 레벨
    @Column(nullable = false)
    private int level = 0 ; //레벨 1로 초기화 한다는 뜻

    // 경험치
    @Column(name = "exp", nullable = false, updatable = false)
    private int exp = 0 ;

    //크리쳐 이름
    @Column(name = "creature_name")
    private String creatureName;

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.setCreature(this);
    }
}