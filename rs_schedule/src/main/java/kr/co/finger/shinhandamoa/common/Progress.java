package kr.co.finger.shinhandamoa.common;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * 진행률
 * 
 * @author wisehouse@finger.co.kr
 */
public class Progress {
    private final long totalCount;
    private long current;
    
    private StopWatch stopWatch;

    public Progress(long totalCount) {
        this.totalCount = totalCount;
        this.stopWatch = StopWatch.createStarted();
    }
    
    public void tick() {
        current++;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getProgressCount() {
        return current;
    }
    
    public float getProgressRate() {
        return 1F * current / totalCount;
    }
    
    public long getTimeLeft() {
        if(current == 0) {
            return 0L;
        }
        
        long millis = stopWatch.getTime(TimeUnit.MILLISECONDS);
        long estimated = millis * totalCount / current;
        long left = estimated - millis;
        
        return left;
    }
}
