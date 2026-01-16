package com.ghvirtualaccount.usermgmt.service;

import java.util.List;

import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbBelongInfoVO;
import com.ghvirtualaccount.vo.TbUserVO;
import com.ghvirtualaccount.vo.UserInfoVO;

public interface UserMgmtService {

	public int getUserInfoListCnt(ParamVO paramVO) throws Exception;
	
	public List<UserInfoVO> getUserInfoList(ParamVO paramVO) throws Exception;
	
	public UserInfoVO getUserInfo(ParamVO paramVO) throws Exception;
	
	public void removeUserInfo(ParamVO paramVO) throws Exception;
	
	public void modifyUserInfo(TbUserVO tbUserVO) throws Exception;

	public void addUserInfo(TbUserVO tbUserVO) throws Exception;
	
	public List<TbBelongInfoVO> getBelongInfoList() throws Exception;
}
