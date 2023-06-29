package solo.william.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/20 19:52
 **/
public class Participant implements  Runnable{

    private Videoconference conferce;
    private String name;
    public Participant(Videoconference conference,String name){
        this.conferce = conference;
        this.name = name;
    }
    @Override
    public void run() {
        long duration = (long)(Math.random()*10);
        try{
            TimeUnit.SECONDS.sleep(duration);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        conferce.arrive(name);
    }
}
