package ru.petspring.manti.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private FilterByStatus status;

    public static TaskDTO toModel(TaskEntity taskEntity){
        TaskDTO model = new TaskDTO();
        model.setId(taskEntity.getId());
        model.setTitle(taskEntity.getTitle());
        model.setDescription(taskEntity.getDescription());
        model.setDueDate(taskEntity.getDueDate());
        model.setStatus(taskEntity.getStatus());
        return model;
    }
}
