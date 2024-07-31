package com.todoslave.feedme.service;

import com.todoslave.feedme.domain.entity.membership.Member;

public interface AlarmService {

    void todoCompleted();
    boolean requestFriendship(Member requestor, Member counterpart);
    void congratsBirthday();

}
