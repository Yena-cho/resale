create table XCASHMAS
(
  CASHMASCD varchar(20)             not null,
  RCPMASCD  varchar(20)             not null,
  CHACD     varchar(8)              not null,
  CHAOFFNO  varchar(10)             not null,
  CUSOFFNO  varchar2(20),
  CUSTYPE   varchar2(1),
  TIP       NUMBER(9),
  TAX       NUMBER(9),
  JOB       varchar(1),
  RCPAMT    NUMBER(9)            not null,
  CONFIRM varchar(20),
  CUSOFFNO2 varchar(20),
  CASHMASST varchar(4)              not null,
  MAKEDT    DATE,
  MAKER     varchar(20),
  REGDT     DATE not null,
  primary key(CASHMASCD)
);