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
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE modules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    module_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
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



CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

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


CREATE TABLE accounts (
    accountId BIGINT NOT NULL AUTO_INCREMENT,
    accountName VARCHAR(255) NOT NULL,
    accountNumber VARCHAR(50) DEFAULT NULL,
    accountOwner VARCHAR(100) DEFAULT NULL,
    createdAt DATETIME(6) NOT NULL,
    updatedAt DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (accountId)
);

CREATE TABLE contacts (
    contactId BIGINT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(255) DEFAULT NULL,
    accountID BIGINT DEFAULT NULL,
    createdAt DATETIME(6) NOT NULL,
    updatedAt DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (contactId),
    FOREIGN KEY (accountID) REFERENCES accounts (accountId)
);

CREATE TABLE deals (
    dealId BIGINT NOT NULL AUTO_INCREMENT,
    dealName VARCHAR(255) NOT NULL,
    amount DECIMAL(15,2) DEFAULT NULL,
    closingDate DATE NOT NULL,
    probability INT NOT NULL,
    accountID BIGINT DEFAULT NULL,
    contactID BIGINT DEFAULT NULL,
    createdAt DATETIME(6) NOT NULL,
    updatedAt DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (dealId),
    FOREIGN KEY (accountID) REFERENCES accounts (accountId),
    FOREIGN KEY (contactID) REFERENCES contacts (contactId)
);

CREATE TABLE leads (
    id BIGINT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255) DEFAULT NULL,
    lastName VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    leadStatus VARCHAR(255) DEFAULT NULL,
    website VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tasks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    subject VARCHAR(255) DEFAULT NULL,
    dueDate DATETIME(6) DEFAULT NULL,
    status VARCHAR(255) DEFAULT NULL,
    taskOwner VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);


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

CREATE TABLE employee_attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(50),
    remarks VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    UNIQUE (employee_id, attendance_date)
);

CREATE TABLE employee_leave (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type VARCHAR(50) DEFAULT 'CASUAL',
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE TABLE email_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    subject VARCHAR(255),
    body LONGTEXT,
    created_by VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE record_access (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    access_level ENUM('VIEW', 'EDIT', 'DELETE'),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_record_access (user_id, entity_type, entity_id)
);



CREATE TABLE role_feature_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (feature_id) REFERENCES features(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id),
    UNIQUE KEY unique_role_feature_permission (role_id, feature_id, permission_id)
);

ALTER TABLE user_roles
DROP FOREIGN KEY user_roles_ibfk_1;


ALTER TABLE user_roles
ADD CONSTRAINT user_roles_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users(id) 
ON DELETE CASCADE;

CREATE TABLE permission_bundles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT
);

CREATE TABLE bundle_permissions (
    bundle_id BIGINT,
    permission_id BIGINT,
    FOREIGN KEY (bundle_id) REFERENCES permission_bundles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);


CREATE TABLE role_bundles (
    role_id BIGINT,
    bundle_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (bundle_id) REFERENCES permission_bundles(id)
);

CREATE TABLE record_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_type VARCHAR(50), -- e.g. 'lead', 'deal', 'contact'
    record_id BIGINT,
    user_id BIGINT,
    permission_type ENUM('view', 'edit', 'delete', 'share'),
    granted_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE field_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    field_name VARCHAR(255),
    role_id BIGINT,
    module_id BIGINT,
    feature_id BIGINT,
    action VARCHAR(50),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (module_id) REFERENCES modules(id),
    FOREIGN KEY (feature_id) REFERENCES features(id)
);

CREATE TABLE role_bundle_assignments (
    role_id BIGINT,
    bundle_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (bundle_id) REFERENCES permission_bundles(id),
    PRIMARY KEY (role_id, bundle_id)
);


CREATE TABLE fields (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    field_name VARCHAR(100) NOT NULL,
    module_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    data_type VARCHAR(50),
    label VARCHAR(100),
    is_required BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_fields_module FOREIGN KEY (module_id) REFERENCES modules(id),
    CONSTRAINT fk_fields_feature FOREIGN KEY (feature_id) REFERENCES features(id)
);


CREATE TABLE IF NOT EXISTS role_module_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    module_id BIGINT NOT NULL,
    feature_id BIGINT, -- optional: keep nullable for flexibility
    permission_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (module_id) REFERENCES modules(id),
    FOREIGN KEY (feature_id) REFERENCES features(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);


CREATE TABLE tenant_modules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(100),         -- Your schema or orgName
    module_id BIGINT NOT NULL,
    FOREIGN KEY (module_id) REFERENCES modules(id)
);


ALTER TABLE users ADD COLUMN country_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_user_country FOREIGN KEY (country_id) REFERENCES countries(id);


