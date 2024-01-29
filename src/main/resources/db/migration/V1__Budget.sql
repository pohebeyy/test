create table budget
(
    id     serial primary key,
    year   int  not null,
    month  int  not null,
    amount int  not null,
    type   text not null
);
create table Author
(
     fullName String  = varchar "full_name", 255


)