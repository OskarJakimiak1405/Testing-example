package com.learning.courses.service;

import com.learning.courses.model.Paper;
import com.learning.courses.model.Person;
import com.learning.courses.model.enums.Role;
import com.learning.courses.repository.PaperRepository;
import com.learning.courses.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaperServiceTest {

    @Mock
    private PaperRepository paperRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PaperService paperService;

    private Person tutor;
    private Paper paper;

    @BeforeEach
    void setUp() {
        tutor = Person.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .role(Role.TUTOR)
                .papers(new ArrayList<>())
                .build();

        paper = Paper.builder()
                .id(1L)
                .title("Test Paper")
                .type("Article")
                .isbn("978-3-16-148410-0")
                .topic("Computer Science")
                .additionalAuthors(List.of("Author One", "Author Two"))
                .build();
    }

    // -------------------------
    // createPaper
    // -------------------------

    @Test
    void createPaper_shouldSavePaper_whenTutorExists() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(tutor));
        when(paperRepository.save(any(Paper.class))).thenReturn(paper);

        Paper result = paperService.createPaper(1L, paper);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Paper");
        verify(paperRepository, times(1)).save(paper);
    }

    @Test
    void createPaper_shouldThrow_whenPersonIsNotTutor() {
        tutor.setRole(Role.STUDENT); // nie tutor
        when(personRepository.findById(1L)).thenReturn(Optional.of(tutor));

        assertThatThrownBy(() -> paperService.createPaper(1L, paper))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tutor not found or person is not a tutor!");

        verify(paperRepository, never()).save(any());
    }

    @Test
    void createPaper_shouldThrow_whenTutorNotFound() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paperService.createPaper(99L, paper))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tutor not found or person is not a tutor!");

        verify(paperRepository, never()).save(any());
    }

    // -------------------------
    // getPapersByTutor
    // -------------------------

    @Test
    void getPapersByTutor_shouldReturnList_whenTutorExists() {
        when(personRepository.existsById(1L)).thenReturn(true);
        when(paperRepository.findByTutorId(1L)).thenReturn(List.of(paper));

        List<Paper> result = paperService.getPapersByTutor(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Paper");
    }

    @Test
    void getPapersByTutor_shouldThrow_whenTutorNotFound() {
        when(personRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> paperService.getPapersByTutor(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tutor not found with id: 99");

        verify(paperRepository, never()).findByTutorId(any());
    }

    // -------------------------
    // getPaperById
    // -------------------------

    @Test
    void getPaperById_shouldReturnPaper_whenExists() {
        when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));

        Paper result = paperService.getPaperById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getPaperById_shouldThrow_whenNotFound() {
        when(paperRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paperService.getPaperById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Paper not found with id: 99");
    }

    // -------------------------
    // updatePaper
    // -------------------------

    @Test
    void updatePaper_shouldUpdateFields_whenPaperExists() {
        Paper updated = Paper.builder()
                .title("Updated Title")
                .type("Book")
                .isbn("000-0-00-000000-0")
                .topic("Mathematics")
                .additionalAuthors(List.of("New Author"))
                .build();

        when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));
        when(paperRepository.save(any(Paper.class))).thenAnswer(inv -> inv.getArgument(0));

        Paper result = paperService.updatePaper(1L, updated);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getType()).isEqualTo("Book");
        assertThat(result.getIsbn()).isEqualTo("000-0-00-000000-0");
        assertThat(result.getTopic()).isEqualTo("Mathematics");
        assertThat(result.getAdditionalAuthors()).containsExactly("New Author");
        verify(paperRepository).save(paper);
    }

    @Test
    void updatePaper_shouldThrow_whenPaperNotFound() {
        when(paperRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paperService.updatePaper(99L, paper))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Paper not found with id: 99");

        verify(paperRepository, never()).save(any());
    }

    // -------------------------
    // deletePaper
    // -------------------------

    @Test
    void deletePaper_shouldDelete_whenPaperExists() {
        when(paperRepository.existsById(1L)).thenReturn(true);

        paperService.deletePaper(1L);

        verify(paperRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePaper_shouldThrow_whenPaperNotFound() {
        when(paperRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> paperService.deletePaper(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Paper not found with id: 99");

        verify(paperRepository, never()).deleteById(any());
    }
}