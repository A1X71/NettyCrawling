package solo.william.nio;

import java.io.IOException;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/18 13:18
 **/
public class TimeServer {
    public static void main(String[] args) throws IOException{
        int port = 8080;
        if( args !=null && args.length >0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException ex){

            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimerServer-001").start();
    }
}
