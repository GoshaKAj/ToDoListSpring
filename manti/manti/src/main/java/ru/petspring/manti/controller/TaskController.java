package ru.petspring.manti.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.petspring.manti.ecxeption.InvalidStatusException;
import ru.petspring.manti.enums.SortByDateOrStatus;
import ru.petspring.manti.enums.Status;
import ru.petspring.manti.model.TaskDTO;
import ru.petspring.manti.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{user_id}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskDTO createTask( @PathVariable Long user_id,
                               @Valid @RequestBody TaskDTO taskDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new InvalidStatusException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
       return taskService.createTask(taskDTO, user_id);
    }

    @PutMapping("{task_id}")
    public TaskDTO updateTask(@PathVariable Long user_id,
                              @PathVariable Long task_id,
                              @Valid @RequestBody TaskDTO taskDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new InvalidStatusException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return taskService.updateTask(task_id, taskDTO, user_id);
    }

    @GetMapping("/filter")
    public List<TaskDTO> filterTasksByStatus(@PathVariable Long user_id,
                                             @RequestParam Status status){
       return taskService.filterTasksByStatus(status, user_id);
    }

    @GetMapping("/sort")
    public List<TaskDTO> sortTasksByDateOrStatus(@PathVariable Long user_id,
                                                 @RequestParam SortByDateOrStatus filter){
        return taskService.sortTasks(filter, user_id);
    }

    @DeleteMapping("{task_id}")
    void deleteTaskById(@PathVariable Long user_id,
                        @PathVariable Long task_id){
        taskService.deleteTask(task_id, user_id);
    }
}
