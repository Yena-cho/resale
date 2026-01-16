package com.ghvirtualaccount.main.service;

import java.util.Map;

import com.ghvirtualaccount.vo.ParamVO;

@SuppressWarnings("rawtypes")
public interface MainService {

	public Map login(ParamVO paramVO) throws Exception;
}
