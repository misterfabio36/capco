drop table fileprocessor.users;


create table fileprocessor.users (
  id_user bigint not null auto_increment,
  user_cpf varchar(11) not null,
  user_name varchar(30) not null,
  role varchar(30) not null,
  primary key pk_srs_dsr (id_user));

insert into fileprocessor.users (id_user, user_cpf, user_name, role) value (null, '29382184864', 'fabio', 'ADMIN');
insert into fileprocessor.users (id_user, user_cpf, user_name, role) value (null, '25172446001', 'fabio', 'USER');
commit;

