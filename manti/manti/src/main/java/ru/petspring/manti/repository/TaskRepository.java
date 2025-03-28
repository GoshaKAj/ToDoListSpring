package ru.petspring.manti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.enums.Status;

import java.util.List;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByStatusAndUserEntity_Id(Status status, Long user_id);
    List<TaskEntity> findAllByUserEntityIdOrderByDueDateAsc(Long user_id);
    List<TaskEntity> findAllByUserEntityIdOrderByDueDateDesc(Long user_id);
    List<TaskEntity> findAllByUserEntityIdOrderByStatusAsc(Long user_id);
    List<TaskEntity> findAllByUserEntityId(Long user_id);

}

