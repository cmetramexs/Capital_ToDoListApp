import React from 'react';

const FilterBar = ({
  searchTerm,
  setSearchTerm,
  statusFilter,
  setStatusFilter,
  categoryFilter,
  setCategoryFilter,
  priorityFilter,
  setPriorityFilter,
  onClearFilters
}) => {
  return (
    <div className="filter-bar">
      <input
        type="text"
        placeholder="Search tasks..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />

      <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
        <option value="">All Status</option>
        <option value="PENDING">Pending</option>
        <option value="IN_PROGRESS">In Progress</option>
        <option value="COMPLETED">Completed</option>
      </select>

      <select value={categoryFilter} onChange={(e) => setCategoryFilter(e.target.value)}>
        <option value="">All Categories</option>
        <option value="WORK">Work</option>
        <option value="PERSONAL">Personal</option>
        <option value="URGENT">Urgent</option>
        <option value="SHOPPING">Shopping</option>
        <option value="HEALTH">Health</option>
        <option value="EDUCATION">Education</option>
      </select>

      <select value={priorityFilter} onChange={(e) => setPriorityFilter(e.target.value)}>
        <option value="">All Priorities</option>
        <option value="LOW">Low</option>
        <option value="MEDIUM">Medium</option>
        <option value="HIGH">High</option>
        <option value="URGENT">Urgent</option>
      </select>

      <button className="btn btn-secondary btn-small" onClick={onClearFilters}>
        Clear Filters
      </button>
    </div>
  );
};

export default FilterBar;