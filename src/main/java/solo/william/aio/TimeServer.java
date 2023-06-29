package solo.william.aio;

import com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction;

import java.util.concurrent.CountDownLatch;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/18 17:25
 **/
public class TimeServer {
    public static void main(String[] args){
        int port = 8080;
       // CountDownLatch latch;
        if( args !=null && args.length >0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException ex){

            }
        }
       // latch = new CountDownLatch(1);
        AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(timeServerHandler,"AIO-AsyncTimeServerHandler-001").start();
      //  latch.wait();
//        try{
//            Thread.sleep(10000);
//        }catch(InterruptedException ex){
//            ex.printStackTrace();
//        }


    }
}
