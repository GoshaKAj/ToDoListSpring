package ru.petspring.manti.service.implService;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petspring.manti.ecxeption.ResourceAlreadyExistsException;
import ru.petspring.manti.ecxeption.ResourceNotFoundException;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.model.UserDTO;
import ru.petspring.manti.repository.UserRepository;
import ru.petspring.manti.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
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
        return userRepository.findByName(name)
                .map(UserDTO::toModelUserWithTasks)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким именем: " + name + " не найден"));
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь с id: "+ id + " не найден");
        }
        userRepository.deleteById(id);
    }
}
