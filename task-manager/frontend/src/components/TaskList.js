import React, { useState, useEffect } from 'react';
import TaskItem from './TaskItem';
import taskService from '../services/taskService';

const TaskList = ({
  tasks,
  onEdit,
  onDelete,
  onToggleStatus,
  onAddSubtask,
  searchTerm,
  statusFilter,
  categoryFilter,
  priorityFilter
}) => {
  const [subtasksMap, setSubtasksMap] = useState({});

  // Fetch subtasks for each main task
  useEffect(() => {
    const fetchSubtasks = async () => {
      const subtasksData = {};

      for (const task of tasks) {
        if (!task.parentTaskId) { // Only fetch for main tasks
          try {
            const response = await taskService.getSubTasks(task.id);
            subtasksData[task.id] = response.data || [];
          } catch (error) {
            console.error(`Error fetching subtasks for task ${task.id}:`, error);
            subtasksData[task.id] = [];
          }
        }
      }

      setSubtasksMap(subtasksData);
    };

    if (tasks.length > 0) {
      fetchSubtasks();
    }
  }, [tasks]);

  // Filter tasks based on search and filter criteria
  const filteredTasks = tasks.filter(task => {
    // Only show main tasks (not subtasks) in the main list
    if (task.parentTaskId) return false;

    // Search filter
    if (searchTerm) {
      const searchLower = searchTerm.toLowerCase();
      if (
        !task.title?.toLowerCase().includes(searchLower) &&
        !task.description?.toLowerCase().includes(searchLower)
      ) {
        return false;
      }
    }

    // Status filter
    if (statusFilter && task.status !== statusFilter) {
      return false;
    }

    // Category filter
    if (categoryFilter && task.category !== categoryFilter) {
      return false;
    }

    // Priority filter
    if (priorityFilter && task.priority !== priorityFilter) {
      return false;
    }

    return true;
  });

  if (filteredTasks.length === 0) {
    return (
      <div className="empty-state">
        <h3>No tasks found</h3>
        <p>
          {tasks.length === 0
            ? "You haven't created any tasks yet. Click 'Create New Task' to get started!"
            : "No tasks match your current filters. Try adjusting your search criteria."
          }
        </p>
      </div>
    );
  }

  return (
    <div className="task-list">
      {filteredTasks.map(task => (
        <TaskItem
          key={task.id}
          task={task}
          onEdit={onEdit}
          onDelete={onDelete}
          onToggleStatus={onToggleStatus}
          onAddSubtask={onAddSubtask}
          subtasks={subtasksMap[task.id] || []}
        />
      ))}
    </div>
  );
};

export default TaskList;