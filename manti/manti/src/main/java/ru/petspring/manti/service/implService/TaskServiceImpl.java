package ru.petspring.manti.service.implService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.petspring.manti.ecxeption.*;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.SortByDateOrStatus;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;
import ru.petspring.manti.model.TaskDTO;
import ru.petspring.manti.repository.TaskRepository;
import ru.petspring.manti.repository.UserRepository;
import ru.petspring.manti.service.TaskService;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, Long user_id) {
        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id: " + user_id + " не найден"));
        TaskEntity taskEntity = TaskEntity.toEntityTask(taskDTO);
        taskEntity.setUserEntity(userEntity);
        return TaskDTO.toModel(taskRepository.save(taskEntity));
    }

    @Override
    public void deleteTask(Long task_id, Long user_id) {
        TaskEntity taskForDelete = taskRepository.findById(task_id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id: " + task_id + " не найдена"));
        if (!taskForDelete.getUserEntity().getId().equals(user_id)) {
            throw new ResourceNotFoundException("Задача с id: " + task_id + " не принадлежит пользователю с id: " + user_id);
        }
        taskRepository.delete(taskForDelete);
    }

    @Override
    public List<TaskDTO> filterTasksByStatus(FilterByStatus status, Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ResourceNotFoundException("Пользователь с id: " + user_id + " не найден");
        }
        return  taskRepository.findByStatusAndUserEntity_Id(status, user_id )
                .stream()
                .map(TaskDTO::toModel)
                .toList();
    }

    @Override
    public List<TaskDTO> sortTasks(SortByDateOrStatus filter, Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ResourceNotFoundException("Пользователь с id: " + user_id + " не найден");
        }
            switch (filter) {
                case BY_DATE_ASC -> {
                    return taskRepository.findAllByUserEntityIdOrderByDueDateAsc(user_id)
                            .stream()
                            .map(TaskDTO::toModel)
                            .toList();
                }
                case BY_DATE_DESC -> {
                    return taskRepository.findAllByUserEntityIdOrderByDueDateDesc(user_id)
                            .stream()
                            .map(TaskDTO::toModel)
                            .toList();
                }
                case BY_STATUS -> {
                    return taskRepository.findAllByUserEntityIdOrderByStatusAsc(user_id)
                            .stream()
                            .map(TaskDTO::toModel)
                            .toList();
                }
                default -> {
                    return taskRepository.findAllByUserEntityId(user_id)
                            .stream()
                            .map(TaskDTO::toModel)
                            .toList();
                }
            }

    }

    @Override
    public TaskDTO updateTask (Long task_id, TaskDTO taskDTO, Long user_id){
        TaskEntity existingTask = taskRepository.findById(task_id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id: " + task_id + " не найдена"));
        if(!existingTask.getUserEntity().getId().equals(user_id)) {
            throw new ResourceNotFoundException("Задача с id: " + task_id + " не принадлежит пользователю с id: " + user_id);
        }

        Optional.ofNullable(taskDTO.getTitle()).ifPresent(existingTask::setTitle);
        Optional.ofNullable(taskDTO.getDescription()).ifPresent(existingTask::setDescription);
        Optional.ofNullable(taskDTO.getStatus()).ifPresent(existingTask::setStatus);
        Optional.ofNullable(taskDTO.getDueDate()).ifPresent(existingTask::setDueDate);

        return TaskDTO.toModel(taskRepository.save(existingTask));
    }

}
