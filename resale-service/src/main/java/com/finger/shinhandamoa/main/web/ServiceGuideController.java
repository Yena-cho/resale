package com.finger.shinhandamoa.main.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author by puki
 * @date 2018. 3. 30.
 * @desc 최초생성
 */
@Controller
@RequestMapping("common/serviceGuide/**")
public class ServiceGuideController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceGuideController.class);

    /*
     * 서비스안내 > DAMOA란?
     */
    @RequestMapping("infoDamoa")
    public ModelAndView infoDamoa() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/serviceGuide/infoDamoa");

        return mav;
    }

    /*
     * 서비스안내 > 이용안내
     */
    @RequestMapping("userGuide")
    public ModelAndView userGuide() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/serviceGuide/userGuide");

        return mav;
    }

}
