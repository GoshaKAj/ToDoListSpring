package ru.petspring.manti.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.petspring.manti.enums.Status;
import ru.petspring.manti.model.TaskDTO;

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
    @NotNull(message = "Статус обязателен")
    private Status status;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static TaskEntity toEntityTask(TaskDTO taskDTO) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDTO.getTitle());
        taskEntity.setDescription(taskDTO.getDescription());
        taskEntity.setDueDate(taskDTO.getDueDate());
        taskEntity.setStatus(taskDTO.getStatus());
        return taskEntity;
    }
}
