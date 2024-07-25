package com.todoslave.feedme.domain.entity.communication;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "memberchatmessage")
public class MemberChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "memberChatRoom_id", nullable = false)
    private MemberChatRoom memberChatRoom;

    @Column(name = "send_id", nullable = false)
    private int sendId;

    @CreationTimestamp
    @Column(name = "transmit_at", updatable = false)
    private LocalDateTime transmitAt;

    @Column(name = "content", nullable = true)
    private String content;

}