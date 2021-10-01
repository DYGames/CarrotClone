CREATE DATABASE carrot;

use carrot;

create table user (
id varchar(25) not null primary key,
location varchar(25) not null,
password varchar(25) not null,
pic varchar(255) not null);

create table category (
id int primary key,
title varchar(25) not null);

create table product (
id int not null auto_increment primary key,
userid varchar(25) not null,
title varchar(50) not null,
category int,
descr varchar(512) not null,
date datetime not null,
price int,
foreign key (userid) references user(id),
foreign key (category) references category(id));

create table picture (
id int primary key,
productid int not null,
url varchar(255) not null,
foreign key (productid) references product(id));

insert into user values('dygames', '안암동1가', '0000', 'https://dygames.github.io/static/7973af728204370096b2d91a309ca475/29667/profile-pic.jpg');
insert into category values(0, '남성패션/잡화');
insert into product(userid, title, category, descr, date, price) values('dygames', '나이키 신발 팝니다', 0, '상세사이즈는 택사진 확인 부탁드려요~\n제가 근무시간이 불규칙해서\n거래는 안암역', now(), 25000);
insert into picture values (0, 1, 'https://cdn-images.farfetch-contents.com/13/67/84/93/13678493_21608896_600.jpg');

ALTER TABLE product AUTO_INCREMENT=1;
SET @COUNT = 0;
UPDATE product SET id = @COUNT:=@COUNT+1;

select location from user where id = 0;
select a.id, a.title, b.location, a.date, a.price, max(c.url) from product as a join user as b join picture as c on a.userid = b.id and a.id = c.productid;
select b.url from product as a join picture as b on a.id = b.productid where a.id = 1;
select b.pic, b.id, b.location, a.title, a.category, a.date, a.descr, a.price from product as a join user as b join picture as c on a.userid = b.id and a.id = c.productid where a.id = 1;
