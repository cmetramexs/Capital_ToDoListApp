import React, { useState } from 'react';
import { format, isAfter, parseISO } from 'date-fns';

const TaskItem = ({
  task,
  onEdit,
  onDelete,
  onToggleStatus,
  onAddSubtask,
  subtasks = []
}) => {
  const [showSubtasks, setShowSubtasks] = useState(false);

  const formatDate = (dateString) => {
    if (!dateString) return null;
    try {
      return format(parseISO(dateString), 'MMM dd, yyyy');
    } catch (error) {
      return dateString;
    }
  };

  const isOverdue = (dueDate) => {
    if (!dueDate) return false;
    try {
      return isAfter(new Date(), parseISO(dueDate));
    } catch (error) {
      return false;
    }
  };

  const getPriorityClass = (priority) => {
    return `task-badge priority-${priority?.toLowerCase() || 'medium'}`;
  };

  const getStatusClass = (status) => {
    return `task-badge status-${status?.toLowerCase().replace('_', '-') || 'pending'}`;
  };

  const getCategoryClass = (category) => {
    return `task-badge category-${category?.toLowerCase() || 'personal'}`;
  };

  const handleStatusToggle = () => {
    const newStatus = task.status === 'COMPLETED' ? 'PENDING' : 'COMPLETED';
    onToggleStatus(task.id, { ...task, status: newStatus });
  };

  return (
    <div className={`task-item ${task.status === 'COMPLETED' ? 'completed' : ''}`}>
      <div className="task-header">
        <div>
          <h3 className="task-title">{task.title}</h3>
          {task.description && (
            <p className="task-description">{task.description}</p>
          )}
        </div>
      </div>

      <div className="task-meta">
        <span className={getPriorityClass(task.priority)}>
          {task.priority}
        </span>
        <span className={getStatusClass(task.status)}>
          {task.status?.replace('_', ' ')}
        </span>
        <span className={getCategoryClass(task.category)}>
          {task.category}
        </span>
        {task.dueDate && (
          <span className={`due-date ${isOverdue(task.dueDate) && task.status !== 'COMPLETED' ? 'overdue' : ''}`}>
            Due: {formatDate(task.dueDate)}
            {isOverdue(task.dueDate) && task.status !== 'COMPLETED' && ' (Overdue)'}
          </span>
        )}
      </div>

      <div className="task-actions">
        <button
          className={`btn btn-small ${task.status === 'COMPLETED' ? 'btn-secondary' : 'btn-success'}`}
          onClick={handleStatusToggle}
        >
          {task.status === 'COMPLETED' ? 'Mark Pending' : 'Mark Complete'}
        </button>
        <button className="btn btn-primary btn-small" onClick={() => onEdit(task)}>
          Edit
        </button>
        <button className="btn btn-secondary btn-small" onClick={() => onAddSubtask(task.id)}>
          Add Subtask
        </button>
        <button className="btn btn-danger btn-small" onClick={() => onDelete(task.id)}>
          Delete
        </button>
        {subtasks.length > 0 && (
          <button
            className="btn btn-secondary btn-small"
            onClick={() => setShowSubtasks(!showSubtasks)}
          >
            {showSubtasks ? 'Hide' : 'Show'} Subtasks ({subtasks.length})
          </button>
        )}
      </div>

      {showSubtasks && subtasks.length > 0 && (
        <div className="subtasks">
          <h4>Subtasks:</h4>
          {subtasks.map(subtask => (
            <div key={subtask.id} className="subtask-item">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <strong className={subtask.status === 'COMPLETED' ? 'completed' : ''}>
                    {subtask.title}
                  </strong>
                  {subtask.description && (
                    <p style={{ fontSize: '0.9rem', color: '#666', margin: '5px 0' }}>
                      {subtask.description}
                    </p>
                  )}
                  <div style={{ display: 'flex', gap: '5px', marginTop: '5px' }}>
                    <span className={getPriorityClass(subtask.priority)}>
                      {subtask.priority}
                    </span>
                    <span className={getStatusClass(subtask.status)}>
                      {subtask.status?.replace('_', ' ')}
                    </span>
                  </div>
                </div>
                <div style={{ display: 'flex', gap: '5px' }}>
                  <button
                    className="btn btn-primary btn-small"
                    onClick={() => onEdit(subtask)}
                  >
                    Edit
                  </button>
                  <button
                    className="btn btn-danger btn-small"
                    onClick={() => onDelete(subtask.id)}
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default TaskItem;