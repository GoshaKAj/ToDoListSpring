package ru.petspring.manti.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.petspring.manti.ecxeption.InvalidStatusException;
import ru.petspring.manti.ecxeption.ResourceNotFoundException;
import ru.petspring.manti.entity.UserEntity;
import ru.petspring.manti.model.UserDTO;
import ru.petspring.manti.service.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("create_user")
    public UserDTO saveUser(@Valid @RequestBody UserEntity userEntity,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidStatusException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return userService.saveUser(userEntity);
    }

    @DeleteMapping("delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @GetMapping("select/{name}")
    public UserDTO getUserByName(@PathVariable String name){
        return userService.findByName(name);
    }
}
