package solo.william.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/20 18:24
 **/
public class Videoconference implements  Runnable{

    private final CountDownLatch controller;

    public Videoconference(int number){
        controller = new CountDownLatch(number);
    }

    public void arrive(String name){
        System.out.printf("%s has arrived.\n",name);
        controller.countDown();
        System.out.printf("VideoConference: Waiting for %d participants.\n",controller.getCount());
    }

    @Override
    public void run() {
        System.out.printf("VideoConference: Initialization:%d participants.\n",controller.getCount());
        try{
            controller.await();
            System.out.printf("VideoConference:All the participants.\n",controller.getCount());
        }catch(InterruptedException ex){
            System.out.printf(ex.getLocalizedMessage());
        }
    }
}
