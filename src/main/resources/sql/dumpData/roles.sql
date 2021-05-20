CREATE TABLE mydatabase.roles (
                                     id BIGINT(20) auto_increment NOT NULL COMMENT 'id',
                                     name varchar(50) NOT NULL COMMENT 'username',
                                     CONSTRAINT roles_pk PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_vietnamese_ci
COMMENT='bảng thông tin phân quyền';