drop table fileprocessor.users;


create table fileprocessor.users (
  id_user bigint not null auto_increment,
  user_cpf varchar(11) not null,
  user_name varchar(30) not null,
  role varchar(30) not null,
  primary key pk_srs_dsr (id_user));

insert into fileprocessor.users (user_cpf, user_name, role) value ('29382184864', 'fabio', 'ADMIN');
insert into fileprocessor.users (user_cpf, user_name, role) value ('25172446001', 'ana', 'USER');
commit;

