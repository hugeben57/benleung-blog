-- benleung-blog 首次启动初始化(仅空数据卷时执行一次)
CREATE DATABASE IF NOT EXISTS benblog
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE benblog;

CREATE TABLE IF NOT EXISTS blog (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    title          VARCHAR(50)  NOT NULL,
    content        VARCHAR(255) NOT NULL,
    create_time    DATETIME     NOT NULL,
    update_time    DATETIME     NOT NULL,
    description    TEXT,
    published      TINYINT      NOT NULL DEFAULT 0,
    type           VARCHAR(10)  NOT NULL,
    published_time DATETIME     NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user` (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role     TINYINT     NOT NULL DEFAULT 0
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS picture (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    url          VARCHAR(255) NOT NULL,
    picture_name VARCHAR(100) NOT NULL,
    is_cover     TINYINT      NOT NULL DEFAULT 0
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS music (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    url        VARCHAR(255) NOT NULL,
    music_name VARCHAR(50)  NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 初始管理员(role=1),登录时密码明文比对,如需修改请同步改这里
INSERT INTO `user` (username, password, role)
VALUES ('admin', '123456', 1);
