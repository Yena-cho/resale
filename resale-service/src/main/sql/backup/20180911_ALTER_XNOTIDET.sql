/**
 * 청구항목 비고 50byte를 50char 로 변경
 */
ALTER TABLE XNOTIDET  
MODIFY (PTRITEMREMARK VARCHAR2(50 CHAR) );

/*
 -- 먼저 테이블 백업
 create table  XNOTIDET_BAK as select * from XNOTIDET

*/