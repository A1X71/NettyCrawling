package solo.william.aio;

import solo.william.nio.TimeClientHandle;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/20 8:03
 **/
public class TimeClient {
    public static void main(String[] args) throws Exception{
        int port = 8080;
        if(args != null && args.length >0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                //采用默认值
            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1",port),"AIO-AsyncTimeClientHandler-001").start();

        Thread.sleep(10000);
    }
}
