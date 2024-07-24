package com.todoslave.feedme;

import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.domain.entity.membership.MemberDetail;
import com.todoslave.feedme.domain.entity.membership.MemberSpace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeedmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedmeApplication.class, args);

//		Member member = new Member();
//		member.setEmail("test@todoslave.com");
//		member.setPassword("123456");
//		member.setToken("ABC");
//
//		MemberSpace memberSpace = new MemberSpace();
//		memberSpace.setMember(member);
//		memberSpace.setLatitude(11.11);
//		memberSpace.setLongitude(22.22);
//
//		MemberDetail memberDetail = new MemberDetail();
//		memberDetail.setMember(member);
//		memberDetail.setExp(0);
//		memberDetail.setStatus("BASIC");
//		memberDetailRepository.save(memberDetail);
//

	}

}
