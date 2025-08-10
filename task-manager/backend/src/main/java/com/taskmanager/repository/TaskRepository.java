package com.taskmanager.repository;

import com.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByIsDeletedFalseOrderByCreatedAtDesc();

    List<Task> findByStatusAndIsDeletedFalse(Task.Status status);

    List<Task> findByCategoryAndIsDeletedFalse(Task.Category category);

    List<Task> findByParentTaskIdAndIsDeletedFalse(Long parentTaskId);

    List<Task> findByParentTaskIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc();

    List<Task> findByIsDeletedTrueOrderByUpdatedAtDesc();

    @Query("SELECT t FROM Task t WHERE t.isDeleted = false AND " +
        "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> searchTasks(@Param("keyword") String keyword);

    @Query("SELECT t FROM Task t WHERE t.isDeleted = false AND t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}