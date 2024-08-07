package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.membership.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository //Component가 들어가있다.
@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Integer id);
    Member findByNickname(String nickname);
    Optional<Member> findByToken(String token);
    List<Member> findAllByBirthday(LocalDate Birthday);

}
