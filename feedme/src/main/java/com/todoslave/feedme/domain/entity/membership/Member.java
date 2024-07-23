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

import java.sql.Timestamp;
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
    private Long id;

    //비밀번호
    @Column(nullable = false)
    private String password;

    //이메일
    @Column(nullable = false)
    private String email;

    //생일
    @Column(nullable = false)
    private Timestamp birthday;

    //여기부터 1대1
    
    //회원 상세와 매핑
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberDetail memberDetail;

    //회원 갱신 정보와 매핑
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberRenewInfo memberRenewInfo;

    //회원 위치와 매핑
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberSpace memberSpace;

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
