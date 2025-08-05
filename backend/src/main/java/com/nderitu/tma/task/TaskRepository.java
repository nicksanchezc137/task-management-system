package com.nderitu.tma.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nderitu.tma.user.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:assignee IS NULL OR t.assignee = :assignee)")
    Page<Task> findByStatusAndAssignee(
            @Param("status") TaskStatus status,
            @Param("assignee") User assignee,
            Pageable pageable
    );

    List<Task> findByAssignee(User assignee);

    List<Task> findByCreator(User creator);

    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.assignee = :assignee AND t.status = :status")
    List<Task> findByAssigneeAndStatus(@Param("assignee") User assignee, @Param("status") TaskStatus status);
} 