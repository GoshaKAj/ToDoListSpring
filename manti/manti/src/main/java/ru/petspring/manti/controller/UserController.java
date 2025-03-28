package ru.petspring.manti.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDTO saveUser(@Valid @RequestBody UserDTO userDTO,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidStatusException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return userService.saveUser(userDTO);
    }

    @DeleteMapping("{user_id}")
    public void deleteUser(@PathVariable Long user_id){
        userService.deleteUser(user_id);
    }

    @GetMapping("{name}")
    public UserDTO getUserByName(@PathVariable String name){
        return userService.findByName(name);
    }
}
