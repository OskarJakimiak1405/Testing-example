package com.learning.courses.service;

import com.learning.courses.model.Paper;
import com.learning.courses.model.enums.Role;
import com.learning.courses.repository.PaperRepository;
import com.learning.courses.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaperService {
    private final PaperRepository paperRepository;
    private final PersonRepository personRepository;

    @Transactional
    public Paper createPaper(Long tutorId, Paper paper) {
        return personRepository.findById(tutorId)
                .filter(person -> person.getRole() == Role.TUTOR)
                .map(tutor -> {
                    paper.setTutor(tutor);
                    return paperRepository.save(paper);
                })
                .orElseThrow(() -> new RuntimeException("Tutor not found or person is not a tutor!"));
    }

    // Pozostałe metody: get, update, delete...
}
