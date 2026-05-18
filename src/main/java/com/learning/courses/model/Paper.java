package com.learning.courses.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "paper")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paper_id_seq")
    @SequenceGenerator(name = "paper_id_seq", sequenceName = "paper_id_seq", allocationSize = 1)
    private Long id;

    private String title;

    private String type;

    private String isbn;

    private String topic;

    @ElementCollection
    @CollectionTable(name = "paper_authors", joinColumns = @JoinColumn(name = "paper_id"))
    @Column(name = "author_name")
    private List<String> additionalAuthors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id")

    private Person tutor;
}
