package com.todoslave.feedme.domain.entity.communication;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "memberchatmessage")
public class MemberChatMessage {

    @Id
<<<<<<< HEAD
    private String id;
    private String memberChatRoomId;
    private String sendId;
=======
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "memberChatRoom_id", nullable = false)
    private MemberChatRoom memberChatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreationTimestamp
    @Column(name = "transmit_at", updatable = false)
    private LocalDateTime transmitAt;

    @Column(name = "content", nullable = true)
>>>>>>> df102da768be1bb2c749385d38cdad44cb0bfbc0
    private String content;

    @CreatedDate
    private LocalDateTime transmitAt;

}