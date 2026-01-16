create table COOCON_ARS (
  REQ_TR_CD CHAR(4),
  REQ_ORG_CD CHAR(5),
  REQ_TX_DATE CHAR(14),
  REQ_PHONE VARCHAR2(11),
  REQ_AUTH_NO VARCHAR2(6),
  REQ_FILE_SAVE_YN CHAR,
  REQ_FILE_NM VARCHAR2(50 char),
  REQ_AUTH_INQUERY VARCHAR2(300 char),
  REQ_REC_INQUERY VARCHAR2(300 char),
  RESP_RSLT_CD CHAR(4),
  RESP_RSLT_MSG VARCHAR2(40 char),
  RESP_TR_CD VARCHAR2(12),
  RESP_TXT_NO VARCHAR2(30),
  ID CHAR(10) not null
    constraint COOCON_ARS_PK
    primary key
);

comment on table COOCON_ARS is '쿠콘 ARS 내역';
comment on column COOCON_ARS.REQ_TR_CD is '업무구분코드';
comment on column COOCON_ARS.REQ_ORG_CD is '업체코드';
comment on column COOCON_ARS.REQ_TX_DATE is '전송일시';
comment on column COOCON_ARS.REQ_PHONE is '수신자번호';
comment on column COOCON_ARS.REQ_AUTH_NO is '인증번호';
comment on column COOCON_ARS.REQ_FILE_SAVE_YN is '저장요청유무';
comment on column COOCON_ARS.REQ_FILE_NM is '파일명';
comment on column COOCON_ARS.REQ_AUTH_INQUERY is '인증 안내멘트';
comment on column COOCON_ARS.REQ_REC_INQUERY is '녹취안내멘트';
comment on column COOCON_ARS.RESP_RSLT_CD is '처리결과코드';
comment on column COOCON_ARS.RESP_RSLT_MSG is '결과메시지';
comment on column COOCON_ARS.RESP_TR_CD is '거래코드';
comment on column COOCON_ARS.RESP_TXT_NO is '전문고유번호';
