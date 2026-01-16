package com.ghvirtualaccount.usermgmt.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ghvirtualaccount.usermgmt.dao.UserMgmtDao;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbBelongInfoVO;
import com.ghvirtualaccount.vo.TbUserVO;
import com.ghvirtualaccount.vo.UserInfoVO;

@Service("userMgmtService")
public class UserMgmtServiceImpl implements UserMgmtService {

	private static Logger logger = LoggerFactory.getLogger(UserMgmtServiceImpl.class);
	
	@Resource(name="userMgmtDao")
	private UserMgmtDao userMgmtDao;
	
	@Override
	public int getUserInfoListCnt(ParamVO paramVO) throws Exception{
		
		logger.debug("========= selectUserInfoListCnt start ========");
		
		return userMgmtDao.selectUserInfoListCnt(paramVO);
	}
	
	@Override
	public List<UserInfoVO> getUserInfoList(ParamVO paramVO) throws Exception{
		
		logger.debug("========= getUserInfoList start ========");
		
		return userMgmtDao.selectUserInfoList(paramVO);
	}

	@Override
	public UserInfoVO getUserInfo(ParamVO paramVO) throws Exception{
		logger.debug("========= getUserInfo start ========");
		
		return userMgmtDao.selectUserInfo(paramVO);
	}
	
	@Override
	public void removeUserInfo(ParamVO paramVO) throws Exception{
		logger.debug("========= removeUserInfo start ========");
		
		userMgmtDao.deleteUserInfo(paramVO);
	}
	
	@Override
	public void modifyUserInfo(TbUserVO tbUserVO) throws Exception{
		logger.debug("========= modifyUserInfo start ========");
		
		userMgmtDao.updateUserInfo(tbUserVO);
	}
	
	@Override
	public void addUserInfo(TbUserVO tbUserVO) throws Exception{
		logger.debug("========= addUserInfo start ========");
		
		userMgmtDao.insertUserInfo(tbUserVO);
	}
	
	public List<TbBelongInfoVO> getBelongInfoList() throws Exception{
		logger.debug("========= getBelongInfoList start ========");
		
		return userMgmtDao.selectBelongInfoList();
	}
}
