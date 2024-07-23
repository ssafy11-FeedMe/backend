package com.todoslave.feedme.domain.entity.communication;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "friendrequest")
public class FriendRequest {

    //친구요청 ID
    @Id @GeneratedValue
    private long id;

    //본인 회원번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //상대방 회원번호
    @Column(nullable = false)
    private int counterpart_id;

}
