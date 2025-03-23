package ru.petspring.manti;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.SortByDateOrStatus;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;
import ru.petspring.manti.model.TaskDTO;
import ru.petspring.manti.repository.TaskRepository;
import ru.petspring.manti.repository.UserRepository;
import ru.petspring.manti.service.implService.TaskServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void testAddTask() {
        // Arrange
       Long userId = 1L;
       UserEntity userEntity = new UserEntity();
       userEntity.setId(userId);
       userEntity.setName("NameUser");

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Title");
        taskDTO.setDescription("Description");
        taskDTO.setDueDate(LocalDate.now());
        taskDTO.setStatus(FilterByStatus.TODO);
        TaskEntity taskEntity = TaskEntity.toEntityTask(taskDTO);
        taskEntity.setUserEntity(userEntity);



        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);

        TaskDTO result = taskService.createTask(taskDTO, userId);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals(FilterByStatus.TODO, result.getStatus());
    }

    @Test
    public void testDeleteTaskBiIdTest(){
        Long userId = 1L;
        Long taskId = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setUserEntity(userEntity);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        taskService.deleteTask(taskId, userId);

        assertEquals(0, taskRepository.findAll().size());

        verify(taskRepository).delete(taskEntity);

    }
    @Test
    public void testFilterTasksByStatus(){
        Long userId = 1L;
        FilterByStatus status = FilterByStatus.TODO;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        TaskEntity taskEntity1 = new TaskEntity();
        taskEntity1.setId(1L);
        taskEntity1.setTitle("Test Title");
        taskEntity1.setDescription("Test Description");
        taskEntity1.setDueDate(LocalDate.now());
        taskEntity1.setStatus(FilterByStatus.TODO);
        taskEntity1.setUserEntity(userEntity);

        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setId(2L);
        taskEntity2.setTitle("Test Title2");
        taskEntity2.setDescription("Test Description2");
        taskEntity2.setDueDate(LocalDate.now());
        taskEntity2.setStatus(FilterByStatus.DONE);
        taskEntity2.setUserEntity(userEntity);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(taskRepository.findByStatusAndUserEntity_Id(status, userId))
                .thenReturn(List.of(taskEntity1));

        List<TaskDTO> result = taskService.filterTasksByStatus(status, userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());
    }
   @Test
    public void testSortTasks(){
        Long userId = 1L;
       SortByDateOrStatus sortByDate = SortByDateOrStatus.BY_DATE_ASC;
       SortByDateOrStatus sortByStatus = SortByDateOrStatus.BY_STATUS;

       UserEntity userEntity = new UserEntity();
       userEntity.setId(userId);

       TaskEntity assertTaskEntity = new TaskEntity();
       assertTaskEntity.setId(1L);
       assertTaskEntity.setTitle("Test Title");
       assertTaskEntity.setStatus(FilterByStatus.TODO);
       assertTaskEntity.setDueDate(LocalDate.now().plusDays(1));
       assertTaskEntity.setUserEntity(userEntity);

       TaskEntity assertTaskEntity2 = new TaskEntity();
       assertTaskEntity2.setId(2L);
       assertTaskEntity2.setTitle("Test Title2");
       assertTaskEntity2.setDueDate(LocalDate.now().plusDays(2));
       assertTaskEntity2.setStatus(FilterByStatus.DONE);
       assertTaskEntity2.setUserEntity(userEntity);

       when(userRepository.existsById(userId)).thenReturn(true);
       when(taskRepository.findAllByUserEntityIdOrderByDueDateAsc(userId))
               .thenReturn(Arrays.asList(assertTaskEntity, assertTaskEntity2));
       when(taskRepository.findAllByUserEntityIdOrderByStatusAsc(userId))
               .thenReturn(Arrays.asList(assertTaskEntity2, assertTaskEntity));

       List<TaskDTO> resultSortByDate = taskService.sortTasks(sortByDate, userId);

       assertNotNull(resultSortByDate);
       assertEquals(2, resultSortByDate.size());
       assertEquals("Test Title", resultSortByDate.get(0).getTitle());

       List<TaskDTO> resultSortByStatus = taskService.sortTasks(sortByStatus, userId);

       assertNotNull(resultSortByStatus);
       assertEquals(2, resultSortByStatus.size());
       assertEquals(FilterByStatus.DONE, resultSortByStatus.get(0).getStatus());

       verify(userRepository,times(2)).existsById(userId);
  }
    @Test
    public void testUpdateTask(){

        Long userId = 1L;
        Long taskId = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(FilterByStatus.TODO);
        existingTask.setDueDate(LocalDate.now());
        existingTask.setUserEntity(userEntity);

        TaskDTO updatedTaskAll = new TaskDTO();
        updatedTaskAll.setId(2L);
        updatedTaskAll.setTitle("New Title");
        updatedTaskAll.setDescription("New Description");
        updatedTaskAll.setStatus(FilterByStatus.IN_PROGRESS);
        updatedTaskAll.setDueDate(LocalDate.now().plusDays(1));
        //updatedTaskAll.setUserEntity(userEntity);

        TaskDTO updatedTaskWithNullFields = new TaskDTO();

        updatedTaskWithNullFields.setDescription("Description");
        updatedTaskWithNullFields.setStatus(FilterByStatus.DONE);


        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        TaskDTO result = taskService.updateTask(taskId, updatedTaskAll, userId);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(FilterByStatus.IN_PROGRESS, result.getStatus());

        TaskDTO resultWithNullFields = taskService.updateTask(taskId, updatedTaskWithNullFields,userId);

        assertNotNull(result);
        assertEquals("New Title", resultWithNullFields.getTitle());
        assertEquals("Description", resultWithNullFields.getDescription());
        assertEquals(FilterByStatus.DONE, resultWithNullFields.getStatus());

        verify(taskRepository, times(2)).findById(taskId);
        verify(taskRepository, times(2)).save(existingTask);
    }
}
