package com.todoslave.feedme.domain.entity.avatar;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "creature")
public class Creature {

    // 크리쳐 ID
    @Id @GeneratedValue
    private int id;

    //회원 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //진화단계
    @Column(nullable = false)
    private int level = 1 ; //레벨 1로 초기화 한다는 뜻

}