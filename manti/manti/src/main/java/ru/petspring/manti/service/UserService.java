package ru.petspring.manti.service;

import org.springframework.stereotype.Service;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.model.UserDTO;

import java.util.List;

@Service
public interface UserService {

    UserDTO saveUser(UserEntity userEntity);
    void deleteUser(Long id);
    List<UserDTO> getAllUsers();
    UserDTO findByName(String name);
}
