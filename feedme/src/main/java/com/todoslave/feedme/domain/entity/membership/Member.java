package com.todoslave.feedme.domain.entity.membership;

import com.todoslave.feedme.domain.entity.avatar.Creature;
import com.todoslave.feedme.domain.entity.board.Feed;
import com.todoslave.feedme.domain.entity.board.FeedComment;
import com.todoslave.feedme.domain.entity.board.FeedRecomment;
import com.todoslave.feedme.domain.entity.board.FeedLike;
import com.todoslave.feedme.domain.entity.check.Alarm;
import com.todoslave.feedme.domain.entity.communication.Friend;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;
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
    @GeneratedValue
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

    //여기부터 1대 N

    // 친구와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Friend> friends = new ArrayList<>();


    // 친구요청과 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FriendRequest> friendRequests = new ArrayList<>();

//    // 채팅방과 매핑
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();
//
//    // 유저간 채팅 메세지
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<MemberChatMessage> memberChatMessages = new ArrayList<>();

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

    //좋아요 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private  List<FeedLike> feedLikes = new ArrayList<>();

    // 피드와 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feeds = new ArrayList<>();

    //피드 댓글과 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedComment> feedComments = new ArrayList<>();

    //피드 대댓글과 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedRecomment> feedRecomments = new ArrayList<>();

    //알람과 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();


}
