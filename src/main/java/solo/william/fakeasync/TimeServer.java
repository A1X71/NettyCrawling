package solo.william.fakeasync;

import solo.william.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/16 16:42
 **/
public class TimeServer {
    public static void main(String[] args) throws IOException{
        int port = 8080;


        if(args != null && args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException ex){

            }
        }

        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            System.out.println("The time server is start in port:");
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(
                    50,1000
            );
            while(true){
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        }finally{
            if(server != null){
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
    public void example(int m ){};
    public int example(int m ,float f){return 0;};
}
