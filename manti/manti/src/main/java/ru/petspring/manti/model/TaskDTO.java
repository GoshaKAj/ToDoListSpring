package ru.petspring.manti.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.enums.Status;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    public static TaskDTO toModel(TaskEntity taskEntity){
        TaskDTO model = new TaskDTO();
        model.setTitle(taskEntity.getTitle());
        model.setDescription(taskEntity.getDescription());
        model.setDueDate(taskEntity.getDueDate());
        model.setStatus(taskEntity.getStatus());
        return model;
    }
}
