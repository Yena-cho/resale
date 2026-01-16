package com.ghvirtualaccount.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ghvirtualaccount.main.dao.MainDao;
import com.ghvirtualaccount.vo.MenuInfoVO;
import com.ghvirtualaccount.vo.ParamVO;
import com.ghvirtualaccount.vo.TbAuthMenuVO;
import com.ghvirtualaccount.vo.UserInfoVO;

@SuppressWarnings("rawtypes")
@Service("mainService")
public class MainServiceImpl implements MainService {

	private static Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);
	
	@Resource(name="mainDao")
	private MainDao mainDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map login(ParamVO paramVO) throws Exception{
		
		logger.debug("========= login start ========");
		
		Map returnMap = new HashMap();
		
		//로그인 처리
		UserInfoVO userInfoVO = mainDao.selectLogin(paramVO);
		if(userInfoVO == null){
			//로그인 불가
			return returnMap;
		} else {
			returnMap.put("userInfoVO", userInfoVO);
			
			//최종 로그인 일시 업데이트
			mainDao.updateUserLastConnDttm(userInfoVO);
			
			//상단메뉴 조회
			List<MenuInfoVO> menuInfoList = mainDao.selectAuthMenu(userInfoVO);
			
			//서브메뉴 조회
			if (menuInfoList.size()>=0){
				for(int i=0;i<menuInfoList.size();i++){
					TbAuthMenuVO tbAuthMenuVO = new TbAuthMenuVO();
					tbAuthMenuVO.setAuthGrpCd(userInfoVO.getAuthGrpCd());
					tbAuthMenuVO.setMenuId(menuInfoList.get(i).getMenuId());
					List<MenuInfoVO> menuInfoList2 = mainDao.selectSubMenu(tbAuthMenuVO);
					menuInfoList.get(i).setSubMenu(menuInfoList2);
				}
				returnMap.put("menuInfo", menuInfoList);
			}
		}
		
		return returnMap;
	}

}
