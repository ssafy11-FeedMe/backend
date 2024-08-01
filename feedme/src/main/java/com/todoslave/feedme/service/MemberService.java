package com.todoslave.feedme.service;

import com.todoslave.feedme.config.jwt.TokenProvider;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.logintmp.dto.AddMemberRequest;
import com.todoslave.feedme.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
//@Transactional(readOnly = true) //조회에선
@RequiredArgsConstructor // 생성자 만들어 주는 얘
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;


    //그냥 가입 시켜주는 얘
    public Member insertMember (Member member) {
        return memberRepository.save(member);
    }

    //DTO로 멤버 저장
    public int save(AddMemberRequest dto) {

        Member member = new Member();
        member.setNickname(dto.getNickname());
        member.setEmail(dto.getEmail());
        member.setBirthday(dto.getBirthday());
        member.setLatitude(dto.getLatitude());
        member.setLongitude(dto.getLongitude());

        return memberRepository.save(member).getId();
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


    //여기부터 다른쪽인데 일단 해볼게염

    //아이디로 맴버 찾기
    public Member findById(int userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found by id: " + userId));
    }

    //토큰으로 맴버 찾기
    public Member findByToken(String token) {
        return memberRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Member not found by token" + token));
    }

    //엑세스 토큰 만들기
    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

//      int userId = findByToken(refreshToken).getId();
//      Member member = findById(userId);  문제 있으면 이걸로
        Member member = findByToken(refreshToken);

        return tokenProvider.generateToken(member, Duration.ofHours(1));
    }

    public boolean authenticate(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public String createRefreshToken(String username) {
       Member member = memberRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Member not found by email: " + username));
       String token = tokenProvider.generateToken(member, Duration.ofDays(14));
       member.setToken(token);
       memberRepository.save(member);  // 변경 사항을 데이터베이스에 저장
       return token;
    }
}
