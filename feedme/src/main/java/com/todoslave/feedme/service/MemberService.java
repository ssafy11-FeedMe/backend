package com.todoslave.feedme.service;

import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // 생성자 만들어 주는 얘
public class MemberService {

    //    @Autowired
    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional //가입시에
    public void Join(Member member) {
//        sameNameCheck(member); // 중복 회원 검증
        System.out.println("========================");
        System.out.println(member.toString());
        System.out.println("========================");
        member = memberRepository.save(member);
        System.out.println(member.toString());
//        return member.getId();
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 단수 조회
//    public Member findOne(int id) {
//        return memberRepository.findById(id);
//    }

    // 중복 체크
//    private void sameNameCheck(Member member) {
//        List<Member> findMembers = memberRepository.findByName(member.getNickname());
//        if(!findMembers.isEmpty()) {
//            throw new IllegalStateException("중복 닉네임");
//        }
//    }


}
