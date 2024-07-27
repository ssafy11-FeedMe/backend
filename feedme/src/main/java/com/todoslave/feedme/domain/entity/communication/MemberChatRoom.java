package com.todoslave.feedme.domain.entity.communication;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "memberchatroom")
public class MemberChatRoom {

    @Id
    private String id;
    private List<String> participantIds;

<<<<<<< HEAD
    @CreatedDate
=======
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "counterpart_id", nullable = true)
    private int counterpartId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
>>>>>>> df102da768be1bb2c749385d38cdad44cb0bfbc0
    private LocalDate createdAt;

}
