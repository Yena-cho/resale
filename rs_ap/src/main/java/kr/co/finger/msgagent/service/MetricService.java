package kr.co.finger.msgagent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MetricService {
    @Autowired
    private GaugeService gaugeService;
    @Autowired
    private CounterService counterService;
    private ConcurrentMap<String, Integer> statusMap = new ConcurrentHashMap();

    private ConcurrentMap<String, Integer> TEMP = new ConcurrentHashMap<>();

    public void increaseCount(String name) {
        synchronized (statusMap) {
            Integer count = null;
            if (statusMap.containsKey(name)) {
                count = statusMap.getOrDefault(name, 0);
            } else {
                count = Integer.valueOf(0);
            }
            Integer result = count + 1;
            statusMap.put(name, result);
            counterService.increment("meter."+name);

            if (result % 1000 == 0) {
                System.out.println(result);
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    private void exportMetrics() {
        synchronized (statusMap) {
            Iterator<String> iterator = statusMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Integer count = statusMap.get(key);
                Integer previous = null;
                if (TEMP.containsKey(key)) {
                    previous = TEMP.get(key);
                } else {
                    previous = Integer.valueOf(0);
                }
                gaugeService.submit(key,(count-previous));
                TEMP.put(key, count);
            }
        }

    }

}
