package kr.co.finger.damoa.commons.concurrent;

import java.util.List;

/**
 * Aggregator 를 일부 구현한 클래스.
 * 적당한 크기와 적당한 시간에 모인 데이터를 aggregate 한다.
 * @param <T>
 */
public interface AggregateQueue<T> {
    /**
     * 큐에 입력한다.
     * @param o
     * @throws InterruptedException
     */
    public void put(T o) throws InterruptedException;

    /**
     * 큐에서 추출한다.
     * @return
     * @throws InterruptedException
     */
    public List<T> take() throws InterruptedException;

    public int size();
}
