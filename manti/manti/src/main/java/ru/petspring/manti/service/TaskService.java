package ru.petspring.manti.service;

import org.springframework.stereotype.Service;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.SortByDateOrStatus;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;
import ru.petspring.manti.model.TaskDTO;

import java.util.List;

@Service
public interface TaskService {

    TaskDTO createTask(TaskEntity taskEntity, Long user_id);

    TaskDTO updateTask(Long id, TaskEntity taskEntity);

    List<TaskDTO> filterTasksByStatus(FilterByStatus status, Long user_id);

    void deleteTask(Long id, Long user_id);

    List<TaskDTO> sortTasks(SortByDateOrStatus filter, Long user_id);

}