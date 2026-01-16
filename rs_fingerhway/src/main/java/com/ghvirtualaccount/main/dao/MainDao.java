package com.ghvirtualaccount.main.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ghvirtualaccount.vo.MenuInfoVO;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbAuthMenuVO;
import com.ghvirtualaccount.vo.UserInfoVO;

@Repository("mainDao")
public class MainDao {

	@Resource
	private SqlSessionTemplate sqlSession;
	
	public UserInfoVO selectLogin(ParamVO paramVO) throws Exception{
		return sqlSession.selectOne("MainDao.login",paramVO);
	}
	
	public int updateUserLastConnDttm(UserInfoVO paramVO) throws Exception{
		return sqlSession.update("MainDao.updateUserLastConnDttm",paramVO);
	}
	
	public List<MenuInfoVO> selectAuthMenu(UserInfoVO paramVO) throws Exception{
		return sqlSession.selectList("MainDao.selectAuthMenu",paramVO);
	}
	
	public List<MenuInfoVO> selectSubMenu(TbAuthMenuVO paramVO) throws Exception{
		return sqlSession.selectList("MainDao.selectSubMenu",paramVO);
	}
}
