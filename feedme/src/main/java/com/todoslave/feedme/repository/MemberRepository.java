package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.merge(member);
    }

    public Member findById(int id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByNickname(String name) {
        return em.createQuery("select m.member from MemberRenewInfo m where m.nickname = :name", Member.class)
                .setParameter("name",name).getResultList();
    }




}
