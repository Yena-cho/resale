package com.finger.shinhandamoa.home.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finger.shinhandamoa.home.dto.LoginDTO;
import com.finger.shinhandamoa.main.dto.NoticeDTO;
import com.finger.shinhandamoa.payer.cashreceipt.dto.CashReceiptDTO;
import com.finger.shinhandamoa.payer.notification.dto.NotificationDTO;
import com.finger.shinhandamoa.payer.payment.dto.PaymentDTO;
import com.finger.shinhandamoa.sys.bbs.dto.BannerDTO;
import com.finger.shinhandamoa.sys.bbs.dto.PopupDTO;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
public interface HomeDAO {

    public List<NoticeDTO> nList(Map<String, Object> reqMap) throws Exception;

    public List<NoticeDTO> fList(Map<String, Object> reqMap) throws Exception;

    public List<NoticeDTO> qList(Map<String, Object> reqMap) throws Exception;

    public List<PopupDTO> pList(Map<String, Object> reqMap) throws Exception;

    public List<BannerDTO> pcBannerList(Map<String, Object> reqMap) throws Exception;

    public List<BannerDTO> mobileBannerList(Map<String, Object> reqMap) throws Exception;

    // 전화상담예약
    public void telReservation(Map<String, Object> map) throws Exception;

    // id 찾기
    public LoginDTO selIdSearch(Map<String, Object> map) throws Exception;

    // 비밀번호 찾기
    public LoginDTO selPasswordSearch(Map<String, Object> map) throws Exception;

    // id 중복확인
    public String selIdOverlap(String loginId) throws Exception;

    // 아이디, 비밀번호 변경
    public void updateIdPassword(Map<String, Object> map) throws Exception;

    // 비밀번호 재설정 - 인증번호
    public void mergeOtpNo(Map<String, Object> map) throws Exception;

    // 비밀번호  재설정
    public void updatePassword(Map<String, Object> map) throws Exception;

    // 약관동의 여부 저장
    public void updateChaSvcYn(Map<String, Object> map) throws Exception;

    // 고지분 조회
    public NotificationDTO getNotification(HashMap<String, Object> reqMap) throws Exception;

    // 최근납부내역
    public List<PaymentDTO> selectPaymentSummary(HashMap<String, Object> reqMap) throws Exception;

    // 현금영수증 내역
    public CashReceiptDTO getCashReceipt(HashMap<String, Object> reqMap) throws Exception;

    // 고객명 자동완성
    public List<LoginDTO> selAutoChaName(String username) throws Exception;

    // 오류항목 찾기
    public int selLoginItem(HashMap<String, Object> map) throws Exception;

    // 약관확인
    public String selChaSvcYn(HashMap<String, Object> map) throws Exception;

    public int reciptCnt(HashMap<String, Object> map) throws Exception;

    // 자동출금 관련 기관 조회
    public LoginDTO selCmsChaInfo(Map<String, Object> map) throws Exception;
} 