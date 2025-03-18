package ru.petspring.manti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.petspring.manti.entity.TaskEntity;
import ru.petspring.manti.entity.taskFilterOrSortMethod.SortByDateOrStatus;
import ru.petspring.manti.entity.taskFilterOrSortMethod.FilterByStatus;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {


    List<TaskEntity> findByStatusAndUserEntity_Id(FilterByStatus status, Long user_id);

    void deleteByIdAndUserEntity_Id(Long id, Long user_id);

    default List<TaskEntity> sortTasks(SortByDateOrStatus filter, Long user_id) {
        switch (filter) {
            case BY_DATE_ASC -> {
                return findAllByUserEntityIdOrderByDueDateAsc(user_id);
            }
            case BY_DATE_DESC -> {
                return findAllByUserEntityIdOrderByDueDateDesc(user_id);
            }
            case BY_STATUS -> {
                return findAllByUserEntityIdOrderByStatusAsc(user_id);
            }
            default -> {
                return findAllByUserEntityId(user_id);
            }
        }
    }

    // Сортировка по дате (по возрастанию) для конкретного пользователя
    @Query("SELECT t FROM TaskEntity t WHERE t.userEntity.id = :user_id ORDER BY t.dueDate ASC")
    List<TaskEntity> findAllByUserEntityIdOrderByDueDateAsc(@Param("user_id") Long user_id);

    // Сортировка по дате (по убыванию) для конкретного пользователя
    @Query("SELECT t FROM TaskEntity t WHERE t.userEntity.id = :user_id ORDER BY t.dueDate DESC")
    List<TaskEntity> findAllByUserEntityIdOrderByDueDateDesc(@Param("user_id") Long user_id);

    // Сортировка по статусу для конкретного пользователя
    @Query("SELECT t FROM TaskEntity t WHERE t.userEntity.id = :user_id ORDER BY t.status ASC")
    List<TaskEntity> findAllByUserEntityIdOrderByStatusAsc(@Param("user_id") Long user_id);

    // Получение всех задач для конкретного пользователя
    @Query("SELECT t FROM TaskEntity t WHERE t.userEntity.id = :user_id")
    List<TaskEntity> findAllByUserEntityId(@Param("user_id") Long user_id);


}

