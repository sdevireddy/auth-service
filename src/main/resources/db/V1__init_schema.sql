-- V1__init_schema.sql

-- Table: accounts
-- ACCOUNTS
DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts (
    accountId BIGINT NOT NULL AUTO_INCREMENT,
    accountName VARCHAR(255) NOT NULL,
    accountNumber VARCHAR(50) DEFAULT NULL,
    accountOwner VARCHAR(100) DEFAULT NULL,
    accountSite VARCHAR(255) DEFAULT NULL,
    accountType ENUM('CUSTOMER','OTHER','PARTNER','PROSPECT','VENDOR') DEFAULT NULL,
    annualRevenue DECIMAL(15,2) DEFAULT NULL,
    createdAt DATETIME(6) NOT NULL,
    fax VARCHAR(255) DEFAULT NULL,
    industry VARCHAR(255) DEFAULT NULL,
    ownership ENUM('OTHER','PRIVATE','PUBLIC','SUBSIDIARY') DEFAULT NULL,
    phone VARCHAR(255) DEFAULT NULL,
    rating ENUM('COLD','HOT','WARM') DEFAULT NULL,
    sicCode VARCHAR(50) DEFAULT NULL,
    tickerSymbol VARCHAR(255) DEFAULT NULL,
    updatedAt DATETIME(6) DEFAULT NULL,
    website VARCHAR(255) DEFAULT NULL,
    parent_account_id BIGINT DEFAULT NULL,
    PRIMARY KEY (accountId),
    UNIQUE KEY UK_account_name (accountName),
    UNIQUE KEY UK_account_number (accountNumber),
    KEY idx_account_name (accountName),
    CONSTRAINT fk_account_parent FOREIGN KEY (parent_account_id) REFERENCES accounts (accountId)
);
INSERT INTO accounts VALUES (1,'Zoho Inc.',NULL,NULL,NULL,NULL,0.00,'2025-03-14 09:19:34.274000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-03-14 09:19:34.274000',NULL,NULL);


-- CONTACTS
DROP TABLE IF EXISTS contacts;
CREATE TABLE contacts (
    contactId BIGINT NOT NULL AUTO_INCREMENT,
    assistant VARCHAR(255) DEFAULT NULL,
    asstPhone VARCHAR(255) DEFAULT NULL,
    contactOwner VARCHAR(255) DEFAULT NULL,
    createdAt DATETIME(6) NOT NULL,
    dateOfBirth DATE DEFAULT NULL,
    department VARCHAR(255) DEFAULT NULL,
    email VARCHAR(100) NOT NULL,
    emailOptOut BIT(1) DEFAULT NULL,
    firstName VARCHAR(50) NOT NULL,
    homePhone VARCHAR(255) DEFAULT NULL,
    lastName VARCHAR(50) NOT NULL,
    leadSource ENUM('ADVERTISEMENT','OTHER','REFERRAL','SOCIAL_MEDIA','WEBSITE') DEFAULT NULL,
    mobile VARCHAR(255) DEFAULT NULL,
    otherPhone VARCHAR(255) DEFAULT NULL,
    phone VARCHAR(255) DEFAULT NULL,
    secondaryEmail VARCHAR(255) DEFAULT NULL,
    skypeId VARCHAR(255) DEFAULT NULL,
    title VARCHAR(255) DEFAULT NULL,
    twitterHandle VARCHAR(255) DEFAULT NULL,
    updatedAt DATETIME(6) DEFAULT NULL,
    vendorName VARCHAR(255) DEFAULT NULL,
    accountID BIGINT DEFAULT NULL,
    reporting_to BIGINT DEFAULT NULL,
    PRIMARY KEY (contactId),
    UNIQUE KEY UK_email (email),
    KEY idx_email (email),
    CONSTRAINT fk_contact_account FOREIGN KEY (accountID) REFERENCES accounts (accountId),
    CONSTRAINT fk_contact_reporting_to FOREIGN KEY (reporting_to) REFERENCES contacts (contactId)
);
INSERT INTO contacts VALUES (1,NULL,NULL,NULL,'2025-03-14 09:19:34.287000',NULL,NULL,'john.doe@example.comm13',b'0','John',NULL,'Doe','OTHER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-03-14 09:19:34.287000',NULL,1,NULL);


