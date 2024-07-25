package com.todoslave.feedme.domain.entity.task;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

// 할 일
@Entity @Getter
@Table(name = "todo")
public class Todo {

    // 할일 ID
    @Id @GeneratedValue
    private int id;

    // 회원 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 할일 카테고리 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todoCategory_id")
    private TodoCategory todoCategory;

    // 내용
    @Column(nullable = false, length = 1000)
    private String text;

    // 생성 일자
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    //완료 여부
    @Column(name = "is_completed",nullable = false)
    private boolean isCompleted;


}
