package com.taskmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskmanager.controller.TaskController;
import com.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task testTask;
    private Task parentTask;
    private Task subTask;
    private List<Task> testTasks;

    @BeforeEach
    void setUp() {
        // Configure ObjectMapper for better JSON handling
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Setup test tasks
        parentTask = new Task();
        parentTask.setId(1L);
        parentTask.setTitle("Parent Task");
        parentTask.setDescription("Parent task description");
        parentTask.setPriority(Task.Priority.HIGH);
        parentTask.setCategory(Task.Category.WORK);
        parentTask.setStatus(Task.Status.PENDING);
        parentTask.setIsDeleted(false);
        parentTask.setCreatedAt(new Date());
        parentTask.setUpdatedAt(new Date());

        subTask = new Task();
        subTask.setId(2L);
        subTask.setTitle("Sub Task");
        subTask.setDescription("Sub task description");
        subTask.setPriority(Task.Priority.MEDIUM);
        subTask.setCategory(Task.Category.WORK);
        subTask.setStatus(Task.Status.PENDING);
        subTask.setParentTaskId(1L);
        subTask.setIsDeleted(false);
        subTask.setCreatedAt(new Date());
        subTask.setUpdatedAt(new Date());

        testTask = new Task();
        testTask.setId(3L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setPriority(Task.Priority.LOW);
        testTask.setCategory(Task.Category.PERSONAL);
        testTask.setStatus(Task.Status.IN_PROGRESS);
        testTask.setIsDeleted(false);
        testTask.setDueDate(new Date());
        testTask.setCreatedAt(new Date());
        testTask.setUpdatedAt(new Date());

        testTasks = Arrays.asList(parentTask, testTask);
    }

    @Test
    void getAllTasks_ShouldReturnTaskList() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(testTasks);

        // Act & Assert
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].title", is("Parent Task")))
            .andExpect(jsonPath("$[0].priority", is("HIGH")))
            .andExpect(jsonPath("$[0].category", is("WORK")))
            .andExpect(jsonPath("$[0].status", is("PENDING")))
            .andExpect(jsonPath("$[1].id", is(3)))
            .andExpect(jsonPath("$[1].title", is("Test Task")))
            .andExpect(jsonPath("$[1].priority", is("LOW")));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getAllTasks_ShouldReturnEmptyList_WhenNoTasks() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(0)));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() throws Exception {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(parentTask));

        // Act & Assert
        mockMvc.perform(get("/tasks/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.title", is("Parent Task")))
            .andExpect(jsonPath("$.description", is("Parent task description")))
            .andExpect(jsonPath("$.priority", is("HIGH")))
            .andExpect(jsonPath("$.category", is("WORK")))
            .andExpect(jsonPath("$.status", is("PENDING")));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/tasks/999"))
            .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(999L);
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        // Arrange
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setPriority(Task.Priority.MEDIUM);
        newTask.setCategory(Task.Category.PERSONAL);
        newTask.setStatus(Task.Status.PENDING);

        Task createdTask = new Task();
        createdTask.setId(4L);
        createdTask.setTitle("New Task");
        createdTask.setDescription("New Description");
        createdTask.setPriority(Task.Priority.MEDIUM);
        createdTask.setCategory(Task.Category.PERSONAL);
        createdTask.setStatus(Task.Status.PENDING);

        when(taskService.createTask(any(Task.class))).thenReturn(createdTask);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(4)))
            .andExpect(jsonPath("$.title", is("New Task")))
            .andExpect(jsonPath("$.description", is("New Description")))
            .andExpect(jsonPath("$.priority", is("MEDIUM")))
            .andExpect(jsonPath("$.category", is("PERSONAL")))
            .andExpect(jsonPath("$.status", is("PENDING")));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenTitleIsEmpty() throws Exception {
        // Arrange
        Task invalidTask = new Task();
        invalidTask.setTitle(""); // Empty title
        invalidTask.setDescription("Description");

        // Act & Assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTask)))
            .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any(Task.class));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenTitleIsTooLong() throws Exception {
        // Arrange
        Task invalidTask = new Task();
        invalidTask.setTitle("a".repeat(201)); // Title too long (>200 chars)
        invalidTask.setDescription("Description");

        // Act & Assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTask)))
            .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any(Task.class));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask_WhenTaskExists() throws Exception {
        // Arrange
        Task updateData = new Task();
        updateData.setTitle("Updated Task");
        updateData.setDescription("Updated Description");
        updateData.setPriority(Task.Priority.URGENT);
        updateData.setCategory(Task.Category.URGENT);
        updateData.setStatus(Task.Status.COMPLETED);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setPriority(Task.Priority.URGENT);
        updatedTask.setCategory(Task.Category.URGENT);
        updatedTask.setStatus(Task.Status.COMPLETED);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        // Act & Assert
        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.title", is("Updated Task")))
            .andExpect(jsonPath("$.description", is("Updated Description")))
            .andExpect(jsonPath("$.priority", is("URGENT")))
            .andExpect(jsonPath("$.category", is("URGENT")))
            .andExpect(jsonPath("$.status", is("COMPLETED")));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    void updateTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        Task updateData = new Task();
        updateData.setTitle("Updated Task");

        when(taskService.updateTask(eq(999L), any(Task.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
            .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(999L), any(Task.class));
    }

    @Test
    void deleteTask_ShouldReturnNoContent_WhenTaskExists() throws Exception {
        // Arrange
        when(taskService.deleteTask(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/tasks/1"))
            .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        when(taskService.deleteTask(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/tasks/999"))
            .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(999L);
    }
}