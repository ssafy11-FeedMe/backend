package com.todoslave.feedme.service;

import com.todoslave.feedme.domain.entity.communication.Friend;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.repository.FriendRepository;
import com.todoslave.feedme.repository.FriendRequestRepository;
import com.todoslave.feedme.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FriendServiceImpl implements FriendService {

    MemberRepository memberRepository;
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Override
    public void insertFriend(int memberId, String email) {

//        int counterId;

//        FriendRequest friendRequest = new FriendRequest();
//        friendRequest.setMember(memberId);
//        friendRequest.setCounterpart_id(counterId);
//
//        friendRequestRepository.save(friendRequest);

    }

    @Override
    @Transactional
    public void deleteFriend(int friendId) {

        friendRequestRepository.deleteById(friendId);

    }

    @Override
    public List<Friend> getFriends(Integer memberId) {
        return friendRepository.findAllByMemberId(memberId);
    }

    @Override
    public List<FriendRequest> getRequestFriend(Integer memberId) {
        return friendRequestRepository.findAllByMemberId(memberId);
    }

    @Override
    public void insertFriendship(int id) {
//
//        FriendRequest friendRequest = friendRequestRepository.findById(id);
//
//        Friend friend = new Friend();
//        Member member = friendRequest.getMember();
//        friend.setMember(member);
//        friend.setCounterpart_id(friendRequest.getCounterpart_id());
//
//        friendRepository.save(friend);

    }

    @Override
    public void deleteRequestFriend(int requestId) {

        friendRequestRepository.deleteById(requestId);

    }


    public boolean isFriend(int memberId, int friendId) {
        return friendRepository.existsByMemberIdAndCounterpartId(memberId, friendId) ||
                friendRepository.existsByCounterpartIdAndMemberId(friendId, memberId);
    }

}
