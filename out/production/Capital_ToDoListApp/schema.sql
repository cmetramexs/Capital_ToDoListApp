-- Create Tasks table
CREATE TABLE tasks (
    id NUMBER(10) PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    description CLOB,
    due_date DATE,
    priority VARCHAR2(20) DEFAULT 'MEDIUM',
    category VARCHAR2(50) DEFAULT 'PERSONAL',
    status VARCHAR2(20) DEFAULT 'PENDING',
    parent_task_id NUMBER(10),
    is_deleted NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_parent_task FOREIGN KEY (parent_task_id) REFERENCES tasks(id)
);

-- Create sequence for auto-increment
CREATE SEQUENCE task_seq START WITH 1 INCREMENT BY 1;

-- Create trigger for auto-increment
CREATE OR REPLACE TRIGGER task_id_trigger
    BEFORE INSERT ON tasks
    FOR EACH ROW
BEGIN
    :NEW.id := task_seq.NEXTVAL;
END;
/

-- Create indexes for better performance
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_category ON tasks(category);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);
CREATE INDEX idx_tasks_parent ON tasks(parent_task_id);

-- Insert sample data
INSERT INTO tasks (title, description, due_date, priority, category, status) VALUES
('Complete project documentation', 'Write comprehensive documentation for the new project', SYSDATE + 7, 'HIGH', 'WORK', 'PENDING');

INSERT INTO tasks (title, description, due_date, priority, category, status) VALUES
('Grocery shopping', 'Buy vegetables, fruits, and dairy products', SYSDATE + 2, 'MEDIUM', 'PERSONAL', 'PENDING');

INSERT INTO tasks (title, description, priority, category, status, parent_task_id) VALUES
('Review database schema', 'Check all table relationships', 'MEDIUM', 'WORK', 'PENDING', 1);

COMMIT;