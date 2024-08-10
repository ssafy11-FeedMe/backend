package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.FriendReqRequestDTO;
import com.todoslave.feedme.DTO.FriendReqResponseDTO;
import com.todoslave.feedme.DTO.FriendResponseDTO;
import com.todoslave.feedme.DTO.MemberChatListResponseDTO;
import com.todoslave.feedme.DTO.PaginationRequestDTO;
import com.todoslave.feedme.domain.entity.communication.Friend;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.mapper.FriendRequestMapper;
import com.todoslave.feedme.repository.FriendRepository;
import com.todoslave.feedme.repository.FriendRequestRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendServiceImpl implements FriendService{

    @Autowired
    MemberService memberService;
    FriendRepository friendRepository;
    FriendRequestRepository friendRequestRepository;
    MemberChatService memberChatService;


    // 친구 요청
    @Override
    public void requestFriend(FriendReqRequestDTO friendReqRequestDTO) {

        Member member = SecurityUtil.getCurrentMember();
        Member counterpart = memberService.findByNickname(friendReqRequestDTO.getCounterpartNickname());

        FriendRequest friendRequest = new FriendRequest();

        friendRequest.setMember(member);
        friendRequest.setCounterpartId(counterpart);

        friendRequestRepository.save(friendRequest);

    }

    // 친구 삭제
    @Override
    @Transactional
    public void deleteFriend(int friendId) {

        friendRequestRepository.deleteById(friendId);

    }

    // 친구 목록 불러오기
    @Override
    public List<FriendResponseDTO> getFriends() {

        int memberId = SecurityUtil.getCurrentUserId();

        List<Friend> f = friendRepository.findAllByMemberId(memberId);
        List<FriendResponseDTO> friends = new ArrayList<>();

        for(Friend friend : f){

            Member counterpart = friend.getCounterpart();
            FriendResponseDTO friendResponseDTO = new FriendResponseDTO();
            friendResponseDTO.setFriendId(friend.getId());
            friendResponseDTO.setCounterpartNickname(counterpart.getNickname());
            friends.add(friendResponseDTO);

        }

        return friends;
    }

    // 친구 요청 불러오기
    @Override
    public Slice<FriendReqResponseDTO> getRequestFriend(PaginationRequestDTO paginationRequestDTO) {

        int memberId = SecurityUtil.getCurrentUserId();

        Pageable pageable = PageRequest.of(paginationRequestDTO.getSkip() / paginationRequestDTO.getLimit(),
            paginationRequestDTO.getLimit());

        Slice<FriendRequest> friendRequests = friendRequestRepository.findAllByMemberId(memberId, pageable);

        return friendRequests.map(FriendRequestMapper::toDto);
    }

    // 친구 수락
    @Override
    public MemberChatListResponseDTO insertFriendship(int requestId) {

        FriendRequest friendRequest = friendRequestRepository.findById(requestId);

        Friend friend = new Friend();

        Member member = SecurityUtil.getCurrentMember();
        Member counterpart = friendRequest.getCounterpartId();

        friend.setMember(member);
        friend.setCounterpart(counterpart);

        // 친구 추가
        friendRepository.save(friend);

        friend.setMember(counterpart);
        friend.setCounterpart(member);

        friendRepository.save(friend);

        friendRequestRepository.deleteById(requestId);

        List<Integer> members = new ArrayList<>();

        members.add(member.getId());
        members.add(counterpart.getId());

        // 채팅방 생성
        return memberChatService.insertChatRoom(members);
    }

    // 친구 거절
    @Override
    public void deleteRequestFriend(int requestId) {

        friendRequestRepository.deleteById(requestId);

    }

    // 친구 인지 확인하기
    public boolean isFriend(int memberId, int friendId) {
        return friendRepository.existsByMemberIdAndCounterpartId(memberId, friendId) ||
            friendRepository.existsByCounterpartIdAndMemberId(friendId, memberId);
    }
}
