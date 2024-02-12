INSERT INTO users (id,created_at,email,name) VALUES
	 ('9c345098-0d0c-419c-bcdf-05c0810f295f','2023-02-08 23:02:39','1675893759024-gustavo@email.com','User2 test'),
	 ('c97d8449-3600-4834-92ad-3bf77b98d923','2023-02-08 23:01:09','1675893669342-flavios@email.com','User test');

INSERT INTO categories (id,created_at,name,created_by_fk) VALUES
	 ('468a89e2-acce-40b6-b356-2c134ba48f5e','2023-02-08 23:02:39','Compras','9c345098-0d0c-419c-bcdf-05c0810f295f'),
	 ('67344fa9-9c58-4d4e-a9e6-51599f315655','2023-02-08 23:01:09','Suporte','c97d8449-3600-4834-92ad-3bf77b98d923');

INSERT INTO departments (id,created_at,name,created_by_fk) VALUES
	 ('51f63edd-4e99-45b9-922e-fc922cf0e05f','2023-02-08 23:01:09','Informatique','c97d8449-3600-4834-92ad-3bf77b98d923'),
	 ('b7f6aeae-8ad0-49c1-9819-12d35dcdc2ab','2023-02-08 23:02:39','Secretaria','9c345098-0d0c-419c-bcdf-05c0810f295f');

INSERT INTO departments_categories (department_fk,category_fk) VALUES
	 ('b7f6aeae-8ad0-49c1-9819-12d35dcdc2ab','468a89e2-acce-40b6-b356-2c134ba48f5e'),
	 ('51f63edd-4e99-45b9-922e-fc922cf0e05f','67344fa9-9c58-4d4e-a9e6-51599f315655');

INSERT INTO departments_users (department_fk,user_fk) VALUES
	 ('b7f6aeae-8ad0-49c1-9819-12d35dcdc2ab','9c345098-0d0c-419c-bcdf-05c0810f295f'),
	 ('51f63edd-4e99-45b9-922e-fc922cf0e05f','c97d8449-3600-4834-92ad-3bf77b98d923');

INSERT INTO tickets (id,created_at,description,estimation,priority,reference,resolved_on,started_on,status,title,assigned_to_fk,category_fk,created_by_fk,department_fk) VALUES
	 ('082722c7-856f-4a39-b8dd-20cb08a6996c','2023-02-08 23:01:09','Descrição da tarefa',3,'HIGH','00kOTS9IZe',NULL,'2023-02-08 23:01:09','OPEN','My task title','c97d8449-3600-4834-92ad-3bf77b98d923','67344fa9-9c58-4d4e-a9e6-51599f315655','c97d8449-3600-4834-92ad-3bf77b98d923','51f63edd-4e99-45b9-922e-fc922cf0e05f'),
	 ('5365151c-edb9-47b0-954c-412d8f2c7161','2023-02-08 23:02:39','Descrição da tarefa',10,'HIGH','KWB0G6hlrA',NULL,'2023-02-08 23:02:39','OPEN','My task title 2','9c345098-0d0c-419c-bcdf-05c0810f295f','468a89e2-acce-40b6-b356-2c134ba48f5e','9c345098-0d0c-419c-bcdf-05c0810f295f','b7f6aeae-8ad0-49c1-9819-12d35dcdc2ab');