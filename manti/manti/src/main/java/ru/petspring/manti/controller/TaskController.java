package ru.petspring.manti.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.petspring.manti.ecxeption.InvalidStatusException;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.SortByDateOrStatus;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;
import ru.petspring.manti.model.TaskDTO;
import ru.petspring.manti.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("create_task")
    public TaskDTO createTask(@Valid @RequestBody TaskEntity taskEntity,
                              BindingResult bindingResult,
                              @RequestParam Long user_id) {
        if(bindingResult.hasErrors()) {
            throw new InvalidStatusException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
       return taskService.createTask(taskEntity, user_id);
    }

    @GetMapping("filter_task/{status}")
    public List<TaskDTO> filterTasksByStatus(@PathVariable FilterByStatus status,
                                             @RequestParam Long user_id){
       return taskService.filterTasksByStatus(status, user_id);
    }

    @PutMapping("update_task/{id}")
    public TaskDTO updateTask(@PathVariable Long id,
                               @Valid @RequestBody TaskEntity taskEntity, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new InvalidStatusException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return taskService.updateTask(id, taskEntity);

    }

    @GetMapping("sort/{filter}")
    public List<TaskDTO> sortTasksByDateOrStatus(@PathVariable SortByDateOrStatus filter,
                                                 @RequestParam Long user_id){
        return taskService.sortTasks(filter, user_id);
    }

    @DeleteMapping("delete_task/{id}")
    void deleteTaskById(@PathVariable Long id,
                        @RequestParam Long user_id){
        taskService.deleteTask(id, user_id);
    }
}
