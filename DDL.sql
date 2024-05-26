drop schema if exists techguardian;

drop user if exists 'user'@'localhost';

create schema techguardian;

create user 'user'@'localhost' identified by 'pass123';

grant select, insert, delete, update on techguardian.* to user@'localhost';

use techguardian;

create table registro_entrada (
    ent_id bigint not null auto_increment,
    data_entrada varchar(10) not null,
    hora_entrada varchar(10) not null, 
    quant_entrada int not null,
    status_entrada varchar(10),
    primary key (ent_id)
);

create table registro_saida (
    sai_id bigint not null auto_increment,
    data_saida varchar(10) not null,
    hora_saida varchar(10) not null,
    quant_saida int not null,
    status_saida varchar(10),
    primary key (sai_id)
);

create table usr_usuario (
    usr_id bigint unsigned not null auto_increment,
    usr_nome varchar(20) not null,
    usr_email varchar(50) not null,
    usr_senha varchar(150) not null,
    primary key (usr_id),
    unique key uni_usuario_nome (usr_nome)
);

create table aut_autorizacao (
    aut_id bigint unsigned not null auto_increment,
    aut_nome varchar(20) not null,
    primary key (aut_id),
    unique key uni_aut_nome (aut_nome)
);

create table uau_usuario_autorizacao (
    usr_id bigint unsigned not null,
    aut_id bigint unsigned not null,
    primary key (usr_id, aut_id),
    foreign key aut_usuario_fk (usr_id) references usr_usuario (usr_id) on delete restrict on update cascade,
    foreign key aut_autorizacao_fk (aut_id) references aut_autorizacao (aut_id) on delete restrict on update cascade
);

insert into registro_entrada (ent_id, data_entrada, hora_entrada, quant_entrada, status_entrada)
    values (1, '2024-04-03', '19:10:00', '1', 'entrada');
insert into registro_saida (sai_id, data_saida, hora_saida, quant_saida, status_saida)
    values (1, '2024-04-02', '19:20:00', '1', 'saida');

insert into usr_usuario (usr_nome, usr_email, usr_senha)
    values ('admin', 'admin@admin', '$2a$10$i3.Z8Yv1Fwl0I5SNjdCGkOTRGQjGvHjh/gMZhdc3e7LIovAklqM6C'),
    ('user','user@user', '$2a$10$8Cye0s.CHRQD9PddqA/cEuhJlp.vTXwXrKKXMlT73S6.T0JYZc.wK');
insert into aut_autorizacao (aut_nome)
    values ('ROLE_ADMIN'),
    ('ROLE_USER');
insert into uau_usuario_autorizacao (usr_id, aut_id) 
    values (1, 1),
    (2, 2);