package com.learning.courses.model;

import com.learning.courses.model.enums.ContactType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    private String value;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}