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


-- USERS (UNCHANGED)
DROP TABLE IF EXISTS users;
CREATE TABLE `users` (
    `account_locked` bit(1) DEFAULT NULL,
    `date_of_birth` date DEFAULT NULL,
    `failed_login_attempts` int DEFAULT NULL,
    `firstlogin` bit(1) DEFAULT NULL,
    `is_active` bit(1) DEFAULT NULL,
    `two_factor_enabled` bit(1) DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `last_login` datetime(6) DEFAULT NULL,
    `last_password_change` datetime(6) DEFAULT NULL,
    `manager_id` bigint DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `language_preference` varchar(10) DEFAULT NULL,
    `gender` varchar(20) DEFAULT NULL,
    `phone_number` varchar(50) DEFAULT NULL,
    `timezone` varchar(50) DEFAULT NULL,
    `department` varchar(100) DEFAULT NULL,
    `first_name` varchar(100) DEFAULT NULL,
    `job_title` varchar(100) DEFAULT NULL,
    `last_name` varchar(100) DEFAULT NULL,
    `username` varchar(100) NOT NULL,
    `profile_picture_url` varchar(512) DEFAULT NULL,
    `address` varchar(255) DEFAULT NULL,
    `email` varchar(255) NOT NULL,
    `password` varchar(255) DEFAULT NULL,
    `security_answer_hash` varchar(255) DEFAULT NULL,
    `security_question` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
    UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE email_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    subject VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    body LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    created_by VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employeeCode VARCHAR(50),
    firstName VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    lastName VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    dateOfBirth DATE,
    gender VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    nationality VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    maritalStatus VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    bloodGroup VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    personalEmail VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    officialEmail VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    personalPhone VARCHAR(50),
    officialPhone VARCHAR(50),

    currentAddress VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    permanentAddress VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    city VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    state VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    country VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    pincode VARCHAR(20),

    department VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    designation VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    reportingManager VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    workLocation VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    dateOfJoining DATE,
    dateOfConfirmation DATE,
    dateOfResignation DATE,
    employmentStatus VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    employmentType VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    panNumber VARCHAR(50),
    aadharNumber VARCHAR(50),
    passportNumber VARCHAR(50),
    drivingLicenseNumber VARCHAR(50),
    taxIdentificationNumber VARCHAR(50),
    socialSecurityNumber VARCHAR(50),

    bankName VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    bankAccountNumber VARCHAR(50),
    ifscCode VARCHAR(50),
    uanNumber VARCHAR(50),
    esicNumber VARCHAR(50),
    salaryStructureId VARCHAR(50),

    emergencyContactName VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    emergencyContactRelationship VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    emergencyContactPhone VARCHAR(50),
    emergencyContactAddress VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,

    skills VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    certifications VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    hobbies VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    languagesKnown VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    totalExperienceYears INT,
    previousEmployers VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci
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
