package solo.william.bio;

/**
 * ----------------------------------------------
 * <p>
 *      2.1.2 同步阻塞式I／O创建的 TimeServer 源码分析代码清单2-1 同步阻塞I／O的TimeServer
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/14 13:33
 **/


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            ServerSocket server = null;
            try {
                server = new ServerSocket(port);
                System.out.println("The time server is start in port :" + port);
                Socket socket = null;
                while (true) {
                    System.out.println("运行中");
                    socket = server.accept();
                    new Thread(new TimeServerHandler(socket)).start();
                }
            } finally {
                if(server!=null){
                    System.out.println("The time server close");
                    server.close();
                    server = null;
                }
            }
        }
    }
}
