package com.learning.courses.service;

import com.learning.courses.model.Paper;
import com.learning.courses.model.enums.Role;
import com.learning.courses.repository.PaperRepository;
import com.learning.courses.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                    tutor.addPaper(paper);
                    return paperRepository.save(paper);
                })
                .orElseThrow(() -> new RuntimeException("Tutor not found or person is not a tutor!"));
    }

    public List<Paper> getPapersByTutor(Long tutorId) {
        if (!personRepository.existsById(tutorId)) {
            throw new RuntimeException("Tutor not found with id: " + tutorId);
        }
        return paperRepository.findByTutorId(tutorId);
    }

    public Paper getPaperById(Long paperId) {
        return paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with id: " + paperId));
    }

    @Transactional
    public Paper updatePaper(Long paperId, Paper updatedPaper) {
        return paperRepository.findById(paperId)
                .map(existing -> {
                    existing.setTitle(updatedPaper.getTitle());
                    existing.setType(updatedPaper.getType());
                    existing.setIsbn(updatedPaper.getIsbn());
                    existing.setTopic(updatedPaper.getTopic());
                    existing.setAdditionalAuthors(updatedPaper.getAdditionalAuthors());
                    return paperRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Paper not found with id: " + paperId));
    }

    @Transactional
    public void deletePaper(Long paperId) {
        if (!paperRepository.existsById(paperId)) {
            throw new RuntimeException("Paper not found with id: " + paperId);
        }
        paperRepository.deleteById(paperId);
    }
}