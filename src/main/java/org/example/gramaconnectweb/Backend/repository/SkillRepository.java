package org.example.gramaconnectweb.Backend.repository;

import org.example.gramaconnectweb.Backend.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
