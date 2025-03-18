package ru.petspring.manti.service.implService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO createTask(TaskEntity taskEntity, Long user_id) {
        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id: " + user_id + " не найден"));
        taskEntity.setUserEntity(userEntity);
        return TaskDTO.toModel(taskRepository.save(taskEntity));
    }

    @Override
    public void deleteTask(Long id, Long user_id) {
        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id: " + user_id + " не найден"));
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Задача с id: " + id + " не найдена"));
        taskRepository.deleteByIdAndUserEntity_Id(id, user_id);
    }

    @Override
    public List<TaskDTO> filterTasksByStatus(FilterByStatus status, Long user_id) {
        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id: " + user_id + " не найден"));

        return  taskRepository.findByStatusAndUserEntity_Id(status, user_id )
                .stream()
                .map(TaskDTO::toModel)
                .toList();
    }

    @Override
    public List<TaskDTO> sortTasks(SortByDateOrStatus filter, Long user_id) {
        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id: " + user_id + " не найден"));

        return taskRepository.sortTasks(filter, user_id)
                .stream()
                .map(TaskDTO::toModel)
                .toList();
    }

    @Override
    public TaskDTO updateTask(Long id, TaskEntity taskEntity) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задачи не существует"));

        existingTask.setTitle(taskEntity.getTitle());
        existingTask.setDescription(taskEntity.getDescription());
        existingTask.setStatus(taskEntity.getStatus());
        existingTask.setDueDate(taskEntity.getDueDate());

        return TaskDTO.toModel(taskRepository.save(existingTask));
    }
}
