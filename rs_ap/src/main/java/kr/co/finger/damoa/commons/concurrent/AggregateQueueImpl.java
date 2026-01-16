package kr.co.finger.damoa.commons.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Aggregator의 기능을 일부 구현한 클래스.
 * size 크기가 채워지면 모인 데이터를 추출할 수 있다.
 * size 크기만큼 채워지지 않더라도 timeout 만큼 시간을 기다려 모인 데이터를 추출한다.
 */
public class AggregateQueueImpl implements AggregateQueue {
    private int size = 10;
    private long timeout = 5000;

    private List<Object> objects = new ArrayList<>();

    public AggregateQueueImpl(int size, long timeout) {
        this.size = size;
        this.timeout = timeout;
    }

    /**
     * 데이터를 쌓는다.
     * 다만 size보다 크면 블록된다.
     *
     * @param o
     * @throws InterruptedException
     */
    @Override
    public synchronized void put(Object o) throws InterruptedException {
        boolean logging = true;
        while (objects.size() >= size) {
            if (logging == true) {
                System.out.println("PUTTING....");
                logging=false;
            }
            wait();

        }
        objects.add(o);
        notifyAll();
    }

    /**
     * @return
     * @throws InterruptedException
     */
    @Override
    public synchronized List<Object> take() throws InterruptedException {
        long finish = System.currentTimeMillis() + timeout;
        while (objects.size() < size) {
            long interval = finish - System.currentTimeMillis();
            if (interval > 0) {
                wait(interval);
            } else {
                break;
            }
        }

        if (objects.size() == 0) {
            notifyAll();
            return null;
        } else {
            List result = new ArrayList();
            result.addAll(objects);
            objects.clear();
            notifyAll();
            return result;
        }
    }

    @Override
    public synchronized int size() {
        return objects.size();
    }

    public static void main(String[] args) {
        final Random random = new Random(System.currentTimeMillis());
        final AggregateQueue aggregateQueue = new AggregateQueueImpl(1000, 2000);
        new Thread(new Runnable() {
            long index = 0;

            @Override
            public void run() {
                while (true) {
                    try {
                        aggregateQueue.put((++index) + "");
//                        System.out.println("PUT "+index);
                        Thread.sleep(1+random.nextInt(1));
                        if (index % 1000 == 0) {
                            System.out.println("PUT " + index);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            int count = 0;
            int index = 0;
            @Override
            public void run() {
                while (true) {
                    try {
                        List list = aggregateQueue.take();
                        int size = list.size();
                        count += size;
                        if (index++ % 10 == 0) {
                            System.out.println("TAKING ... "+count);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("QUEUE " + aggregateQueue.size());
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }


}
