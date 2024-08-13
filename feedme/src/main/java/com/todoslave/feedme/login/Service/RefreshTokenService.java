package com.todoslave.feedme.login.Service;

import com.todoslave.feedme.login.Handler.JWTUtill;
import com.todoslave.feedme.login.dto.RefreshToken;
import com.todoslave.feedme.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;


    @Transactional
    public void saveTokenInfo(String email, String refreshToken, String accessToken) {
        repository.save(new RefreshToken(email, accessToken, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        RefreshToken token = repository.findByAccessToken(accessToken)
                .orElseThrow(IllegalArgumentException::new);

        repository.delete(token);
    }


//    @Transactional
//    public String republishAccessToken(String accessToken) {
//        // 액세스 토큰으로 Refresh 토큰 객체를 조회
//        Optional<RefreshToken> refreshToken = repository.findByAccessToken(accessToken);
//
//        // RefreshToken이 존재하고 유효하다면 실행
//        if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {
//            // RefreshToken 객체를 꺼내온다.
//            RefreshToken resultToken = refreshToken.get();
//            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
//            String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));
//            // 액세스 토큰의 값을 수정해준다.
//            resultToken.updateAccessToken(newAccessToken);
//            repository.save(resultToken);
//            // 새로운 액세스 토큰을 반환해준다.
//            return newAccessToken;
//        }
//
//        return null;
//    }
}