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
    status ENUM('PRESENT', 'ABSENT', 'LEAVE') DEFAULT 'PRESENT',
    remarks VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
	

CREATE TABLE employee_leave (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type ENUM(
        'CASUAL',
        'SICK',
        'ANNUAL',
        'MATERNITY',
        'PATERNITY',
        'COMP_OFF',
        'UNPAID',
        'EARNED',
        'BEREAVEMENT',
        'MARRIAGE',
        'STUDY',
        'RELOCATION'
    ) DEFAULT 'CASUAL',
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_leave_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

