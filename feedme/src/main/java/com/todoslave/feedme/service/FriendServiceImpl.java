package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.FriendReqRequestDTO;
import com.todoslave.feedme.DTO.FriendReqResponseDTO;
import com.todoslave.feedme.DTO.FriendResponseDTO;
import com.todoslave.feedme.domain.entity.communication.Friend;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.repository.FriendRepository;
import com.todoslave.feedme.repository.FriendRequestRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

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
    public List<FriendReqResponseDTO> getRequestFriend() {

        int memberId = SecurityUtil.getCurrentUserId();

        List<FriendRequest> friendRequests = friendRequestRepository.findAllByMemberId(memberId);
        List<FriendReqResponseDTO> friendReqResponseDTOList = new ArrayList<>();

        for(FriendRequest friendRequest : friendRequests){

            FriendReqResponseDTO friendReqResponseDTO = new FriendReqResponseDTO();
            friendReqResponseDTO.setId(friendRequest.getId());
            friendReqResponseDTO.setCounterpartNickname(friendRequest.getCounterpartId().getNickname());
            friendReqResponseDTOList.add(friendReqResponseDTO);

        }

        return friendReqResponseDTOList;
    }

    // 친구 수락
    @Override
    public void insertFriendship(int requestId) {

        FriendRequest friendRequest = friendRequestRepository.findById(requestId);

        Friend friend = new Friend();

        Member member = SecurityUtil.getCurrentMember();
        Member counterpart = friendRequest.getCounterpartId();

        friend.setMember(member);
        friend.setCounterpart(counterpart);

        friendRequestRepository.deleteById(requestId);

        List<Integer> members = new ArrayList<>();

        members.add(member.getId());
        members.add(counterpart.getId());

        memberChatService.insertChatRoom(members);
        friendRepository.save(friend);

        memberChatService.insertChatRoom(members);

    }

    // 친구 거절
    @Override
    public void deleteRequestFriend(int requestId) {

        friendRequestRepository.deleteById(requestId);

    }
}
