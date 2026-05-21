package com.learning.courses.api.rest.controller;

import com.learning.courses.model.Paper;
import com.learning.courses.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/papers")
@RequiredArgsConstructor
public class PaperController {
    private final PaperService paperService;

    @PostMapping("/tutor/{tutorId}")
    public ResponseEntity<Paper> createPaper(@PathVariable Long tutorId,
                                             @RequestBody Paper paper) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paperService.createPaper(tutorId, paper));
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<Paper>> getPapersByTutor(@PathVariable Long tutorId) {
        return ResponseEntity.ok(paperService.getPapersByTutor(tutorId));
    }

    @GetMapping("/{paperId}")
    public ResponseEntity<Paper> getPaperById(@PathVariable Long paperId) {
        return ResponseEntity.ok(paperService.getPaperById(paperId));
    }

    @PutMapping("/{paperId}")
    public ResponseEntity<Paper> updatePaper(@PathVariable Long paperId,
                                             @RequestBody Paper paper) {
        return ResponseEntity.ok(paperService.updatePaper(paperId, paper));
    }

    @DeleteMapping("/{paperId}")
    public ResponseEntity<Void> deletePaper(@PathVariable Long paperId) {
        paperService.deletePaper(paperId);
        return ResponseEntity.noContent().build();
    }
}
