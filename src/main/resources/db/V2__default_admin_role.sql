
-- FEATURES for CRM (module_id = 1)
INSERT INTO features (id, module_id, name, description) VALUES
(1, 1, 'Account Management', 'Manage customer company accounts and related details.'),
(2, 1, 'Contact Management', 'Track individuals, their info, and communication history.'),
(3, 1, 'Deal Pipeline', 'Visual pipeline for tracking deals from lead to close.'),
(4, 1, 'Lead Management', 'Capture, assign, and nurture leads.'),
(5, 1, 'Task Tracking', 'Manage to-dos and follow-ups.'),
(6, 1, 'Notes & Activities', 'Add call logs, notes, meetings to contacts/deals.'),
(7, 1, 'Email Integration', 'Send, receive, and track emails directly from CRM.'),
(8, 1, 'CRM Reports & Dashboards', 'Visual insights on sales and customer engagement.');

-- FEATURES for HR (module_id = 2)
INSERT INTO features (id, module_id, name, description) VALUES
(9, 2, 'Employee Management', 'Manage employee profiles, designations, and status.'),
(10, 2, 'Leave Management', 'Submit, approve, and track employee leaves.'),
(11, 2, 'Attendance Tracking', 'Monitor check-in/out times and working hours.'),
(12, 2, 'Payroll Processing', 'Handle salary generation and payslip downloads.'),
(13, 2, 'Department Structure', 'Define and organize departments and reporting.'),
(14, 2, 'HR Reports', 'Get reports on employee performance, attendance, leaves.');

-- FEATURES for Campaigns (module_id = 3)
INSERT INTO features (id, module_id, name, description) VALUES
(15, 3, 'Email Campaigns', 'Design, schedule, and send marketing emails.'),
(16, 3, 'SMS Campaigns', 'Run SMS-based promotional campaigns.'),
(17, 3, 'Campaign Analytics', 'Track open rates, click rates, bounces, etc.'),
(18, 3, 'Contact Segmentation', 'Group audience based on criteria.'),
(19, 3, 'Campaign Automation', 'Setup auto-responses and drip campaigns.');

-- FEATURES for Learning (module_id = 4)
INSERT INTO features (id, module_id, name, description) VALUES
(20, 4, 'Course Management', 'Create, assign, and manage learning courses.'),
(21, 4, 'Learning Paths', 'Organize training material in logical sequence.'),
(22, 4, 'Quiz & Assessment', 'Add tests, quizzes, and track scores.'),
(23, 4, 'Progress Tracking', 'See individual learning progress.'),
(24, 4, 'Certificate Generation', 'Issue completion certificates.'),
(25, 4, 'Instructor Management', 'Manage trainers and course owners.');

-- FEATURES for Projects (module_id = 5)
INSERT INTO features (id, module_id, name, description) VALUES
(26, 5, 'Project Planning', 'Define project goals, timeline, milestones.'),
(27, 5, 'Task Assignment', 'Assign and track task progress.'),
(28, 5, 'Time Tracking', 'Log hours worked on projects/tasks.'),
(29, 5, 'Kanban Boards', 'Visual workflow using boards and stages.'),
(30, 5, 'File Sharing', 'Upload and share documents per project/task.'),
(31, 5, 'Project Reports', 'Generate performance, progress, and time reports.');

-- FEATURES for Support (module_id = 6)
INSERT INTO features (id, module_id, name, description) VALUES
(32, 6, 'Ticket Management', 'Log, assign, and resolve customer tickets.'),
(33, 6, 'SLA Tracking', 'Define and monitor SLA timelines.'),
(34, 6, 'Help Center', 'Provide knowledge base and FAQs.'),
(35, 6, 'Customer Feedback', 'Capture satisfaction and feedback ratings.'),
(36, 6, 'Agent Performance', 'Report on support agent efficiency.');