-- DEALS
DROP TABLE IF EXISTS deals;
CREATE TABLE deals (
    dealId BIGINT NOT NULL AUTO_INCREMENT,
    amount DECIMAL(15,2) DEFAULT NULL,
    campaignSource VARCHAR(255) DEFAULT NULL,
    closingDate DATE NOT NULL,
    createdAt DATETIME(6) NOT NULL,
    dealName VARCHAR(255) NOT NULL,
    dealOwner VARCHAR(100) DEFAULT NULL,
    expectedRevenue DECIMAL(15,2) DEFAULT NULL,
    leadSource ENUM('ADVERTISEMENT','OTHER','REFERRAL','SOCIAL_MEDIA','WEBSITE') DEFAULT NULL,
    nextStep VARCHAR(255) DEFAULT NULL,
    probability INT NOT NULL,
    qualification VARCHAR(255) DEFAULT NULL,
    stage ENUM('CLOSED_LOST','CLOSED_WON','NEGOTIATION','PROPOSAL','PROSPECTING','QUALIFICATION') DEFAULT NULL,
    type ENUM('EXISTING_BUSINESS','NEW','NEW_BUSINESS','OTHER','RENEWAL') DEFAULT NULL,
    updatedAt DATETIME(6) DEFAULT NULL,
    accountID BIGINT DEFAULT NULL,
    contactID BIGINT DEFAULT NULL,
    PRIMARY KEY (dealId),
    UNIQUE KEY UK_deal_name (dealName),
    KEY idx_deal_name (dealName),
    CONSTRAINT fk_deal_account FOREIGN KEY (accountID) REFERENCES accounts (accountId),
    CONSTRAINT fk_deal_contact FOREIGN KEY (contactID) REFERENCES contacts (contactId)
);
INSERT INTO deals VALUES (1,0.00,NULL,'2025-03-14','2025-03-14 09:19:34.295000','Zoho Inc. Deal',NULL,NULL,'OTHER',NULL,50,NULL,'PROSPECTING','NEW','2025-03-14 09:19:34.295000',1,1);


-- LEADS (UNCHANGED)
DROP TABLE IF EXISTS leads;
CREATE TABLE leads (
    id BIGINT NOT NULL AUTO_INCREMENT,
    annualRevenue DOUBLE DEFAULT NULL,
    company VARCHAR(255) DEFAULT NULL,
    converted BIT(1) DEFAULT NULL,
    description VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    emailOptOut BIT(1) DEFAULT NULL,
    fax VARCHAR(255) DEFAULT NULL,
    firstName VARCHAR(255) DEFAULT NULL,
    industry VARCHAR(255) DEFAULT NULL,
    lastName VARCHAR(255) DEFAULT NULL,
    leadOwner VARCHAR(255) DEFAULT NULL,
    leadSource TINYINT DEFAULT NULL,
    leadStatus VARCHAR(255) DEFAULT NULL,
    mobile VARCHAR(255) DEFAULT NULL,
    noOfEmployees INT DEFAULT NULL,
    rating VARCHAR(255) DEFAULT NULL,
    secondaryEmail VARCHAR(255) DEFAULT NULL,
    skypeId VARCHAR(255) DEFAULT NULL,
    title VARCHAR(255) DEFAULT NULL,
    twitter VARCHAR(255) DEFAULT NULL,
    website VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT leads_chk_1 CHECK ((leadSource BETWEEN 0 AND 4))
);
INSERT INTO leads VALUES (1,NULL,'Zoho Inc.',b'1',NULL,'john.doe@example.comm13',NULL,NULL,'John',NULL,'Doe',NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'Sales Manager3',NULL,NULL);


-- TASKS (UNCHANGED)
DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    associatedWith ENUM('ACCOUNT','DEAL','INVOICE','PRODUCT','PURCHASE_ORDER','QUOTE','SALES_ORDER') DEFAULT NULL,
    dueDate DATETIME(6) DEFAULT NULL,
    priority VARCHAR(255) DEFAULT NULL,
    relatedTo ENUM('ACCOUNT','DEAL','INVOICE','PRODUCT','PURCHASE_ORDER','QUOTE','SALES_ORDER') DEFAULT NULL,
    reminder BIT(1) DEFAULT NULL,
    repeatTask BIT(1) DEFAULT NULL,
    status VARCHAR(255) DEFAULT NULL,
    subject VARCHAR(255) DEFAULT NULL,
    taskOwner VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);


