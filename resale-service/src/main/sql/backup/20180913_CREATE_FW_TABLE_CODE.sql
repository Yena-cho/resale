create table FW_TABLE_CODE
(
  TABLE_NAME VARCHAR2(100) not null,
  COLUMN_NAME VARCHAR2(100) not null,
  KEY VARCHAR2(10) not null,
  VALUE VARCHAR2(100) not null,
  ORDER_NO NUMBER(9) default 0 not null,
  DESCRIPTION VARCHAR2(2000),
  constraint SYS_C0022107
  primary key (TABLE_NAME, COLUMN_NAME, KEY)
);

comment on column FW_TABLE_CODE.TABLE_NAME is '테이블명';
comment on column FW_TABLE_CODE.COLUMN_NAME is '컬럼명';
comment on column FW_TABLE_CODE.KEY is '코드';
comment on column FW_TABLE_CODE.VALUE is '화면 표시 내용';
comment on column FW_TABLE_CODE.ORDER_NO is '표시 순서';
comment on column FW_TABLE_CODE.DESCRIPTION is '설명';

