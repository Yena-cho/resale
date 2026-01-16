package com.finger.shinhandamoa.sys.bbs.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class SysDTO {
	//-------------------board table culum------------------------------
	private int rn;
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
	
	//-------------------------공지사항 관리 추가 컬럼----------------------------------------
	
	private String option0;	//공지사항 관리페이지 권한에 따른 검색 옵션
	private String option1;	
	private String option2;
	private String option3;
	private String searchOrderBy;
	private String searchOption;
	private String keyword;
	private int pageScale;
	private int curPage;
	private String startday;
	private String endday;
	
	//____________________자주하는 질문_____________
	private String category;
	private String bbssubstr; 
	
	//---------------------QnA 
	private int chacd;
	private String chaname;
	private int datcnt;
	
	//_________________엑셀 파일 다운로드_____________________
	private String exSearchOption;
	private String exKeyword;
	private String exStartday;
	private String exEndday;
	private String exOption0;
	private String exOption1;
	private String exOption2;
	private String exOption3;
	private int exCount;
	
	private String exRegdate;
	private String exTitle;
	private String exCategory;
	
	private int exChacode;
	private String exChaname;


	// 공지사항 스마트에디터2 사진업로드
	private MultipartFile Filedata;
	//callback URL
	private String callback;
	//콜백함수??
	private String callback_func;

	public MultipartFile getFiledata() {
		return Filedata;
	}

	public void setFiledata(MultipartFile filedata) {
		Filedata = filedata;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getCallback_func() {
		return callback_func;
	}

	public void setCallback_func(String callback_func) {
		this.callback_func = callback_func;
	}
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
	public int getExChacode() {
		return exChacode;
	}
	public void setExChacode(int exChacode) {
		this.exChacode = exChacode;
	}
	public String getExChaname() {
		return exChaname;
	}
	public void setExChaname(String exChaname) {
		this.exChaname = exChaname;
	}
	public String getExCategory() {
		return exCategory;
	}
	public void setExCategory(String exCategory) {
		this.exCategory = exCategory;
	}
	public String getExOption0() {
		return exOption0;
	}
	public void setExOption0(String exOption0) {
		this.exOption0 = exOption0;
	}
	public String getExRegdate() {
		return exRegdate;
	}
	public void setExRegdate(String exRegdate) {
		this.exRegdate = exRegdate;
	}
	public String getExTitle() {
		return exTitle;
	}
	public void setExTitle(String exTitle) {
		this.exTitle = exTitle;
	}
	public String getExSearchOption() {
		return exSearchOption;
	}
	public void setExSearchOption(String exSearchOption) {
		this.exSearchOption = exSearchOption;
	}
	public String getExKeyword() {
		return exKeyword;
	}
	public void setExKeyword(String exKeyword) {
		this.exKeyword = exKeyword;
	}
	public String getExStartday() {
		return exStartday;
	}
	public void setExStartday(String exStartday) {
		this.exStartday = exStartday;
	}
	public String getExEndday() {
		return exEndday;
	}
	public void setExEndday(String exEndday) {
		this.exEndday = exEndday;
	}
	public String getExOption1() {
		return exOption1;
	}
	public void setExOption1(String exOption1) {
		this.exOption1 = exOption1;
	}
	public String getExOption2() {
		return exOption2;
	}
	public void setExOption2(String exOption2) {
		this.exOption2 = exOption2;
	}
	public String getExOption3() {
		return exOption3;
	}
	public void setExOption3(String exOption3) {
		this.exOption3 = exOption3;
	}
	public int getExCount() {
		return exCount;
	}
	public void setExCount(int exCount) {
		this.exCount = exCount;
	}
	public int getDatcnt() {
		return datcnt;
	}
	public void setDatcnt(int datcnt) {
		this.datcnt = datcnt;
	}
	public int getChacd() {
		return chacd;
	}
	public void setChacd(int chacd) {
		this.chacd = chacd;
	}
	public String getChaname() {
		return chaname;
	}
	public void setChaname(String chaname) {
		this.chaname = chaname;
	}
	public String getBbssubstr() {
		String bbssubstr = "";
		bbssubstr = String.valueOf(getBbs());
		bbssubstr = bbssubstr.substring(0, 1);
		return bbssubstr;
	}
	public void setBbssubstr(String bbssubstr) {
		this.bbssubstr = bbssubstr;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getOption0() {
		return option0;
	}
	public void setOption0(String option0) {
		this.option0 = option0;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStartday() {
		return startday;
	}
	public void setStartday(String startday) {
		this.startday = startday;
	}
	public String getEndday() {
		return endday;
	}
	public void setEndday(String endday) {
		this.endday = endday;
	}
	public String getOption1() {
		return option1;
	}
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	public String getOption2() {
		return option2;
	}
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	public String getOption3() {
		return option3;
	}
	public void setOption3(String option3) {
		this.option3 = option3;
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
	
	public int getPageScale() {
		return pageScale;
	}
	public void setPageScale(int pageScale) {
		this.pageScale = pageScale;
	}
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getRn() {
		return rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
	
	
}
