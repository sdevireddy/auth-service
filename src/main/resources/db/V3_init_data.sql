-- COUNTRIES
INSERT INTO countries (id, name) VALUES
(1,'India'),(2,'USA');

-- BRANCHES
INSERT INTO branches (id, name, country_id) VALUES
(1,'Bangalore HQ',1),(2,'New York HQ',2);

-- LOCATIONS
INSERT INTO locations (id, name, branch_id) VALUES
(1,'Bangalore‑Whitefield',1),(2,'NY‑Manhattan',2);

-- MODULES
INSERT INTO modules (id,name) VALUES
(1,'CRM'),(2,'HR'),(3,'Marketing');

-- FEATURES (under CRM)
INSERT INTO features (id,module_id,name) VALUES
(1,1,'Account Management'),
(2,1,'Contact Management'),
(3,1,'Deal Pipeline'),
(4,1,'Lead Qualification'),
(5,1,'Task Tracking');

-- PERMISSIONS
INSERT INTO permissions (id,name) VALUES
(1,'view'),(2,'create'),(3,'edit'),(4,'delete');

-- ROLES
INSERT INTO roles (id,name) VALUES
(1,'Admin'),
(2,'Sales Manager'),
(3,'Sales Executive'),
(4,'Marketing Manager'),
(5,'Marketing Executive');

-- ROLE‑MODULE PERMISSIONS (module‑level)
INSERT INTO role_module_permissions (role_id,module_id,permission_id) VALUES
-- Admin: full CRM
(1,1,1),(1,1,2),(1,1,3),(1,1,4),
-- Sales Manager: view/create/edit CRM
(2,1,1),(2,1,2),(2,1,3),
-- Sales Exec: view/create leads & deals
(3,1,1),(3,1,2);

-- ROLE‑FEATURE PERMISSIONS (feature‑level)
INSERT INTO role_feature_permissions (role_id,feature_id,permission_id) VALUES
-- Admin on all features
(1,1,1),(1,1,2),(1,1,3),(1,1,4),
(1,2,1),(1,2,2),(1,2,3),(1,2,4),
(1,3,1),(1,3,2),(1,3,3),(1,3,4),
(1,4,1),(1,4,2),(1,4,3),(1,4,4),
(1,5,1),(1,5,2),(1,5,3),(1,5,4),
-- Sales Manager limited feature access: Account & Deal
(2,1,1),(2,1,2),(2,1,3),(2,1,4),
(2,3,1),(2,3,2),(2,3,3),
-- Sales Executive: create & view leads/deals
(3,4,1),(3,4,2),(3,3,1),(3,3,2),
-- Marketing Manager: create & view contacts/leads
(4,2,1),(4,2,2),(4,4,1),(4,4,2),
-- Marketing Executive: view leads only
(5,4,1);

-- FIELDS (for field‑level permission tests)
INSERT INTO fields (id,field_name,module_id,feature_id,data_type,label,is_required) VALUES
(1,'account_name',1,1,'string','Account Name',TRUE),
(2,'contact_email',1,2,'string','Contact Email',TRUE),
(3,'deal_amount',1,3,'decimal','Deal Amount',TRUE),
(4,'lead_status',1,4,'string','Lead Status',TRUE),
(5,'task_subject',1,5,'string','Task Subject',TRUE);

-- FIELD PERMISSIONS (Admin has read/edit on all fields)
INSERT INTO field_permission (role_id,module_id,feature_id,field_name,action) VALUES
(1,1,1,'account_name','read'),
(1,1,1,'account_name','edit'),
(1,1,2,'contact_email','read'),
(1,1,2,'contact_email','edit'),
(1,1,3,'deal_amount','read'),
(1,1,3,'deal_amount','edit'),
(1,1,4,'lead_status','read'),
(1,1,4,'lead_status','edit'),
(1,1,5,'task_subject','read'),
(1,1,5,'task_subject','edit');

-- USERS
INSERT INTO users (id,username,email,password,first_name,last_name,created_at,is_active,branch_id,location_id,accountType,updated_at) VALUES
(1,'admin1','admin1@org.com','pwd','Admin','One',NOW(),1,1,1,'OTHER',NOW()),
(2,'smgr','smgr@org.com','pwd','Sales','Manager',NOW(),1,1,1,'OTHER',NOW()),
(3,'sexec','sexec@org.com','pwd','Sales','Exec',NOW(),1,1,1,'OTHER',NOW()),
(4,'mmgr','mmgr@org.com','pwd','Marketing','Manager',NOW(),1,2,2,'OTHER',NOW()),
(5,'mexec','mexec@org.com','pwd','Marketing','Exec',NOW(),1,2,2,'OTHER',NOW());

-- USER ROLES
INSERT INTO user_roles (user_id,role_id) VALUES
(1,1),(2,2),(3,3),(4,4),(5,5);

-- SAMPLE CRM DATA
INSERT INTO accounts (accountId,accountName,accountNumber,accountOwner,createdAt,updatedAt) VALUES
(1,'Acme Corp','A001','admin1',NOW(),NOW()),
(2,'Beta Inc','B002','smgr',NOW(),NOW());

INSERT INTO contacts (contactId,firstName,lastName,email,phone,accountID,createdAt,updatedAt) VALUES
(1,'John','Doe','jdoe@acme.com','111111',1,NOW(),NOW()),
(2,'Jane','Smith','jsmith@beta.com','222222',2,NOW(),NOW());

INSERT INTO leads (id,firstName,lastName,email,leadStatus,website) VALUES
(1,'Lead','Alpha','lead1@org.com','New','www.alpha.com'),
(2,'Lead','Beta','lead2@org.com','Qualified','www.beta.com');

INSERT INTO deals (dealId,dealName,amount,closingDate,probability,accountID,contactID,createdAt,updatedAt) VALUES
(1,'Deal One',5000.00,'2025-08-15',50,1,1,NOW(),NOW()),
(2,'Deal Two',12000.00,'2025-09-01',75,2,2,NOW(),NOW());

INSERT INTO tasks (id,subject,dueDate,status,taskOwner) VALUES
(1,'Call client','2025-08-05','Pending','smgr'),
(2,'Send proposal','2025-08-10','In Progress','sexec');

-- RECORD-LEVEL PERMISSIONS (Admin sees all, Sales Exec sees own leads)
INSERT INTO record_permissions (user_id,record_type,record_id,permission_type,granted_by,created_at) VALUES
(1,'Lead',1,'view',1,NOW()),
(1,'Deal',1,'view',1,NOW()),
(2,'Lead',1,'view',2,NOW()),
(3,'Lead',2,'view',3,NOW()),
(3,'Deal',2,'view',3,NOW());
