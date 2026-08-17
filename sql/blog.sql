create table if not exists blog(
    id int primary key auto_increment,
    title varchar(50) not null,
    content varchar(255) not null,
    create_time datetime not null,
    update_time datetime not null,
    description text,
    published tinyint not null default 0,
    type varchar(10) not null
)

create table if not exists `user`(
    id int primary key auto_increment,
    username varchar(50) not null unique,
    password varchar(50) not null,
    role tinyint not null default 0
)

alter table blog add column published_time datetime not null;

-- 初始管理员（role=1），需要管理员登录时取消注释执行一次：
-- insert into `user`(user_name, password, role) values('admin', '123456', 1);