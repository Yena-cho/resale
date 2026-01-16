-- --------------------------------------------------------------------------------
-- FW_CODE 스키마 변경
-- --------------------------------------------------------------------------------
ALTER TABLE FW_CODE
    MODIFY (NAME VARCHAR2(80 CHAR) );

-- --------------------------------------------------------------------------------
-- FW_CODE 데이터 입력
-- 금융결제원 코드 추가
-- --------------------------------------------------------------------------------
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K00000','K0','요청 전',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K00001','K0','대기',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K00002','K0','처리 중',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K00003','K0','완료',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K50001','K5','부분출금가능',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K50002','K5','전액출금',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K50003','K5','1000원 기준',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K50004','K5','2000원 기준',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K50005','K5','3000원 기준',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K50006','K5','4000원 기준',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K50007','K5','5000원 기준',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K20000','K2','사용안함',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K20001','K2','보험료',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K20002','K2','약관대출',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K20003','K2','이자',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K10001','K1','신규',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K10002','K1','해지',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K10007','K1','임의해지',null,'wisehouse',to_date('2018-10-04 08:16:49','YYYY-MM-DD HH24:MI:SS'));

Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30011','K3','참가기관(은행)점코드 오류','참가기관(은행)점코드 오류','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30012','K3','계좌번호 오류 또는 계좌번호 없음','계좌번호체계 오류(계좌번호길이 부적합, Check Digit 불일치 등), 계좌번호 없음, 준비계좌','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30014','K3','사업자등록번호 또는 생년월일 오류','출금이체신청(EB13) 및 입금이체(EB31)시 계좌번호는 정당하나 이용기 관이 실명번호 검증을 요구한 건에 대하여 동번호가 원장내용과 일치 하지 않는 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30015','K3','계정과목 오류','계좌번호는 있으나 CMS업무에서 지정한 입출금과목이 아닌 경우 (보통예금,저축예금,자유저축예금,당좌예금,기업자유예금,가계당좌예금)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30017','K3','출금이체 미신청계좌','1출금이체(EB21,EC21)에서 출금이체신청등록원장에 해당 계좌번호 및 납부자번호가 없거나 상이한 경우. 2출금이체신청내역(EB13)의 신청구분이 해지신청(“3”) 또는 임의해지 신청(“7”)일 때 출금이체신청등록원장에 해당 계좌번호 및 납부자 번 호가 없거나 상이한 경우.','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30018','K3','출금이체신청 임의해지','참가기관(은행)이 1년 이상 출금요청이 없는 출금이체 신청에 대해 임의 해지한 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30019','K3','출금이체신청 참가기관 해지','납부자가 참가기관을 통해 출금이체 신청을 해지한 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30020','K3','출금동의 증빙자료 부재로 인한 임의해지','납부자의 출금동의 증빙자료 부재로 참가기관이 임의해지한 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30021','K3','잔액 또는 지불가능 잔액 부족','1. 지불가능잔액부족, 대월한도초과, 출금한도초과, 미결제타점권 존재. 2. 부분출금시 잔액이 140원 미만(EB21)이거나 300원 미만(EC21)인 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30022','K3','입금한도 초과','입금이체(EB31)시 입금한도 초과인 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30024','K3','계좌변경으로 인한 출금 이체신청 해지','납부자가 참가기관(은행) 또는 자동이체 통합관리서비스를 통해 계좌를 변경하여 변경 전 계좌에 대한 출금이체신청내역이 해지된 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30031','K3','해약계좌','해지계좌, 이관계좌, 신규취소계좌, 비활동계좌','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30032','K3','가명계좌 또는 실명미확인','실명전환이 이루어지지 않은 계좌, 가명계좌','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30033','K3','잡좌','잡좌편입계좌, 잡수입계좌','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30034','K3','법적제한계좌, 지급정지 또는 사고신고계좌','1. 거래정지계좌, 지급정지계좌(압류.가압류 이외의 사유), 부도등록계좌, 사망신고계좌. 2. 사고신고계좌(통장분실, 인감분실 등)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30035','K3','압류.가압류 계좌','압류.가압류계좌','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30036','K3','잔액증명발급 계좌','잔액증명발급계좌, 부채증명발급계좌','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30037','K3','연체계좌 또는 지점통제계좌','연체계좌(당좌, 가계당좌), 지점자체사유에 의한 입지급통제계좌','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30041','K3','참가기관시스템 오류','시스템 오류(DB읽기 오류 등)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30051','K3','기타 오류','표준불능코드에 정의되지 않은 사유로 발생한 오류','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30061','K3','의뢰금액이 “0”원인 경우','의뢰금액이 “0”원인 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30062','K3','건당 이체금액한도 초과','건당 이체금액한도 초과','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30065','K3','법인계좌 사용불가','증권사 등은 법인계좌 및 투자자예탁금이외의 계좌는 입/출금이 불가 능 (증권사 등에서 확인하여 세트하여야 함)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30066','K3','투자자예탁금이 아님','증권사 등은 법인계좌 및 투자자예탁금이외의 계좌는 입/출금이 불가능 입출금이 가능한 계좌는 투자자예탁금으로 이루어진 계좌만 가능함 증권사 등에서 확인하여 세트하여야 함','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30068','K3','“통장기재내용” Field에 HEX 20(Space)보다 작은 값이 왔을 때','“통장기재내용” Field에 HEX 20(Space)보다 작은 값이 왔을 때','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30075','K3',null,'1. 출금의뢰내역(EB21, EC21)의 “출금형태”항목에 전액출금(1) 또는 부분출금가능(0, 2, 3, 4, 5, 6)이 아닌 값이 왔을 경우. 2. 출금의뢰내역에 의뢰금액이 140원 미만(EB21)이거나 300원 미만(EC21)인 경우.','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30077','K3','계좌변경 내역을 상대 참가기관(변경 전 또는 변경 후)에서 전송하지 않은 경우','계좌변경 내역을 상대 참가기관(변경 전 또는 변경 후)에서 전송하지 않은 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30078','K3','이용기관이 EI13을 전송하지 않아 EB13 신규 신청내역을 불능처리 하는 경우','이용기관이 EI13을 전송하지 않아 EB13 신규 신청내역을 불능처리 하는 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30079','K3','이용기관이 계좌변경 내역을 SET로 처리하지 않아 결제원이 참가기관으로 대행응답 하는 경우','이용기관이 계좌변경 내역을 SET로 처리하지 않아 결제원이 참가기관으로 대행응답 하는 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30081','K3','"Record 구분" 항목이 “H", "R" 또는 "T"가 아니거나, 일련번호가 오류인 경우','"Record 구분" 항목이 “H", "R" 또는 "T"가 아니거나, 일련번호가 오류인 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30087','K3','한글 오류 (한글코드 Field에 한글코드이외의 값이 올 경우)','한글 오류 (한글코드 Field에 한글코드이외의 값이 올 경우)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30088','K3','영문자.숫자 오류 (영.숫자코드 Field에 영.숫자코드 이외의 값이 올 경우)','영문자.숫자 오류 (영.숫자코드 Field에 영.숫자코드 이외의 값이 올 경우)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30089','K3','Space 오류 (Space Field에 Space가 아닌 값이 올 경우)','Space 오류 (Space Field에 Space가 아닌 값이 올 경우)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30090','K3','All Zero 오류 (All Zero Field에 All Zero가 아닌 값이 올 경우)','All Zero 오류 (All Zero Field에 All Zero가 아닌 값이 올 경우)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30091','K3','생년월일 대신 주민등록번호로 신청하는 경우','생년월일 대신 주민등록번호로 신청하는 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30096','K3','CMS 미참가 참가기관(은행)','CMS 미참가 참가기관(은행)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K30098','K3',null,'Alpha-Numeric + Space 오류 예) 계좌번호 Field (16자리인 경우) 11234567890123456 - (○) 21234567890 + Space 6자리 - (○) 31234567890 + Space + 12345 - (×)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K39998','K3','기타 오류 (표준불능코드에 정의되지 않은 사유로 발생한 오류)','기타 오류 (표준불능코드에 정의되지 않은 사유로 발생한 오류)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K39999','K3','참가기관(은행)시스템 장애','참가기관(은행)시스템 장애','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A011','K3','신청일자 오류','출금이체신청내역(EB13)에서 신청일자가 없거나 자료전송일보다 이후 인 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A012','K3','신청구분 오류 (출금이체신청내역(EB13)에서 신청구분이 신규(1), 해지(3), 임의해지(7)이 아닌 경우)','신청구분 오류 (출금이체신청내역(EB13)에서 신청구분이 신규(1), 해지(3), 임의해지(7)이 아닌 경우)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A013','K3','납부자번호 상이 또는 없음','출금이체신청내역(EB11)의 납부자번호가 고객원장에 없거나 해당 납부자가 자사 고객이 아닌 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A016','K3','이중신청','이미 이용기관의 출금이체신청등록원장에 등록 또는 해지된 계좌 번호 및 납부자번호에 대하여 재신청한 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A016','K3','이중신청','이미 출금이체신청등록원장에 등록된 계좌번호 및 납부자번호에 대하 여 재신청한 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A017','K3','기타오류','표준불능코드에 정의되지 않은 사유로 발생한 오류','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A018','K3','납부자번호체계 오류','특정 이용기관의 출금이체신청내역(EB13)의 납부자번호가 결제원으로 부터 사전 통지받은 납부자번호체계와 다른 경우(납부자번호체계를 검 증하는 참가기관에 한함)','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));
Insert into FW_CODE (CODE,CODE_GROUP,NAME,DESCRIPTION,MODIFY_USER,MODIFY_DATE) values ('K3A019','K3','출금요청 중 또는 출금일 도래','이용기관이 출금요청 중이거나 고객과 계약된 출금일이 도래하여 계좌변경이 불가능한 경우','wisehouse',to_date('2018-10-11 09:46:14','YYYY-MM-DD HH24:MI:SS'));

-- --------------------------------------------------------------------------------
-- KFTC_PAYER 변경
-- --------------------------------------------------------------------------------
ALTER TABLE KFTC_PAYER
  ADD (EB13_ID CHAR(8) );

ALTER TABLE KFTC_PAYER
  ADD (EB14_ID CHAR(8) );

COMMENT ON COLUMN KFTC_PAYER.USER_DEFINE_1 IS '사용자 정의 #1';
COMMENT ON COLUMN KFTC_PAYER.USER_DEFINE_2 IS '사용자 정의 #2';
COMMENT ON COLUMN KFTC_PAYER.USER_DEFINE_3 IS '사용자 정의 #3';
COMMENT ON COLUMN KFTC_PAYER.USER_DEFINE_4 IS '사용자 정의 #4';
COMMENT ON COLUMN KFTC_PAYER.USER_DEFINE_5 IS '사용자 정의 #5';
COMMENT ON COLUMN KFTC_PAYER.EB13_ID IS 'EB13 파일명';
COMMENT ON COLUMN KFTC_PAYER.EB14_ID IS 'EB14 파일명';
