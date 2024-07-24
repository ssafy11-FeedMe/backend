package com.todoslave.feedme;

import com.todoslave.feedme.domain.entity.membership.Emotion;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.domain.entity.membership.MemberDetail;
import com.todoslave.feedme.domain.entity.membership.MemberSpace;
import com.todoslave.feedme.domain.entity.task.CreatureTodo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class FeedmeApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Test
	public void testDatabaseOperations() {
		Member member = new Member();
		member.setEmail("test@todoslave.com");
		member.setPassword("123456");
		member.setToken("ABC");
		entityManager.persist(member);

		Member member2 = new Member();
		member2.setEmail("test2@todoslave.com");
		member2.setPassword("1234567");
		member2.setToken("ABCD");
		entityManager.persist(member2);

		MemberSpace memberSpace = new MemberSpace();
		memberSpace.setMember(member);
		memberSpace.setLatitude(11.11);
		memberSpace.setLongitude(22.22);
		entityManager.persist(memberSpace);

		MemberDetail memberDetail = new MemberDetail();
		memberDetail.setMember(member);
		memberDetail.setExp(0);
		memberDetail.setStatus(Emotion.BASIC);
		entityManager.persist(memberDetail);

		CreatureTodo creatureTodo = new CreatureTodo();
		creatureTodo.setContent("Test Todo");
		creatureTodo.setMember(member);
		entityManager.persist(creatureTodo);

		// Friend friend = new Friend();
		// friend.setCounterpart_id();
		// friend.setMember(member);
		// entityManager.persist(friend);

		// Flush the entity manager to ensure data is written to the database
		entityManager.flush();
	}
}
