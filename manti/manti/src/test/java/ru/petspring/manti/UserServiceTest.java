package ru.petspring.manti;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.petspring.manti.ecxeption.ResourceNotFoundException;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.enums.Status;
import ru.petspring.manti.model.UserDTO;
import ru.petspring.manti.repository.UserRepository;
import ru.petspring.manti.service.implService.UserServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        UserDTO user = new UserDTO();
        user.setName("test Name");

        UserEntity userEntity = UserEntity.toUserEntity(user);

        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserDTO result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals("test Name", result.getName());
        verify(userRepository).save(userEntity);
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

        assertNotNull(result);
        assertEquals(2, result.size());

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
        task.setStatus(Status.TODO);
        task.setUserEntity(user);

        tasks.add(task);
        user.setTaskEntity(tasks);

        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));

        UserDTO result = userService.findByName(user.getName());

        assertNotNull(result);
        assertEquals("test Name", result.getName());
        assertEquals("Test Title", result.getTasks().get(0).getTitle());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.findByName(null));
        assertEquals("Пользователь с таким именем: " + null + " не найден", exception.getMessage());

        verify(userRepository, times(1)).findByName(user.getName());
    }

    @Test
    public void testDeleteUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("test Name");

        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(user.getId());

        assertEquals(0, userService.getAllUsers().size());

        Long exceptionUserId = 321L;
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(exceptionUserId));
        assertEquals("Пользователь с id: "+ exceptionUserId + " не найден", exception.getMessage() );

        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}
