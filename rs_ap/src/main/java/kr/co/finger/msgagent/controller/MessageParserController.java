package kr.co.finger.msgagent.controller;

import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.Layout;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 전문을 파싱하는 Controller .
 *
 */
@Controller
public class MessageParserController {
    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private NettyProducer nettyProducer;


    private Logger LOG = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/parse/message", method = RequestMethod.GET)
    public ModelAndView parseMessage( ModelAndView modelAndView) {
        modelAndView.addObject("message", "");
        modelAndView.setViewName("message-parser");
        return modelAndView;
    }

    @RequestMapping(value = "/parse/message", method = RequestMethod.POST)
    public ModelAndView parseMessage(@RequestParam(value = "message") String message, ModelAndView modelAndView) {
        modelAndView.setViewName("message-parser");

        if (StringUtils.isEmpty(message)) {
            modelAndView.addObject("message", "");
            modelAndView.addObject("srcmessage", "");
            return modelAndView;
        }

        LOG.info(message);
        List<Layout> fields = messageFactory.parse(message);
        modelAndView.addObject("message", message);
        modelAndView.addObject("srcmessage", message);
        modelAndView.addObject("list", fields);

        if (fields.size() !=0) {
            Layout layout = fields.remove(0);
            modelAndView.addObject("messageType", layout.getType());
            modelAndView.addObject("messageDesc", layout.getDesc());
            modelAndView.addObject("messageLength", layout.getLength());
        } else {

        }
        return modelAndView;
    }



    private int length(String message) {
        return message.getBytes().length;
    }
}
