package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.CreateAccessTokenRequest;
import com.todoslave.feedme.DTO.CreateAccessTokenResponse;
import com.todoslave.feedme.service.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final MemberService memberService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = memberService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
