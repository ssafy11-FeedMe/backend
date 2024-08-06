package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.MemberSearchResponseDTO;
import com.todoslave.feedme.DTO.MemberSignupRequestDTO;
import com.todoslave.feedme.domain.entity.avatar.Creature;
import com.todoslave.feedme.domain.entity.membership.Emotion;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
//@Transactional(readOnly = true) //조회에선
@RequiredArgsConstructor // 생성자 만들어 주는 얘
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FriendService friendService;


    //그냥 가입 시켜주는 얘
    public Member insertMember (Member member) {
        return memberRepository.save(member);
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //아이디로 맴버 찾기
    public Member findById(int userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found by id: " + userId));
    }

    //이메일로 찾기
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    public boolean authenticate(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    //회원가입
    public Member registerMember(MemberSignupRequestDTO memberSignupRequestDTO) {
//        memberRepository.findByEmail(memberSignup.getEmail()).orElseThrow(() -> new RuntimeException("Member not found by email: " + memberSignup.getEmail()));

        Member member = new Member();
        member.setEmail(memberSignupRequestDTO.getEmail());
        member.setBirthday(memberSignupRequestDTO.getBirthday());
        member.setNickname(memberSignupRequestDTO.getNickname());
        member.setUserRole(memberSignupRequestDTO.getUserRole());

        memberRepository.save(member);
        return member;
    }

    public Member updateMember(MemberSignupRequestDTO memberSignupRequestDTO) {
        int id = SecurityUtil.getCurrentUserId();

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        member.setNickname(memberSignupRequestDTO.getNickname());
        member.setBirthday(memberSignupRequestDTO.getBirthday());
        member.setUserRole(memberSignupRequestDTO.getUserRole());

        return memberRepository.save(member);
    }

    public boolean removeMember() {
        Member member = SecurityUtil.getCurrentMember();

        if (member == null) {
            return false;
        }
        memberRepository.delete(member);
        return true;
    }

    //닉네임에 맞는 친구 데려오기
    public List<MemberSearchResponseDTO> getMemberList(String searchvalue) {

        List<Member> members = memberRepository.findByNicknameContaining(searchvalue);
        List<MemberSearchResponseDTO> memberSerachResponse = new ArrayList<>();

        for (Member member : members) {
            System.out.println("하하");
            System.out.println(member.getId());
            System.out.println(SecurityUtil.getCurrentMember().getId());

            MemberSearchResponseDTO mem = new MemberSearchResponseDTO();
            mem.setNickname(member.getNickname());

            mem.setCreatureImg(generateCreatureImgPath(member));

            if(member.getId()== SecurityUtil.getCurrentUserId()){continue;}

            if(friendService.isFriend(member.getId(), SecurityUtil.getCurrentUserId())){

                mem.setFriend(true);

            }else {

                mem.setFriend(false);
                //만약 친구이면  true 아니면 false
            }
            memberSerachResponse.add(mem);
        }

        return memberSerachResponse;
    }

    private String generateCreatureImgPath(Member member) {
            Emotion state = member.getStatus();
            Creature creature = member.getCreature();
            int creatureId = creature.getId();
            return "http://localhost:8080/image/creature/" + creatureId + "_" + state;
    }
}
