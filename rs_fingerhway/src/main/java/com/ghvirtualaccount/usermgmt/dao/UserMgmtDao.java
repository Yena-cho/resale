package com.ghvirtualaccount.usermgmt.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbBelongInfoVO;
import com.ghvirtualaccount.vo.TbUserVO;
import com.ghvirtualaccount.vo.UserInfoVO;

@Repository("userMgmtDao")
public class UserMgmtDao {

	@Resource
	private SqlSessionTemplate sqlSession;
	
	public int selectUserInfoListCnt(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("UserMgmtDao.selectUserInfoListCnt",paramVO);
	}
	public List<UserInfoVO> selectUserInfoList(ParamVO paramVO) throws Exception{
		return sqlSession.selectList("UserMgmtDao.selectUserInfoList",paramVO);
	}
	public UserInfoVO selectUserInfo(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("UserMgmtDao.selectUserInfo",paramVO);
	}
	public void deleteUserInfo(ParamVO paramVO) throws Exception{
		sqlSession.selectOne("UserMgmtDao.deleteUserInfo",paramVO);
	}
	public void updateUserInfo(TbUserVO tbUserVO) throws Exception{
		sqlSession.update("UserMgmtDao.updateUserInfo",tbUserVO);
	}
	public void insertUserInfo(TbUserVO tbUserVO) throws Exception{
		sqlSession.insert("UserMgmtDao.insertUserInfo",tbUserVO);
	}
	public List<TbBelongInfoVO> selectBelongInfoList() throws Exception{
		return sqlSession.selectList("UserMgmtDao.selectBelongInfoList");
	}
}
