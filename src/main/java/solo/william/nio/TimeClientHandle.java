package solo.william.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/18 16:22
 **/
public class TimeClientHandle implements  Runnable{
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;
    public TimeClientHandle(String host,int port){
        this.host = host == null ? "127.0.0.1":host;
        this.port = port;
        try{
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    public void run() {
            try{
                doConnect();
            }catch(IOException e){
                e.printStackTrace();
                System.exit(1);
            }
            while(!stop){
                try{
                    selector.select(1000);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey>  it = selectionKeys.iterator();
                    SelectionKey key = null;
                    while(it.hasNext()){
                        key = it.next();
                        it.remove();
                        try{
                            handleInput(key);
                        }catch(Exception e){
                            if(key != null){
                                key.cancel();
                                if(key.channel() != null ){
                                    key.channel().close();
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            if(selector !=null){
                try{
                    selector.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
    }

    private void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            //判断是否连接成功
            SocketChannel sc = (SocketChannel) key.channel();
            if(key.isConnectable()){//处于连接状态 ，服务器已经返回syn-ack消息
                if(sc.finishConnect()){//判断连接结果，是否连接成功
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }
            }else{
                System.out.println("forced to exit");
                // 经测试，在这里加exit()是不合适的，未接到返回就退出了，去掉后可以接到服务器写回的数据
             //   System.exit(1);
            }
            if(key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("Now is :" + body);
                    this.stop = true;
                } else if (readBytes < 0) {
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                } else
                    ; //读0字节，忽略
            }
        }
    }
    private void doConnect() throws IOException {
        //如果直接连接成功，则注册到多路复用器上，发送请求消处，读应答
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else{// 直连未成功，服务器未返回TCP握手应答消息，不代表连接失败，将sc注册到selector上，并注册OP_CONNECT
            //当服务端返回TCP syn-ack消息后，selector就能轮询到SocketChannel处于连接就绪状态
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }
    private void doWrite(SocketChannel sc) throws IOException{
        byte[] req = "QUERY TIME ORDER".getBytes(StandardCharsets.UTF_8);
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }
    }
}
