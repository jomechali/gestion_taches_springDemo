-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE employee (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    userName VARCHAR(255)
)

-- changeset liquibase:2
ALTER TABLE employee RENAME COLUMN firstName TO first_name;
ALTER TABLE employee RENAME COLUMN lastName TO last_name;
ALTER TABLE employee RENAME COLUMN userName TO user_name;

-- changeset liquibase:3
CREATE TABLE task (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    description TINYTEXT,
    it DATETIME,
    rt INTEGER
);

CREATE TABLE employee_tasks (
    id_employee INTEGER NOT NULL,
    id_task INTEGER NOT NULL,
    PRIMARY KEY (id_employee, id_task),
    FOREIGN KEY (id_employee) REFERENCES employee(id),
    FOREIGN KEY (id_task) REFERENCES task(id)
)

-- changeset liquibase:4
ALTER TABLE employee_tasks RENAME COLUMN id_task TO task_id;
ALTER TABLE employee_tasks RENAME COLUMN id_employee TO employee_id;

-- changeset liquibase:5
ALTER TABLE employee_tasks RENAME COLUMN task_id TO tasks_id;

-- changeset liquibase:6
ALTER TABLE employee ADD COLUMN password VARCHAR(255);
ALTER TABLE employee ADD COLUMN enable BOOLEAN;

-- changeset liquibase:7
ALTER TABLE employee RENAME COLUMN enable TO enabled

-- changeset liquibase:8
CREATE TABLE role (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(200)
);

CREATE TABLE employee_role (
    employee_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (employee_id, role_id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

INSERT INTO role(label) VALUES ("ADMIN");
INSERT INTO role(label) VALUES ("USER");
INSERT INTO role(label) VALUES ("MANAGER");

-- changeset liquibase:9
CREATE TABLE comfirmation_token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    number INT,
    creation DATETIME,
    id_employee INT,
    FOREIGN KEY (id_employee) REFERENCES employee(id)
)

-- changeset liquibase:10
ALTER TABLE comfirmation_token RENAME confirmation_token;

-- changeset liquibase:11
ALTER TABLE confirmation_token MODIFY COLUMN number VARCHAR(255);

-- changeset liquibase:12
ALTER TABLE confirmation_token ADD activation DATETIME;

-- changeset liquibase:13
CREATE TABLE auth_data (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_employee INT,
    securityToken VARCHAR(255),
    FOREIGN KEY (id_employee) REFERENCES employee(id)
)

--changeset liquibase:14
ALTER TABLE auth_data RENAME COLUMN securityToken TO security_token;

--changeset liquibase:15
ALTER TABLE auth_data ADD authentication TINYTEXT;

