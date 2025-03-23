package ru.petspring.manti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.SortByDateOrStatus;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;

import java.util.List;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByStatusAndUserEntity_Id(FilterByStatus status, Long user_id);
    List<TaskEntity> findAllByUserEntityIdOrderByDueDateAsc(Long user_id);
    List<TaskEntity> findAllByUserEntityIdOrderByDueDateDesc(Long user_id);
    List<TaskEntity> findAllByUserEntityIdOrderByStatusAsc(Long user_id);
    List<TaskEntity> findAllByUserEntityId(Long user_id);

}

