package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

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
        Mockito.when(taskService.getAllTasks()).thenReturn(testTasks);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("Parent Task")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].priority", Matchers.is("HIGH")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].category", Matchers.is("WORK")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("PENDING")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", Matchers.is("Test Task")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].priority", Matchers.is("LOW")));

        Mockito.verify(taskService, Mockito.times(1)).getAllTasks();
    }

    @Test
    void getAllTasks_ShouldReturnEmptyList_WhenNoTasks() throws Exception {
        // Arrange
        Mockito.when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(taskService, Mockito.times(1)).getAllTasks();
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() throws Exception {
        // Arrange
        Mockito.when(taskService.getTaskById(1L)).thenReturn(Optional.of(parentTask));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Parent Task")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Parent task description")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.priority", Matchers.is("HIGH")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category", Matchers.is("WORK")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("PENDING")));

        Mockito.verify(taskService, Mockito.times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        Mockito.when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(taskService, Mockito.times(1)).getTaskById(999L);
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

        Mockito.when(taskService.createTask(ArgumentMatchers.any(Task.class))).thenReturn(createdTask);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(4)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("New Task")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("New Description")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.priority", Matchers.is("MEDIUM")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category", Matchers.is("PERSONAL")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("PENDING")));

        Mockito.verify(taskService, Mockito.times(1)).createTask(ArgumentMatchers.any(Task.class));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenTitleIsEmpty() throws Exception {
        // Arrange
        Task invalidTask = new Task();
        invalidTask.setTitle(""); // Empty title
        invalidTask.setDescription("Description");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTask)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).createTask(ArgumentMatchers.any(Task.class));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenTitleIsTooLong() throws Exception {
        // Arrange
        Task invalidTask = new Task();
        invalidTask.setTitle("a".repeat(201)); // Title too long (>200 chars)
        invalidTask.setDescription("Description");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTask)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).createTask(ArgumentMatchers.any(Task.class));
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

        Mockito.when(taskService.updateTask(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Task.class))).thenReturn(updatedTask);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Updated Task")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Updated Description")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.priority", Matchers.is("URGENT")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category", Matchers.is("URGENT")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("COMPLETED")));

        Mockito.verify(taskService, Mockito.times(1)).updateTask(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Task.class));
    }

    @Test
    void updateTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        Task updateData = new Task();
        updateData.setTitle("Updated Task");

        Mockito.when(taskService.updateTask(ArgumentMatchers.eq(999L), ArgumentMatchers.any(Task.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(taskService, Mockito.times(1)).updateTask(ArgumentMatchers.eq(999L), ArgumentMatchers.any(Task.class));
    }

    @Test
    void deleteTask_ShouldReturnNoContent_WhenTaskExists() throws Exception {
        // Arrange
        Mockito.when(taskService.deleteTask(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(taskService, Mockito.times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        Mockito.when(taskService.deleteTask(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(taskService, Mockito.times(1)).deleteTask(999L);
    }
}