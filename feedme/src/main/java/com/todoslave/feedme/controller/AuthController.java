package com.todoslave.feedme.controller;

import com.todoslave.feedme.config.security.StatusResponseDto;
import com.todoslave.feedme.login.Handler.JWTUtill;
import com.todoslave.feedme.login.dto.RefreshToken;
import com.todoslave.feedme.login.dto.TokenResponseStatus;
import com.todoslave.feedme.repository.RefreshTokenRepository;
import com.todoslave.feedme.login.Service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenService tokenService;
    private final JWTUtill jwtUtil;

    @PostMapping("token/logout")
    public ResponseEntity<StatusResponseDto> logout(@RequestHeader("Authorization") final String accessToken) {
        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);

        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }


//    @PostMapping("/token/refresh2")
//    public ResponseEntity<TokenResponseStatus> refresh2(@RequestHeader("Authorization") final String accessToken) {
//
//        String newAccessToken = tokenService.republishAccessToken(accessToken);
//        if (StringUtils.hasText(newAccessToken)) {
//            return ResponseEntity.ok(TokenResponseStatus.addStatus(200, newAccessToken));
//        }
//
//        return ResponseEntity.badRequest().body(TokenResponseStatus.addStatus(400, null));
//    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseStatus> refresh(@RequestHeader("Authorization") final String accessToken) {

        // 액세스 토큰으로 Refresh 토큰 객체를 조회
        Optional<RefreshToken> refreshToken = tokenRepository.findByAccessToken(accessToken);

//        System.out.println(refreshToken.get().getRefreshToken());
        // RefreshToken이 존재하고 유효하다면 실행
        if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {



            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = refreshToken.get();
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));
            // 액세스 토큰의 값을 수정해준다.
            resultToken.updateAccessToken(newAccessToken);
            tokenRepository.save(resultToken);

            // 새로운 액세스 토큰을 반환해준다.
            return ResponseEntity.ok(TokenResponseStatus.addStatus(200, newAccessToken));
        }

        return ResponseEntity.badRequest().body(TokenResponseStatus.addStatus(400, null));
    }



}