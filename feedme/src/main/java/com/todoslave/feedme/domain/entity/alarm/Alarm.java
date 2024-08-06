package com.todoslave.feedme.domain.entity.check;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity @Data
public class Alarm {

    // 알람 ID
    @Id
    @GeneratedValue
    private int id;

    // 회원 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    // 내용
    @Column(nullable = false)
    private String content;

    // 생성 시간
    @CreationTimestamp
    @Column(name = "receive_at", nullable = false, updatable = false)
    private LocalDateTime receiveAt;

    // 확인 했는 지 여부
    @Column(name = "is_checked")
    private int isChecked;

//    //==연관관계 메서드==//
//    public void setMember(Member member) {
//        this.member = member;
//        member.getAlarms().add(this);
//    }

}