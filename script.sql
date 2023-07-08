
    create table categories (
       id varchar(255) not null,
        created_at datetime not null,
        name varchar(255) not null,
        created_by_fk varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table departments (
       id varchar(255) not null,
        created_at datetime not null,
        name varchar(255) not null,
        created_by_fk varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table departments_categories (
       department_fk varchar(255) not null,
        category_fk varchar(255) not null,
        primary key (department_fk, category_fk)
    ) engine=InnoDB;

    create table departments_users (
       department_fk varchar(255) not null,
        user_fk varchar(255) not null,
        primary key (department_fk, user_fk)
    ) engine=InnoDB;

    create table tickets (
       id varchar(255) not null,
        created_at datetime not null,
        description varchar(5000),
        estimation integer,
        priority varchar(10),
        reference varchar(255) not null,
        resolved_on datetime,
        started_on datetime,
        status varchar(15),
        title varchar(255) not null,
        created_by_fk varchar(255),
        assigned_to_fk varchar(255),
        category_fk varchar(255),
        department_fk varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users (
       id varchar(255) not null,
        created_at datetime not null,
        email varchar(255),
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    alter table tickets 
       add constraint UK_mq50l7q5c3773jsrnrsubj92r unique (reference);

    alter table users 
       add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table categories 
       add constraint FKf5r5ep9m89fj4k3pf3cvj07hu 
       foreign key (created_by_fk) 
       references users (id);

    alter table departments 
       add constraint FKrldto71abqgdnvab8dt29t344 
       foreign key (created_by_fk) 
       references users (id);

    alter table departments_categories 
       add constraint FKao61uc1ly8bnk2biv14td3aas 
       foreign key (category_fk) 
       references categories (id);

    alter table departments_categories 
       add constraint FKsr810b9rkan75mtxij9ywg4nk 
       foreign key (department_fk) 
       references departments (id);

    alter table departments_users 
       add constraint FKf6hwmt2u2crvsnptaml0sa3ex 
       foreign key (user_fk) 
       references users (id);

    alter table departments_users 
       add constraint FKahhx4ncnifbnpsy4m0hwguxq2 
       foreign key (department_fk) 
       references departments (id);

    alter table tickets 
       add constraint FK40nppirhiy263blhikdxn23un 
       foreign key (created_by_fk) 
       references users (id);

    alter table tickets 
       add constraint FK1itcllmsltu62n8e8n7sda0np 
       foreign key (assigned_to_fk) 
       references users (id);

    alter table tickets 
       add constraint FKtnoh7w16tqwvc29uoxuax5ovc 
       foreign key (category_fk) 
       references categories (id);

    alter table tickets 
       add constraint FKr1vcn5jfsppyv3y729gp5wvyc 
       foreign key (department_fk) 
       references departments (id);
