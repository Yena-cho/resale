package co.finger.socket.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 메인 로직
 * 
 * @author wisehouse@finger.co.kr
 */
@Component
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
//    
////    @Autowired
//    private BlockingQueue<String> rx1;
//    private BlockingQueue<String> rx2;
//    private BlockingQueue<String> rx3;
//    private BlockingQueue<String> rx4;
//    
//    private BlockingQueue<String> tx1;
//    private BlockingQueue<String> tx2;
//    private BlockingQueue<String> tx3;
//    private BlockingQueue<String> tx4;
//    
//    public void observeRx1() {
//        for(;;) {
//            try {
//                String message = rx1.take();
//                tx4.add(message);
//            } catch (Throwable t) {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.warn(t.getMessage(), t);
//                } else {
//                    LOGGER.warn(t.getMessage());
//                }
//                
//            }
//        }
//    }
//    
//    public void observeRx2() {
//        for(;;) {
//            try {
//                String message = rx2.take();
//                tx4.add(message);
//            } catch (Throwable t) {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.warn(t.getMessage(), t);
//                } else {
//                    LOGGER.warn(t.getMessage());
//                }
//                
//            }
//        }
//    }
//    
//    public void observeRx3() {
//        for(;;) {
//            try {
//                String message = rx3.take();
//                tx4.add(message);
//            } catch (Throwable t) {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.warn(t.getMessage(), t);
//                } else {
//                    LOGGER.warn(t.getMessage());
//                }
//                
//            }
//        }
//    }
//    
//    public void observeRx4() {
//        for(;;) {
//            try {
//                String message = rx4.take();
//                SocketRequest request = new SocketRequestParser().parse(message);
//                
//                tx4.add(message);
//            } catch (Throwable t) {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.warn(t.getMessage(), t);
//                } else {
//                    LOGGER.warn(t.getMessage());
//                }
//                
//            }
//        }
//    }
//    
}
