package com.todoslave.feedme.domain.entity.board;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class FeedRecomment {
    // 피드 대댓글 ID
    @Id
    @GeneratedValue
    private int id;

    //피드 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private FeedComment feedComment;

    // 회원 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 대댓글 내용
    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 생성 시간
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
