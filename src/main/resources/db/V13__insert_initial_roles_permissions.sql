
-- Countries
INSERT INTO countries (name) VALUES 
('India'), ('USA'), ('UK'), ('Germany'), ('France'),
('Canada'), ('Australia'), ('Japan'), ('Singapore'), ('Brazil'),
('UAE'), ('Italy'), ('Spain'), ('Netherlands'), ('South Africa'),
('Mexico'), ('Russia'), ('China'), ('New Zealand'), ('Norway'),
('Sweden'), ('Switzerland'), ('Denmark'), ('Belgium'), ('Poland');

-- Branches
INSERT INTO branches (name, country_id) VALUES 
('Bangalore HQ', 1), ('Chennai Branch', 1), ('Hyderabad Office', 1), ('Delhi Office', 1), ('Mumbai Hub', 1),
('New York HQ', 2), ('San Francisco Branch', 2), ('Chicago Office', 2), ('Boston Office', 2), ('Texas Hub', 2),
('London HQ', 3), ('Berlin HQ', 4), ('Paris HQ', 5), ('Toronto Office', 6), ('Sydney HQ', 7),
('Tokyo HQ', 8), ('Singapore HQ', 9), ('Dubai HQ', 10), ('Rome Office', 11), ('Madrid Office', 12),
('Amsterdam HQ', 13), ('Cape Town Office', 14), ('Mexico City Office', 15), ('Moscow Office', 16), ('Beijing HQ', 17);

-- Locations
INSERT INTO locations (name, branch_id) VALUES 
('Bangalore-Whitefield', 1), ('Chennai-OMR', 2), ('Hyderabad-Hitech', 3), ('Delhi-CP', 4), ('Mumbai-BKC', 5),
('NY-Manhattan', 6), ('SF-Downtown', 7), ('Chicago-Loop', 8), ('Boston-Central', 9), ('Texas-Dallas', 10),
('London-City', 11), ('Berlin-Center', 12), ('Paris-Central', 13), ('Toronto-Yonge', 14), ('Sydney-CBD', 15),
('Tokyo-Shibuya', 16), ('Singapore-Orchard', 17), ('Dubai-DIFC', 18), ('Rome-Central', 19), ('Madrid-Center', 20),
('Amsterdam-Center', 21), ('Cape Town-CBD', 22), ('Mexico City-Zocalo', 23), ('Moscow-Central', 24), ('Beijing-CBD', 25);

-- Roles
INSERT INTO roles (name) VALUES 
('Admin'), ('Sales Manager'), ('Sales Executive'), ('HR Manager'), ('HR Executive'),
('Finance Manager'), ('Finance Executive'), ('IT Admin'), ('Marketing Manager'), ('Marketing Executive'),
('Operations Manager'), ('Customer Support'), ('Product Manager'), ('Developer'), ('QA Engineer'),
('Logistics'), ('Procurement'), ('Legal'), ('Strategy'), ('Trainer'),
('CEO'), ('CTO'), ('CFO'), ('COO'), ('Account Manager');


-- Accounts
INSERT INTO accounts (accountName, accountNumber, accountOwner, createdAt) VALUES 
('ABC Corp', 'ACC001', 'johnsmith', NOW()),
('XYZ Ltd', 'ACC002', 'sarahdoe', NOW()),
('Tech Solutions', 'ACC003', 'miketurner', NOW()),
('Global Ventures', 'ACC004', 'amypaul', NOW()),
('Enterprise Inc.', 'ACC005', 'roberthall', NOW());

-- Contacts
INSERT INTO contacts (firstName, lastName, email, phone, accountID, createdAt) VALUES 
('Mark', 'Johnson', 'mark.j@example.com', '1111111111', 1, NOW()),
('Emma', 'Watson', 'emma.w@example.com', '2222222222', 2, NOW()),
('Ryan', 'Gosling', 'ryan.g@example.com', '3333333333', 3, NOW()),
('Olivia', 'Brown', 'olivia.b@example.com', '4444444444', 4, NOW()),
('Chris', 'Evans', 'chris.e@example.com', '5555555555', 5, NOW());

-- Deals
INSERT INTO deals (dealName, amount, closingDate, probability, accountID, contactID, createdAt)
VALUES 
('Deal ABC', 10000, '2025-08-01', 75, 1, 1, NOW()),
('Deal XYZ', 20000, '2025-09-01', 60, 2, 2, NOW()),
('Tech Contract', 15000, '2025-10-01', 80, 3, 3, NOW()),
('Global Project', 25000, '2025-11-01', 70, 4, 4, NOW()),
('Enterprise Expansion', 30000, '2025-12-01', 90, 5, 5, NOW());

