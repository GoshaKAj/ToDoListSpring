package ru.petspring.manti.service.implService;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petspring.manti.ecxeption.ResourceAlreadyExistsException;
import ru.petspring.manti.ecxeption.ResourceNotFoundException;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.model.UserDTO;
import ru.petspring.manti.repository.UserRepository;
import ru.petspring.manti.service.UserService;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO saveUser(UserEntity userEntity) {
        if(userRepository.findByName(userEntity.getName()) != null) {
            throw new ResourceAlreadyExistsException("Пользователь с таким именем: " + userEntity.getName() + ", уже существует");
        }
        return UserDTO.toModelUser(userRepository.save(userEntity));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::toModelUser)
                .toList();
    }

    @Override
    public UserDTO findByName(String name) {
        UserEntity userEntity = userRepository.findByName(name);
        if(userEntity == null) {
            throw new ResourceNotFoundException("Пользователь с таким именем: " + name + " не найден");
        }
        return UserDTO.toModelUserWithTasks(userEntity);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id: "+ id + " не найден"));
        userRepository.deleteById(id);
    }
}
