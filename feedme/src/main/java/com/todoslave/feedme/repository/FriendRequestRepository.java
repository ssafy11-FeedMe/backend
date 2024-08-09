package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.communication.Friend;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    FriendRequest findById(int id);
    Slice<FriendRequest> findAllByMemberId(int memberId, Pageable pageable);



}
