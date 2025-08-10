package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findByParentTaskIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setDueDate(taskDetails.getDueDate());
            task.setPriority(taskDetails.getPriority());
            task.setCategory(taskDetails.getCategory());
            task.setStatus(taskDetails.getStatus());
            return taskRepository.save(task);
        }
        return null;
    }

    public boolean deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setIsDeleted(true);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public Task restoreTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setIsDeleted(false);
            return taskRepository.save(task);
        }
        return null;
    }

    public List<Task> getTasksByStatus(Task.Status status) {
        return taskRepository.findByStatusAndIsDeletedFalse(status);
    }

    public List<Task> getTasksByCategory(Task.Category category) {
        return taskRepository.findByCategoryAndIsDeletedFalse(category);
    }

    public List<Task> searchTasks(String keyword) {
        return taskRepository.searchTasks(keyword);
    }

    public List<Task> getSubTasks(Long parentTaskId) {
        return taskRepository.findByParentTaskIdAndIsDeletedFalse(parentTaskId);
    }

    public List<Task> getDeletedTasks() {
        return taskRepository.findByIsDeletedTrueOrderByUpdatedAtDesc();
    }

    public List<Task> getTasksByDateRange(Date startDate, Date endDate) {
        return taskRepository.findTasksByDateRange(startDate, endDate);
    }
}