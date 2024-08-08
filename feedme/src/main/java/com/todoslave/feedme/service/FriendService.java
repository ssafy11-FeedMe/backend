package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.FriendReqRequestDTO;
import com.todoslave.feedme.DTO.FriendReqResponseDTO;
import com.todoslave.feedme.DTO.FriendResponseDTO;
import com.todoslave.feedme.DTO.MemberChatListResponseDTO;
import com.todoslave.feedme.DTO.PaginationRequestDTO;
import com.todoslave.feedme.domain.entity.communication.Friend;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;

import com.todoslave.feedme.domain.entity.membership.Member;
import java.util.List;
import org.springframework.data.domain.Slice;

public interface FriendService {

    // 친구 추가
    void requestFriend(FriendReqRequestDTO friendReqRequestDTO);

    // 친구 삭제
    void deleteFriend(int friendId);

    // 친구 목록 조회
    List<FriendResponseDTO> getFriends();

    // 친구 요청 목록 조회
    Slice<FriendReqResponseDTO> getRequestFriend(PaginationRequestDTO paginationRequestDTO);

    // 친구 수락
    MemberChatListResponseDTO insertFriendship(int requestId);

    // 친구 거절
    void deleteRequestFriend(int requestId);


}
