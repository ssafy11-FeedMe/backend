package com.todoslave.feedme.domain.entity.communication;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
@Table(name = "memberchatmessage")
public class MemberChatMessage {

    // 채팅 메세지 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 회윈 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 채팅방 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberChatRoom_id")
    private MemberChatRoom memberChatRoom;

    // 보낸 시간
    @Column(name = "transmit_at", nullable = true)
    private LocalDateTime transmitAt;

    // 내용
    @Column(name = "content", nullable = true)
    private String content;

}