package ru.petspring.manti.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;

import java.time.LocalDate;

@Entity
@Table(name="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @NotEmpty(message = "Name of title should not empty")
    private String title;

    private String description;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private FilterByStatus status;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
