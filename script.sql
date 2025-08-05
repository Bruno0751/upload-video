create database db_upload_video CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

create table db_upload_video.id_video (
	id_video INT auto_increment not null,
	name varchar(60) not null,
	m timestamp default current_timestamp on update current_timestamp not null,
	length INT not null,
	CONSTRAINT pk_id_video PRIMARY KEY(id_video)
);

CREATE TABLE db_upload_video.video (
    id_table INT AUTO_INCREMENT not NULL,
	content longblob not null,
	id_video INT not null unique,
   	CONSTRAINT pk_id_table PRIMARY KEY(id_table),
   	CONSTRAINT fk_id_video foreign key(id_video) references db_upload_video.id_video(id_video)
);

select * from db_upload_video.id_video;

select * from db_upload_video.video;

drop table db_upload_video.id_video;

drop table db_upload_video.video;

SHOW VARIABLES LIKE 'max_allowed_packet';