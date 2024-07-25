package com.todoslave.feedme.domain.entity.check;


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
    private Member member;

    //컨텐츠
    @Column(nullable = false)
    private String content;

    // 생성 시간
    @CreationTimestamp
    @Column(name = "receive_at", nullable = false, updatable = false)
    private LocalDateTime receiveAt;

}
