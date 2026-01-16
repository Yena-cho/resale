package kr.co.finger.damoa.scheduler.controller;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import kr.co.finger.damoa.scheduler.model.Result;
import kr.co.finger.damoa.scheduler.task.*;
import kr.co.finger.damoa.scheduler.task.daily.CashReceiptFileReceiveTask;
import kr.co.finger.shinhandamoa.scheduler.task.daily.DailySettleTask;
import kr.co.finger.shinhandamoa.scheduler.task.daily.VaSettleTask;
import kr.co.finger.shinhandamoa.service.ClientSettleService;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class GatewayController {

    @Autowired
    private ClientSettleService clientSettleService;

    @Autowired
    private VaSettleTask dailySettleTask;

    private Logger LOG = LoggerFactory.getLogger(getClass());


    /**
     *  월 마감 작업
     * @param params yyyyMM
     * @throws Exception
     */
    @RequestMapping(value = "/force/monthlySettleForce", method = RequestMethod.POST)
    public Result monthlySettleForce(@RequestBody JSONObject params) throws Exception {

        if(params.get("requestMonth") == null){
            return new Result("요청월이없습니다");
        }
        String requestMonth = params.get("requestMonth").toString();
        clientSettleService.executeMonthlySettle(requestMonth);

        return new Result("OK");
    }

    /**
     *  일 마감 작업
     * @param params yyyyMMdd
     * @throws Exception
     */
    @RequestMapping(value = "/force/dailySettleForce", method = RequestMethod.POST)
    @ResponseBody
    public Result dailySettleForce(@RequestBody JSONObject params) throws Exception {

        LOG.info("가상계좌 일 마감 시작 ");
        if(params.get("requestDay") == null){
            return new Result("요청일이없습니다");
        }
        String requestDay = params.get("requestDay").toString();
        clientSettleService.executeDailySettle(requestDay);

        return new Result("OK");
    }

    /**
     *  일 정산 작업
     * @param params yyyyMMdd
     * @throws Exception
     */
    @RequestMapping(value = "/force/vaSettleForce", method = RequestMethod.POST)
    @ResponseBody
    public Result vaSettleForce(@RequestBody JSONObject params) throws Exception {

        LOG.info("가상계좌 일 정산 시작 ");
        if(params.get("requestDay") == null){
            return new Result("요청일이없습니다");
        }
        String requestDay = params.get("requestDay").toString();
        clientSettleService.executeVaSettle(requestDay);

        return new Result("OK");
    }






}
