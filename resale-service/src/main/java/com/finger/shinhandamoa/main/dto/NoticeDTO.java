package com.finger.shinhandamoa.main.dto;

import java.util.Date;

/**
 * @author  by puki
 * @date    2018. 3. 30.
 * @desc    최초생성
 * @version 
 * 
 */
public class NoticeDTO {

	private int rn;
	private String impoYN;
	private int viewCnt;
	private Date regDt;
	private String regEmp;
	private int notiNo;
	
	private long no;				//board 테이블 전체의 순번 2000000 부터 시작
	private int bbs;			//구분값 7 : 공지사항(로그인전)  8 : 공지사항   9 : 자료실  10 : 서비스문의
	private int num;			//구분값에 따른 글번호
	private long fromno;			//부모글 번호??
	private String step;		//댓글 들여쓰기??
	private String id;			//작성자 ID
	private String password;	//암호
	private String writer;		//작성자
	private String email;		//이메일
	private String title;		//제목
	private String contents;	//내용
	private String filename;	//파일명
	private String transfilename;
	private int filesize;		//파일크기
	private String ip;			//작성자 ip
	private int ishtml;			//??
	private int isfix;			//상단에 고정되어야 하는 중요공지 구분값 
	private int count;			//조회수
	private String day;			//작성일
	private String sendemail;	//이메일
	private int issend;			//이메일 발송 여부
	private String code;		//기관코드
	private Date day1;			//기타일자
	private String data1;		//asis 소스에서 tellno를 집어넣는걸로 봐서는 전화번호인듯.
	private String data2;		//
	private String data3;		//
	private String data4;		//
	private String data5;		//
	private String data6;		//
	private String data7;		//문의하기 카테고리
	private String data8;		//
	private String fileid;
	private String fileext;
	
	private int recently;
	private int pageScale;// PAGE_SCALE;
	private String searchOption;
	private String keyWord;
	private int curPage;
	private String searchOrderBy;
	private String icon;
	private int datcnt;
	
	//QnA 글쓰기기관 담당자 전화번호
	private String ownertel;
	private String chrhp;
	
	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getFileext() {
		return fileext;
	}

	public void setFileext(String fileext) {
		this.fileext = fileext;
	}

	public String getTransfilename() {
		if(getFilename() == null){
			transfilename = getFilename();
		}else if(getFilename().length() >= 32)
			transfilename = getFilename().substring(32);
		else
			transfilename = getFilename();
		return transfilename;
	}

	public void setTransfilename(String transfilename) {
		this.transfilename = transfilename;
	}

	public String getOwnertel() {
		return ownertel;
	}

	public void setOwnertel(String ownertel) {
		this.ownertel = ownertel;
	}

	public String getChrhp() {
		return chrhp;
	}

	public void setChrhp(String chrhp) {
		this.chrhp = chrhp;
	}

	public int getDatcnt() {
		return datcnt;
	}

	public void setDatcnt(int datcnt) {
		this.datcnt = datcnt;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getPageScale() {
		return pageScale;
	}

	public String getSearchOrderBy() {
		return searchOrderBy;
	}

	public void setSearchOrderBy(String searchOrderBy) {
		this.searchOrderBy = searchOrderBy;
	}

	public String getSearchOption() {
		return searchOption;
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public void setPageScale(int pageScale) {
		this.pageScale = pageScale;
	}

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}


	public int getBbs() {
		return bbs;
	}

	public void setBbs(int bbs) {
		this.bbs = bbs;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getFromno() {
		return fromno;
	}

	public void setFromno(long fromno) {
		this.fromno = fromno;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getFilesize() {
		return filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getIshtml() {
		return ishtml;
	}

	public void setIshtml(int ishtml) {
		this.ishtml = ishtml;
	}

	public int getIsfix() {
		return isfix;
	}

	public void setIsfix(int isfix) {
		this.isfix = isfix;
	}

	
	public String getDay() {
		day = day.substring(0, 4)+ "." + day.substring(5, 7) + "." + day.substring(8, 10);
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getSendemail() {
		return sendemail;
	}

	public void setSendemail(String sendemail) {
		this.sendemail = sendemail;
	}

	public int getIssend() {
		return issend;
	}

	public void setIssend(int issend) {
		this.issend = issend;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDay1() {
		return day1;
	}

	public void setDay1(Date day1) {
		this.day1 = day1;
	}

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

	public String getData6() {
		return data6;
	}

	public void setData6(String data6) {
		this.data6 = data6;
	}

	public String getData7() {
		return data7;
	}

	public void setData7(String data7) {
		this.data7 = data7;
	}

	public String getData8() {
		return data8;
	}

	public void setData8(String data8) {
		this.data8 = data8;
	}
	

	public int getNotiNo() {
		return notiNo;
	}

	public void setNotiNo(int notiNo) {
		this.notiNo = notiNo;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRecently() {
		return recently;
	}
	
	public void setRecently(int recently) {
		this.recently = recently;
	}
	
	
	
	
	
	
	

//------ 이후 삭제

	


	public int getRn() {
		return rn;
	}

	public void setRn(int rn) {
		this.rn = rn;
	}

	public String getImpoYN() {
		return impoYN;
	}

	public void setImpoYN(String impoYN) {
		this.impoYN = impoYN;
	}

	public int getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public String getRegEmp() {
		return regEmp;
	}

	public void setRegEmp(String regEmp) {
		this.regEmp = regEmp;
	}
	

		
}
