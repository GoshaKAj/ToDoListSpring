package ru.petspring.manti.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.petspring.manti.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String name;
    List<TaskDTO> tasks;

public static UserDTO toModelUser(UserEntity userEntity){
    return new UserDTO(
            userEntity.getId(),
            userEntity.getName(),
            null
    );
}

public static UserDTO toModelUserWithTasks(UserEntity userEntity){
    UserDTO modelWithTasks = toModelUser(userEntity);
    modelWithTasks.setTasks(userEntity.getTaskEntity()
            .stream()
            .map(TaskDTO::toModel)
            .collect(Collectors.toList()));
        return modelWithTasks;
    }

}
