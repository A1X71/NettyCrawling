package solo.william.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/18 17:26
 **/
public class AsyncTimeServerHandler implements Runnable{
    private int port;
    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    public AsyncTimeServerHandler(int port){
        this.port = port;
        try{
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port:" + port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

            //完成一组正在执行的操作之前，允许当前的线程一直阻塞。
            //让线程在此阻塞， 防止服务端执行完成退出
            //实际项目中，不需要启动独立的线程来处理AsynchronousServerSocketChannel
            latch = new CountDownLatch(1);
            doAccept();
            try{
                synchronized (latch) {
                    latch.wait();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }

    }
    public void doAccept(){
        asynchronousServerSocketChannel.accept(this,
                new AcceptCompletionHandler());
    }
}
