package com.todoslave.feedme.domain.entity.communication;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "memberchatroom")
public class MemberChatRoom {

    // 채팅방 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 회원 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 상대방 ID
    @Column(name = "counterpart_id", nullable = true)
    private int counterpartId;

    //최근 대화 일자
    @Column(name = "updated_at", nullable = true)
    private LocalDate updatedAt;

    //유저간 채팅 메시지 매핑
    @OneToMany(mappedBy = "memberChatRoom")
    private List<MemberChatMessage> messages = new ArrayList<>();

}
