import React, { useState, useEffect } from 'react';
import './App.css';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import FilterBar from './components/FilterBar';
import taskService from './services/taskService';

function App() {
  const [tasks, setTasks] = useState([]);
  const [deletedTasks, setDeletedTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState(null);
  const [showDeleted, setShowDeleted] = useState(false);
  const [parentTaskId, setParentTaskId] = useState(null);

  // Filter states
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('');

  // Fetch tasks on component mount
  useEffect(() => {
    fetchTasks();
    fetchDeletedTasks();
  }, []);

  // Auto-hide messages after 3 seconds
  useEffect(() => {
    if (error || success) {
      const timer = setTimeout(() => {
        setError(null);
        setSuccess(null);
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [error, success]);

  const fetchTasks = async () => {
    try {
      setLoading(true);
      const response = await taskService.getAllTasks();
      setTasks(response.data || []);
      setError(null);
    } catch (err) {
      setError('Failed to fetch tasks. Please try again.');
      console.error('Error fetching tasks:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchDeletedTasks = async () => {
    try {
      const response = await taskService.getDeletedTasks();
      setDeletedTasks(response.data || []);
    } catch (err) {
      console.error('Error fetching deleted tasks:', err);
    }
  };

  const handleCreateTask = async (taskData) => {
    try {
      const taskToCreate = {
        ...taskData,
        parentTaskId: parentTaskId
      };

      await taskService.createTask(taskToCreate);
      setSuccess(parentTaskId ? 'Subtask created successfully!' : 'Task created successfully!');
      setShowForm(false);
      setParentTaskId(null);
      fetchTasks();
    } catch (err) {
      setError('Failed to create task. Please try again.');
      console.error('Error creating task:', err);
    }
  };

  const handleUpdateTask = async (taskData) => {
    try {
      await taskService.updateTask(editingTask.id, taskData);
      setSuccess('Task updated successfully!');
      setEditingTask(null);
      setShowForm(false);
      fetchTasks();
    } catch (err) {
      setError('Failed to update task. Please try again.');
      console.error('Error updating task:', err);
    }
  };

  const handleDeleteTask = async (taskId) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await taskService.deleteTask(taskId);
        setSuccess('Task deleted successfully!');
        fetchTasks();
        fetchDeletedTasks();
      } catch (err) {
        setError('Failed to delete task. Please try again.');
        console.error('Error deleting task:', err);
      }
    }
  };

  const handleRestoreTask = async (taskId) => {
    try {
      await taskService.restoreTask(taskId);
      setSuccess('Task restored successfully!');
      fetchTasks();
      fetchDeletedTasks();
    } catch (err) {
      setError('Failed to restore task. Please try again.');
      console.error('Error restoring task:', err);
    }
  };

  const handleToggleStatus = async (taskId, updatedTask) => {
    try {
      await taskService.updateTask(taskId, updatedTask);
      setSuccess('Task status updated successfully!');
      fetchTasks();
    } catch (err) {
      setError('Failed to update task status. Please try again.');
      console.error('Error updating task status:', err);
    }
  };

  const handleEditTask = (task) => {
    setEditingTask(task);
    setShowForm(true);
    setParentTaskId(null);
  };

  const handleAddSubtask = (parentId) => {
    setParentTaskId(parentId);
    setEditingTask(null);
    setShowForm(true);
  };

  const handleCancelForm = () => {
    setShowForm(false);
    setEditingTask(null);
    setParentTaskId(null);
  };

  const clearFilters = () => {
    setSearchTerm('');
    setStatusFilter('');
    setCategoryFilter('');
    setPriorityFilter('');
  };

  const getTaskStats = () => {
    const total = tasks.length;
    const completed = tasks.filter(t => t.status === 'COMPLETED').length;
    const pending = tasks.filter(t => t.status === 'PENDING').length;
    const inProgress = tasks.filter(t => t.status === 'IN_PROGRESS').length;

    return { total, completed, pending, inProgress };
  };

  const stats = getTaskStats();

  if (loading) {
    return <div className="loading">Loading tasks...</div>;
  }

  return (
    <div className="app">
      <header className="header">
        <h1>Task Manager</h1>
        <p>Organize your tasks efficiently and stay productive</p>
      </header>

      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

      <div className="stats">
        <div className="stat-card">
          <div className="stat-number">{stats.total}</div>
          <div className="stat-label">Total Tasks</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{stats.pending}</div>
          <div className="stat-label">Pending</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{stats.inProgress}</div>
          <div className="stat-label">In Progress</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{stats.completed}</div>
          <div className="stat-label">Completed</div>
        </div>
      </div>

      <div className="main-content">
        <div className="task-section">
          {showForm && (
            <TaskForm
              task={editingTask}
              onSubmit={editingTask ? handleUpdateTask : handleCreateTask}
              onCancel={handleCancelForm}
              isEditing={!!editingTask}
            />
          )}

          <FilterBar
            searchTerm={searchTerm}
            setSearchTerm={setSearchTerm}
            statusFilter={statusFilter}
            setStatusFilter={setStatusFilter}
            categoryFilter={categoryFilter}
            setCategoryFilter={setCategoryFilter}
            priorityFilter={priorityFilter}
            setPriorityFilter={setPriorityFilter}
            onClearFilters={clearFilters}
          />

          <TaskList
            tasks={showDeleted ? deletedTasks : tasks}
            onEdit={handleEditTask}
            onDelete={showDeleted ? handleRestoreTask : handleDeleteTask}
            onToggleStatus={handleToggleStatus}
            onAddSubtask={handleAddSubtask}
            searchTerm={searchTerm}
            statusFilter={statusFilter}
            categoryFilter={categoryFilter}
            priorityFilter={priorityFilter}
          />
        </div>

        <div className="sidebar">
          <div style={{ marginBottom: '20px' }}>
            <button
              className="btn btn-primary"
              style={{ width: '100%', marginBottom: '10px' }}
              onClick={() => {
                setShowForm(true);
                setEditingTask(null);
                setParentTaskId(null);
              }}
            >
              Create New Task
            </button>

            <button
              className="btn btn-secondary"
              style={{ width: '100%' }}
              onClick={() => setShowDeleted(!showDeleted)}
            >
              {showDeleted ? 'Show Active Tasks' : `Show Deleted (${deletedTasks.length})`}
            </button>
          </div>

          <div>
            <h3 style={{ marginBottom: '15px', color: '#333' }}>Quick Actions</h3>
            <button
              className="btn btn-secondary btn-small"
              style={{ width: '100%', marginBottom: '5px' }}
              onClick={() => setStatusFilter('PENDING')}
            >
              View Pending Tasks
            </button>
            <button
              className="btn btn-secondary btn-small"
              style={{ width: '100%', marginBottom: '5px' }}
              onClick={() => setCategoryFilter('URGENT')}
            >
              View Urgent Tasks
            </button>
            <button
              className="btn btn-secondary btn-small"
              style={{ width: '100%', marginBottom: '5px' }}
              onClick={() => setPriorityFilter('HIGH')}
            >
              View High Priority
            </button>
            <button
              className="btn btn-secondary btn-small"
              style={{ width: '100%' }}
              onClick={clearFilters}
            >
              Clear All Filters
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;