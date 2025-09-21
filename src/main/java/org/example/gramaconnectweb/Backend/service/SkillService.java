package org.example.gramaconnectweb.Backend.service;

import org.example.gramaconnectweb.Backend.entity.Skill;
import org.example.gramaconnectweb.Backend.exception.ResourceNotFoundException;
import org.example.gramaconnectweb.Backend.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Long id, Skill skillDetails) {
        Skill skill = getSkillById(id);
        skill.setCategory(skillDetails.getCategory());
        skill.setName(skillDetails.getName());
        skill.setDescription(skillDetails.getDescription());
        skill.setPrice(skillDetails.getPrice());
        skill.setContact(skillDetails.getContact());
        return skillRepository.save(skill);
    }

    public void deleteSkill(Long id) {
        Skill skill = getSkillById(id);
        skillRepository.delete(skill);
    }
}