-- Leads
INSERT INTO leads (firstName, lastName, email, leadStatus, website) VALUES 
('Lead1', 'Alpha', 'lead1@example.com', 'New', 'http://lead1.com'),
('Lead2', 'Beta', 'lead2@example.com', 'Contacted', 'http://lead2.com'),
('Lead3', 'Gamma', 'lead3@example.com', 'Qualified', 'http://lead3.com'),
('Lead4', 'Delta', 'lead4@example.com', 'Proposal Sent', 'http://lead4.com'),
('Lead5', 'Epsilon', 'lead5@example.com', 'Closed', 'http://lead5.com');

-- Tasks
INSERT INTO tasks (subject, dueDate, status, taskOwner) VALUES 
('Follow up with client', '2025-08-10', 'Pending', 'johnsmith'),
('Prepare proposal', '2025-08-15', 'In Progress', 'sarahdoe'),
('Review contract', '2025-08-20', 'Completed', 'miketurner'),
('Schedule meeting', '2025-08-25', 'Pending', 'amypaul'),
('Send Invoice', '2025-08-30', 'Pending', 'roberthall');

-- Employees
INSERT INTO employees (employee_code, first_name, last_name, gender, employment_status, employment_type) VALUES 
('EMP001', 'John', 'Smith', 'Male', 'Active', 'Full-time'),
('EMP002', 'Sarah', 'Doe', 'Female', 'Active', 'Full-time'),
('EMP003', 'Mike', 'Turner', 'Male', 'Active', 'Full-time'),
('EMP004', 'Amy', 'Paul', 'Female', 'Active', 'Full-time'),
('EMP005', 'Robert', 'Hall', 'Male', 'Active', 'Full-time');

-- Attendance
INSERT INTO employee_attendance (employee_id, attendance_date, check_in_time, check_out_time, status) VALUES 
(1, '2025-07-01', '09:00:00', '18:00:00', 'Present'),
(2, '2025-07-01', '09:00:00', '18:00:00', 'Present'),
(3, '2025-07-01', '09:00:00', '18:00:00', 'Present'),
(4, '2025-07-01', '09:00:00', '18:00:00', 'Present'),
(5, '2025-07-01', '09:00:00', '18:00:00', 'Present');

-- Leaves
INSERT INTO employee_leave (employee_id, leave_type, from_date, to_date, reason, status) VALUES 
(1, 'SICK', '2025-07-05', '2025-07-07', 'Fever', 'APPROVED'),
(2, 'CASUAL', '2025-07-10', '2025-07-11', 'Personal Work', 'APPROVED'),
(3, 'SICK', '2025-07-15', '2025-07-16', 'Medical', 'APPROVED'),
(4, 'CASUAL', '2025-07-20', '2025-07-21', 'Family Event', 'PENDING'),
(5, 'CASUAL', '2025-07-25', '2025-07-26', 'Vacation', 'PENDING');


-- MODULES
INSERT INTO modules (id, name) VALUES
(1, 'Leads'), (2, 'Contacts'), (3, 'Deals'), (4, 'Accounts'), (5, 'Tasks'),
(6, 'Employees'), (7, 'Attendance'), (8, 'Leaves'), (9, 'Templates'), (10, 'Users');

-- FEATURES
INSERT INTO features (id, module_id, name) VALUES
(1, 1, 'Lead View'), (2, 1, 'Lead Create'), (3, 1, 'Lead Edit'), (4, 1, 'Lead Delete'),
(5, 2, 'Contact View'), (6, 2, 'Contact Create'), (7, 2, 'Contact Edit'), (8, 2, 'Contact Delete'),
(9, 3, 'Deal View'), (10, 3, 'Deal Create'), (11, 3, 'Deal Edit'), (12, 3, 'Deal Delete'),
(13, 4, 'Account View'), (14, 4, 'Account Create'),
(15, 5, 'Task View'), (16, 5, 'Task Update');

-- PERMISSIONS
INSERT INTO permissions (id, name) VALUES
(1, 'VIEW'), (2, 'CREATE'), (3, 'EDIT'), (4, 'DELETE');

-- ROLE-MODULE-PERMISSIONS
INSERT INTO role_module_permissions (role_id, module_id, permission_id) VALUES
(1, 1, 1), (1, 1, 2), (1, 1, 3), (1, 1, 4),  -- Admin - Leads Full
(2, 2, 1), (2, 2, 2), (2, 2, 3),             -- Sales Manager - Contacts
(3, 3, 1), (3, 3, 2);                        -- Sales Exec - Deals

-- ROLE-FEATURE-PERMISSIONS
INSERT INTO role_feature_permissions (role_id, feature_id, permission_id) VALUES
(1, 1, 1), (1, 2, 2), (1, 3, 3), (1, 4, 4),     -- Admin - Leads All
(2, 5, 1), (2, 6, 2), (2, 7, 3),               -- Sales Manager - Contacts
(3, 9, 1), (3, 10, 2);                         -- Sales Exec - Deals

