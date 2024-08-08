package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.avatar.Creature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatureRepository extends JpaRepository<Creature, Integer> {

  Creature findByMemberId(int memberId);

}
