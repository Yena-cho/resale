package com.finger.shinhandamoa.sys.highwayManager.web;


import com.finger.shinhandamoa.sys.highwayManager.dto.SysHighwayListDTO;
import com.finger.shinhandamoa.sys.highwayManager.service.HighwayRcpService;
import com.finger.shinhandamoa.vo.PageVO;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/highway")
public class HighwayController {

    private static final Logger logger = LoggerFactory.getLogger(HighwayController.class);

    @Inject
    private HighwayRcpService highwayRcpService;


    @RequestMapping("/highwayRcpListView")
    public ModelAndView highwayRcpListView(){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("sys/highway/highway-list");

        return mav;
    }

    @RequestMapping("/vanoSendListView")
    public ModelAndView vanoSendListView(){
        ModelAndView mav = new ModelAndView();


        mav.setViewName("sys/highway/highway-send-vano");


        return mav;
    }

    @RequestMapping("/vanoUseListView")
    public ModelAndView vanoUseListView(){
        ModelAndView mav = new ModelAndView();


        mav.setViewName("sys/highway/highway-use-vano");


        return mav;
    }


    @ResponseBody
    @RequestMapping("/rcpLoseList")
    public HashMap<String, Object> highwayRcpLoseList(@RequestBody SysHighwayListDTO body ,HttpServletRequest request) throws Exception{


        logger.debug("======highwayRcpLoseList==========={}",body);
        HashMap<String, Object> reqMap = new HashMap<>();


        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        reqMap.put("startDate", body.getStartDate());
        reqMap.put("endDate", body.getEndDate());
        reqMap.put("chaCd", body.getChaCd());
        reqMap.put("chaName", body.getChaName());
        reqMap.put("searchGb", body.getSearchGb());
        reqMap.put("searchValue", body.getSearchValue());
        reqMap.put("search_orderBy", body.getSearchOrderBy());
        reqMap.put("curPage", body.getCurPage());
        reqMap.put("PAGE_SCALE", body.getPageScale());


        logger.debug("reqMap.toString() {}",reqMap.toString());


        int totCount = highwayRcpService.sysHighwayRcpLoseListCnt(reqMap); //distinct

        PageVO page = new PageVO(totCount, body.getCurPage(), body.getPageScale());
        int start = page.getPageBegin();
        int end = page.getPageEnd();
        reqMap.put("start", start);
        reqMap.put("end", end);
        logger.debug("start.toString() {}",start);
        logger.debug("end.toString() {}",end);



        HashMap<String, Object> resultMap = new HashMap<>();


        resultList = highwayRcpService.sysHighwayRcpLoseList(reqMap);



        result.put("resultList",resultList);
        result.put("totalItemCount",totCount);

        return result;


    }

    @ResponseBody
    @RequestMapping("/vanoSendList")
    public HashMap<String, Object> vanoSendList(@RequestBody SysHighwayListDTO body ,HttpServletRequest request) throws Exception{


        logger.debug("======vanoSendList==========={}",body);
        HashMap<String, Object> reqMap = new HashMap<>();


        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        reqMap.put("trnVanoId", body.getTrnVanoId());


        int totCount = highwayRcpService.sysHighwaySendVanoListCnt(reqMap); //distinct



        logger.debug("reqMap.toString() {}",reqMap.toString());


        PageVO page = new PageVO(totCount, body.getCurPage(), body.getPageScale());
        int start = page.getPageBegin();
        int end = page.getPageEnd();
        reqMap.put("start", start);
        reqMap.put("end", end);
        logger.debug("start.toString() {}",start);
        logger.debug("end.toString() {}",end);



        HashMap<String, Object> resultMap = new HashMap<>();


        resultList = highwayRcpService.sysHighwaySendVanoList(reqMap);



        result.put("resultList",resultList);
        result.put("totalItemCount",totCount);

        return result;


    }

    @RequestMapping("/vanoSend")
    @ResponseBody
    public HashMap<String, Object> vanoSend(@RequestBody SysHighwayListDTO body ,HttpServletRequest request) throws Exception{


        logger.debug("======vanoSend==========={}",body);
        HashMap<String, Object> reqMap = new HashMap<>();


        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        reqMap.put("trnVanoId",body.getTrnVanoId());
        highwayRcpService.vanoSend(reqMap);


        result.put("resultCd","0000");

        return result;


    }

    @ResponseBody
    @RequestMapping("/highwayUsedVanoInfo")
    public HashMap<String, Object> highwayUsedVanoInfo(@RequestBody SysHighwayListDTO body ,HttpServletRequest request) throws Exception{

        logger.debug("======highwayUsedVanoInfo==========={}", body);
        HashMap<String, Object> result = new HashMap<>();
        try {



            Map<String, Object> resultMap = highwayRcpService.highwayUsedVanoInfo(body);

            List<Map<String, Object>> list = new ArrayList<>();

            list.add(resultMap);


            result.put("resultCd", "0000");
            result.put("list", list);

            return result;

        }catch (Exception e){

            result.put("resultCd", "0000");
            result.put("resultMsg", e.getMessage().toString());

            return result;

        }

    }



}