-- PERMISSION BUNDLES
INSERT INTO permission_bundles (id, name, description) VALUES
(1, 'Basic Lead Access', 'View and create leads'),
(2, 'Full Deal Access', 'All permissions on deals'),
(3, 'HR Admin', 'All permissions for HR modules');

-- BUNDLE-PERMISSIONS
INSERT INTO bundle_permissions (bundle_id, permission_id) VALUES
(1, 1), (1, 2),     -- Basic Lead Access: VIEW, CREATE
(2, 1), (2, 2), (2, 3), (2, 4),  -- Full Deal Access: all
(3, 1), (3, 3), (3, 4);  -- HR Admin: VIEW, EDIT, DELETE

-- ROLE-BUNDLE-ASSIGNMENTS
INSERT INTO role_bundle_assignments (role_id, bundle_id) VALUES
(1, 1), (1, 2), (4, 3);  -- Admin gets 1,2; HR Manager gets HR bundle

-- FIELD-LEVEL PERMISSIONS
INSERT INTO field_permission (field_name, role_id, module_id, feature_id, action) VALUES
('email', 1, 1, 1, 'view'),
('phone', 1, 1, 1, 'view'),
('leadStatus', 2, 1, 3, 'edit'),
('amount', 3, 3, 9, 'edit');

-- USERS
INSERT INTO users (username, email, password, first_name, last_name, phone_number, created_at, is_active, branch_id, location_id, country_id)
VALUES 
('johnsmith', 'john.smith@example.com', 'hashedpwd1', 'John', 'Smith', '1234567890', NOW(), 1, 1, 1, 1),
('sarahdoe', 'sarah.doe@example.com', 'hashedpwd2', 'Sarah', 'Doe', '1234567891', NOW(), 1, 2, 2, 1),
('miketurner', 'mike.turner@example.com', 'hashedpwd3', 'Mike', 'Turner', '1234567892', NOW(), 1, 3, 3, 2),
('amypaul', 'amy.paul@example.com', 'hashedpwd4', 'Amy', 'Paul', '1234567893', NOW(), 1, 4, 4, 3),
('roberthall', 'robert.hall@example.com', 'hashedpwd5', 'Robert', 'Hall', '1234567894', NOW(), 1, 5, 5, 4);


-- RECORD ACCESS
INSERT INTO record_access (user_id, entity_type, entity_id, access_level) VALUES
(1, 'deal', 1, 'VIEW'),
(1, 'lead', 1, 'EDIT'),
(2, 'lead', 2, 'VIEW'),
(3, 'contact', 3, 'DELETE');

-- USER_ROLE_BRANCH_LOCATION
INSERT INTO user_role_branch_location (user_id, role_id, branch_id, location_id) VALUES
(1, 1, 1, 1),
(2, 2, 2, 2),
(3, 3, 3, 3),
(4, 4, 4, 4),
(5, 5, 5, 5);

-- USER_BRANCHES
INSERT INTO user_branches (user_id, branch_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

-- USER_LOCATIONS
INSERT INTO user_locations (user_id, location_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

-- RECORD PERMISSIONS
INSERT INTO record_permissions (record_type, record_id, user_id, permission_type, granted_by) VALUES
('lead', 1, 1, 'view', 2),
('deal', 1, 1, 'edit', 2),
('contact', 3, 3, 'delete', 1);


-- Sample fields for Leads feature under CRM module
INSERT INTO fields (field_name, module_id, feature_id, data_type, label, is_required) VALUES
('lead_name', 1, 1, 'String', 'Lead Name', true),
('email', 1, 1, 'String', 'Email Address', false),
('phone', 1, 1, 'String', 'Phone Number', false),
('source', 1, 1, 'Dropdown', 'Lead Source', false);

-- Sample fields for Contacts feature under CRM module
INSERT INTO fields (field_name, module_id, feature_id, data_type, label, is_required) VALUES
('first_name', 1, 2, 'String', 'First Name', true),
('last_name', 1, 2, 'String', 'Last Name', true),
('email', 1, 2, 'String', 'Email Address', false),
('mobile', 1, 2, 'String', 'Mobile Number', false);



-- EMAIL TEMPLATES
INSERT INTO email_templates (name, subject, body, created_by, created_at)
VALUES 
('Welcome Email', 'Welcome to Our CRM', 'Dear {{name}}, welcome aboard!', 'admin', NOW()),
('Password Reset', 'Reset Your Password', 'Click here to reset your password.', 'admin', NOW()),
('Task Reminder', 'Pending Task Alert', 'You have a task due today.', 'admin', NOW());


-- USER ROLES
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- Admin
(2, 2), -- Sales Manager
(3, 3), -- Sales Executive
(4, 4), -- HR Manager
(5, 5); -- HR Executive

