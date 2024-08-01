package com.todoslave.feedme.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
