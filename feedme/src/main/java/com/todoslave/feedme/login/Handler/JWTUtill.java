package com.todoslave.feedme.login.Handler;

import com.todoslave.feedme.config.jwt.JwtProperties;
import com.todoslave.feedme.login.Service.RefreshTokenService;
import com.todoslave.feedme.login.Service.TokenBlacklistService;
import com.todoslave.feedme.login.dto.GeneratedToken;
import com.todoslave.feedme.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

//토큰 발행 및 토큰 검증에 쓰이는 유틸 입니다.

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTUtill {
    private final JwtProperties jwtProperties;
    private final RefreshTokenService tokenService;
    private final MemberService memberService;
    private final TokenBlacklistService tokenBlacklistService;
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public GeneratedToken generateToken(String email, String role) {
        // refreshToken과 accessToken을 생성한다.
        String refreshToken = generateRefreshToken(email, role);
        String accessToken = generateAccessToken(email, role);

        // 토큰을 Redis에 저장한다.
        tokenService.saveTokenInfo(email, refreshToken, accessToken);

        //토큰을 MY SQL에 저장한다.
//        memberService.settoken(email, refreshToken);

        return new GeneratedToken(accessToken, refreshToken);
    }



    public String generateRefreshToken(String email, String role) {
        // 토큰의 유효 기간을 밀리초 단위로 설정.
        long refreshPeriod = 1000L * 60L * 60L * 24L * 14; // 2주

        // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한)을 셋팅
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        // 현재 시간과 날짜를 가져온다.
        Date now = new Date();

        return Jwts.builder()
                // Payload를 구성하는 속성들을 정의한다.
                .setClaims(claims)
                // 발행일자를 넣는다.
                .setIssuedAt(now)
                // 토큰의 만료일시를 설정한다.
                .setExpiration(new Date(now.getTime() + refreshPeriod))
                // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String generateAccessToken(String email, String role) {
        long tokenPeriod = 1000L * 60L * 30L * 4; // 2시간
//        long tokenPeriod = 1000L * 30L;
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();
        return
                Jwts.builder()
                        // Payload를 구성하는 속성들을 정의한다.
                        .setClaims(claims)
                        // 발행일자를 넣는다.
                        .setIssuedAt(now)
                        // 토큰의 만료일시를 설정한다.
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();

    }


//    public boolean verifyToken(String token) { //토큰 검증 하기 (시간 체크)
//        try {
//            Jws<Claims> claims = Jwts.parser()
//                    .setSigningKey(secretKey) // 비밀키를 설정하여 파싱한다.
//                    .parseClaimsJws(token);  // 주어진 토큰을 파싱하여 Claims 객체를 얻는다.
//            // 토큰의 만료 시간과 현재 시간비교
//            return claims.getBody()
//                    .getExpiration()
//                    .after(new Date());  // 만료 시간이 현재 시간 이후인지 확인하여 유효성 검사 결과를 반환
//        } catch (Exception e) {
//            return false;
//        }
//    }
public boolean verifyToken(String token) {
    try {
        if (tokenBlacklistService.isBlacklisted(token)) {
            return false;
        }
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
        return claims.getBody()
                .getExpiration()
                .after(new Date());
    } catch (Exception e) {
        return false;
    }
}
    //토큰을 블랙리스트에 추가
    public void invalidateToken(String token) {
        // 토큰의 만료 날짜를 얻음
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Date expiryDate = claims.getExpiration();
        // 블랙리스트 서비스에 토큰을 추가
        tokenBlacklistService.addToBlacklist(token, expiryDate);
    }

    // 토큰에서 Email을 추출한다.
    public String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 ROLE(권한)만 추출한다.
    public String getRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
    }

}