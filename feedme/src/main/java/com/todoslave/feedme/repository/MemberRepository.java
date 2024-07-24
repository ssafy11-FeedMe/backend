package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findById(int id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname",
                        Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }




}