DROP TABLE IF EXISTS users;
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) DEFAULT NULL,
    `first_name` VARCHAR(100) DEFAULT NULL,
    `last_name` VARCHAR(100) DEFAULT NULL,
    `phone_number` VARCHAR(50) DEFAULT NULL,
    `address` VARCHAR(255) DEFAULT NULL,
    `date_of_birth` DATE DEFAULT NULL,
    `gender` VARCHAR(20) DEFAULT NULL,
    `language_preference` VARCHAR(10) DEFAULT NULL,
    `timezone` VARCHAR(50) DEFAULT NULL,
    `department` VARCHAR(100) DEFAULT NULL,
    `job_title` VARCHAR(100) DEFAULT NULL,
    `manager_id` BIGINT DEFAULT NULL,
    `profile_picture_url` VARCHAR(512) DEFAULT NULL,
    `account_locked` BIT(1) DEFAULT NULL,
    `failed_login_attempts` INT DEFAULT NULL,
    `firstlogin` BIT(1) DEFAULT NULL,
    `is_active` BIT(1) DEFAULT NULL,
    `two_factor_enabled` BIT(1) DEFAULT NULL,
    `last_login` DATETIME(6) DEFAULT NULL,
    `last_password_change` DATETIME(6) DEFAULT NULL, accountType ENUM('CUSTOMER','OTHER','PARTNER','PROSPECT','VENDOR'),
    `created_at` DATETIME(6) NOT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `security_question` VARCHAR(255) DEFAULT NULL,
    `security_answer_hash` VARCHAR(255) DEFAULT NULL,
    `branch_id` BIGINT DEFAULT NULL,
    `location_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_username` (`username`),
    UNIQUE KEY `UK_email` (`email`),
    CONSTRAINT `fk_user_branch` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
    CONSTRAINT `fk_user_location` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_code VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci UNIQUE NOT NULL,

    -- Personal Details
    first_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    last_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    date_of_birth DATE,
    gender VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    nationality VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    marital_status VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    blood_group VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    -- Contact Details
    personal_email VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    official_email VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    personal_phone VARCHAR(20),
    official_phone VARCHAR(20),
    current_address VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    permanent_address VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    city VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    state VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    country VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    pincode VARCHAR(20),

    -- Work Details
    department VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    designation VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    reporting_manager VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    work_location VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    date_of_joining DATE,
    date_of_confirmation DATE,
    date_of_resignation DATE,
    employment_status ENUM('Active', 'Resigned', 'On Leave', 'Terminated') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    employment_type ENUM('Full-time', 'Part-time', 'Contractual', 'Intern') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    -- Official IDs
    pan_number VARCHAR(50),
    aadhar_number VARCHAR(50),
    passport_number VARCHAR(50),
    driving_license_number VARCHAR(50),
    tax_identification_number VARCHAR(50),
    social_security_number VARCHAR(50),

    -- Financial Details
    bank_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    bank_account_number VARCHAR(50),
    ifsc_code VARCHAR(50),
    uan_number VARCHAR(50),
    esic_number VARCHAR(50),
    salary_structure_id VARCHAR(50),

    -- Emergency Contact
    emergency_contact_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    emergency_contact_relationship VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    emergency_contact_phone VARCHAR(20),
    emergency_contact_address VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    -- Skills / Hobbies / Experience
    skills TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    certifications TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    hobbies TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    languages_known TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    total_experience_years INT,
    previous_employers TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    -- Documents
    resume_file VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    offer_letter_file VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    id_proof_file VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    -- Audit Columns
    created_by VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    updated_by VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE countries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE branches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country_id BIGINT,
    FOREIGN KEY (country_id) REFERENCES countries(id)
);

CREATE TABLE locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    branch_id BIGINT,
    FOREIGN KEY (branch_id) REFERENCES branches(id)
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE modules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);


CREATE TABLE features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    module_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (module_id) REFERENCES modules(id)
);


CREATE TABLE role_module_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT,
    module_id BIGINT,
    permission_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (module_id) REFERENCES modules(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id),
    UNIQUE (role_id, module_id, permission_id)
);


CREATE TABLE email_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    subject VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    body LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    created_by VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE employee_attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    remarks VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees(id),
    CONSTRAINT unique_attendance UNIQUE (employee_id, attendance_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE employee_leave (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type VARCHAR(50) DEFAULT 'CASUAL',
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_leave_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE user_branches (
    user_id BIGINT,
    branch_id BIGINT,
    PRIMARY KEY (user_id, branch_id)
);

CREATE TABLE user_locations (
    user_id BIGINT,
    location_id BIGINT,
    PRIMARY KEY (user_id, location_id)
);

CREATE TABLE role_feature_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (feature_id) REFERENCES features(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id),
    UNIQUE (role_id, feature_id, permission_id)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE user_role_branch_location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (branch_id) REFERENCES branches(id),
    FOREIGN KEY (location_id) REFERENCES locations(id),
    UNIQUE KEY unique_user_role_branch_location (user_id, role_id, branch_id, location_id)
);


ALTER TABLE users ADD role_id BIGINT;
ALTER TABLE users ADD country_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id);
ALTER TABLE users ADD CONSTRAINT fk_user_country FOREIGN KEY (country_id) REFERENCES countries(id);

