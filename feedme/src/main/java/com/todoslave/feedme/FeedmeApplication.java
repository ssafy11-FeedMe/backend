package com.todoslave.feedme;

import com.todoslave.feedme.domain.entity.membership.Emotion;
import com.todoslave.feedme.domain.entity.membership.Member;

import com.todoslave.feedme.repository.MemberRepository;
import com.todoslave.feedme.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeedmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedmeApplication.class, args);


		Member member = new Member();

		member.setEmail("asdf@gmail.com");
		member.setToken("ABC");
		member.setExp(0);
		member.setStatus(Emotion.SAD);
		member.setNickname("테스트1");
		member.setLatitude(12.4);
		member.setLongitude(12.4);

		//When
		MemberService memberService = new MemberService();
		int saveId = memberService.Join(member);

	}

}
