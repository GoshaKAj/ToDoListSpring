package ru.petspring.manti;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;
import ru.petspring.manti.model.TaskDTO;
import ru.petspring.manti.model.UserDTO;
import ru.petspring.manti.repository.UserRepository;
import ru.petspring.manti.service.implService.UserServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSaveUser(){

        UserEntity user = new UserEntity();
        user.setName("test Name");
        user.setId(1L);

        when(userRepository.findByName(user.getName())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = userService.saveUser(user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("test Name", result.getName());

        verify(userRepository).findByName(user.getName());
        verify(userRepository).save(user);
    }

    @Test
    public void testGetAllUsers(){

        UserEntity user = new UserEntity();
        user.setName("test Name");
        user.setId(1L);

        UserEntity user2 = new UserEntity();
        user2.setName("test Name2");
        user2.setId(2L);

        when(userRepository.findAll()).thenReturn(List.of(user, user2));

        List<UserDTO> result = userService.getAllUsers();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        verify(userRepository, times(1)).findAll();
    }
    @Test
    public void testFindByName(){
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("test Name");

        List<TaskEntity> tasks = new ArrayList<>();

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setStatus(FilterByStatus.TODO);
        task.setUserEntity(user);

        tasks.add(task);
        user.setTaskEntity(tasks);

        when(userRepository.findByName(user.getName())).thenReturn(user);

        UserDTO result = userService.findByName(user.getName());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("test Name", result.getName());
        Assertions.assertEquals("Test Title", result.getTasks().get(0).getTitle());

        verify(userRepository, times(1)).findByName(user.getName());
    }

    @Test
    public void testDeleteUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("test Name");


        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        Assertions.assertEquals(0, userService.getAllUsers().size());

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}
