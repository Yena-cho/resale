package kr.co.finger.damoa.shinhan.agent.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * TODO
 * 두가지를 체크할 것이다.
 * 서버모듈에서 ReaderIdle이 발생하면 WARNING을 던짐.
 * 클라이언트모듈에서 ObjectPool의 크기가 1이 아니면 WARNING을 던짐.
 */
@Component
public class DamoaHealth implements HealthIndicator {
    private boolean isOk = true;
    @Override
    public Health health() {
        if (isOk) {
            return Health.up().build();
        } else {
            //TODO
            return Health.down().withDetail("reason", "신한가상계좌서버에 접속할 수 없음.").build();
        }
    }
}
