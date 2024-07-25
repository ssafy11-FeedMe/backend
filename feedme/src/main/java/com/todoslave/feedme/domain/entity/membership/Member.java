package com.todoslave.feedme.domain.entity.membership;

import com.todoslave.feedme.domain.entity.avatar.Creature;
import com.todoslave.feedme.domain.entity.communication.Friend;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import com.todoslave.feedme.domain.entity.diary.PictureDiary;
import com.todoslave.feedme.domain.entity.task.CreatureTodo;
import com.todoslave.feedme.domain.entity.task.Todo;
import com.todoslave.feedme.domain.entity.task.TodoCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {

    //회원 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //이메일
    @Column(nullable = false)
    private String email;

    //닉네임
    @Column(nullable = false)
    private String nickname;

    //생일
    private Timestamp birthday;

    // 경험치
    @Column(name = "exp", nullable = false, updatable = false)
    private int exp;

    // 상태
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Emotion status; // BASIC, JOY, SAD

    //위도
    @Column(name = "latitude")
    private Double latitude;

    //경도
    @Column(name = "longitude")
    private Double longitude;

    //가입일
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime joinDate;

    //토큰
    @Column(name = "token", nullable = false)
    private String token;

    //알람 수신 여부
    @Column(name = "alarm_enabled", nullable = false)
    private boolean alarm_enabled;

    //알람 시간
    @Column(name = "alarm_time")
    private LocalDateTime alarm_time;

    //여기부터 1대1
    
//    //회원 상세와 매핑
//    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private MemberDetail memberDetail;
//
//    //회원 갱신 정보와 매핑
//    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private MemberRenewInfo memberRenewInfo;
//
//    //회원 위치와 매핑
//    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private MemberSpace memberSpace;

    //여기부터 1대 N

    //친구와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Friend> friends = new ArrayList<>();

    //친구요청과 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FriendRequest> friendRequests = new ArrayList<>();

    // 채팅방과 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    // 유저간 채팅 메세지
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberChatMessage> memberChatMessages = new ArrayList<>();

    // 투두와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    // 투두 카테고리와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoCategory> todoCategories = new ArrayList<>();

    // 크리쳐 숙제와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreatureTodo> creatureTodos = new ArrayList<>();

    //크리쳐와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Creature> creatures = new ArrayList<>();

    // 그림일기와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PictureDiary> pictureDiary = new ArrayList<>();


}
