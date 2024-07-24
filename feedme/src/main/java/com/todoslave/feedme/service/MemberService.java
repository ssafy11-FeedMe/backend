package com.todoslave.feedme.service;

import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.repository.MemberRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //조회에선
@RequiredArgsConstructor // 생성자 만들어 주는 얘
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입

    @Transactional //가입시에
    public int Join(Member member) {
        sameNameCheck(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 단수 조회

    public Member findOne(int id) {
        return memberRepository.findById(id);
    }

    // 중복 체크
    private void sameNameCheck(Member member) {
        List<Member> findMembers = memberRepository.findByNickname(member.getMemberRenewInfo().getNickname());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("중복 닉네임");
        }
    }


}
