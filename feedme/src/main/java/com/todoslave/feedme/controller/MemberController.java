package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.CreateAccessTokenRequest;
import com.todoslave.feedme.DTO.CreateAccessTokenResponse;
import com.todoslave.feedme.DTO.LoginRequest;
import com.todoslave.feedme.DTO.LoginResponse;
import com.todoslave.feedme.config.jwt.JwtProperties;
import com.todoslave.feedme.config.jwt.TokenProvider;
import com.todoslave.feedme.domain.entity.membership.Emotion;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.domain.entity.task.Todo;
import com.todoslave.feedme.logintmp.dto.AddMemberRequest;
import com.todoslave.feedme.repository.MemberRepository;
import com.todoslave.feedme.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    JwtProperties jwtProperties;

//    //테스트 만들기
//    @GetMapping("/sign")
//    public ResponseEntity<String> sign() {
//        try {
//            Member member = new Member();
//            member.setEmail("asdf@gmail.com");
//            member.setNickname("테스트1");
//            member.setBirthday(Timestamp.valueOf(LocalDateTime.now()));
//            member.setToken("ABC");
//            memberService.insertMember(member);
//            return ResponseEntity.ok("멤버 성공적 전달!");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error saving member: " + e.getMessage());
//        }
//    }

    @GetMapping("/sign")
    public ResponseEntity<String> sign() {
        try {
            // 유저 생성
            Member member = new Member();
            member.setEmail("asdf@gmail.com");
            member.setNickname("테스트1");
            member.setBirthday(Timestamp.valueOf(LocalDateTime.now()));
            memberService.insertMember(member);

            // 토큰 생성
            String token = tokenProvider.generateToken(member, Duration.ofDays(14));

            // 토큰 유효성 검증 및 유저 ID 반환
            boolean isValid = tokenProvider.validToken(token);
            int userId = tokenProvider.getUserId(token);

            // 유저 DB에 토큰 저장
            member.setToken(token);
            memberRepository.save(member);

            // 토큰으로 유저 정보 조회
            Member memberFromToken = memberRepository.findById((int) userId).orElse(null);

            return ResponseEntity.ok("토큰: " + token + ", 토큰 유효성: " + isValid + ", 유저 ID: " + userId + ", 토큰으로 찾은 유저의 토큰값: " + (memberFromToken != null ? memberFromToken.getToken() : "없음"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving member: " + e.getMessage());
        }
    }


    @GetMapping("/sign2")
    public ResponseEntity<String> sign2() {
        try {
            // 유저 생성
            Member member = new Member();
            member.setEmail("asd353f@gmail.com");
            member.setNickname("테23스트1");
            member.setBirthday(Timestamp.valueOf(LocalDateTime.now()));
            memberService.insertMember(member);

            // 토큰 생성
            String token = tokenProvider.generateToken(member, Duration.ofDays(14));

            // 토큰 유효성 검증 및 유저 ID 반환
            boolean isValid = tokenProvider.validToken(token);
            int userId = tokenProvider.getUserId(token);

            // 유저 DB에 토큰 저장
            member.setToken(token);
            memberRepository.save(member);

            // 토큰으로 유저 정보 조회
            Member memberFromToken = memberRepository.findByToken(token).orElse(null);

            return ResponseEntity.ok("토큰: " + token + ", 토큰 유효성: " + isValid + ", 유저 ID: " + userId + ", 토큰으로 찾은 유저의 ID : " + memberFromToken.getId() );
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving member: " + e.getMessage());
        }
    }


    @Tag(name = "정상적인 멤버 가입")
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member){
        //
        // 원래라면 로그인 가능하면 로그인 아니면 회원가입 근데 일단 회원가입 먼저
        //
        return ResponseEntity.ok(memberService.insertMember(member));
    }
    
//    @Tag(name = "AddMemberRequest로 맴버 입력하기", description = "유저 테스트")
    @PostMapping("/stu")
    public ResponseEntity<Long> addMember(@RequestBody AddMemberRequest request) {
        int memberId = memberService.save(request);
        return ResponseEntity.ok((long) memberId);
    }

//    @Tag(name = "로그아웃 하기", description = "유저 테스트")
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    //리프래시 이용하여 엑세스 토큰 만들기
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = memberService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        boolean isAuthenticated = memberService.authenticate(request.getEmail());

        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = memberService.createRefreshToken(request.getEmail());

        String accessToken = memberService.createNewAccessToken(refreshToken);

        //굳이?
//        memberService.storeRefreshToken(request.getUsername(), refreshToken);

        LoginResponse response = new LoginResponse(accessToken);


        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .body(response); //여기 어카노!!
    }


//
//    @PostMapping("/user/regist")
//    public ResponseEntity<?> registUser(@RequestBody User user) {
//        User check = userService.checkUser(user);
//        if (check == null) {
//            userService.insertUser(user);
//            userService.updateImg(user.getId());
//            return new ResponseEntity<String>("회원가입 성공", HttpStatus.OK);
//        }
//        String msg = "이미 있는 아이디입니다.";
//        return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
//    }


}
