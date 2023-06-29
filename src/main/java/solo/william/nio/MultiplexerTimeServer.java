package solo.william.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
 * @Date 2022/11/18 13:20
 **/
public class MultiplexerTimeServer implements  Runnable{
    private Selector selector;
    private ServerSocketChannel servChannel;
    private volatile boolean stop;
    public MultiplexerTimeServer(int port){
        try{
            //多路复用工作对象
            selector = Selector.open();
            //所有客户端连接的父管道
            servChannel = ServerSocketChannel.open();
            //对Channel的TCP参数进行配置
            servChannel.configureBlocking(false);
            //backlog设置为1024
            servChannel.socket().bind(new InetSocketAddress(port),1024);
            //告诉selector监听select事件，监听SelectionKey.OP_ACCEPT操作位
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port:" + port);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void stop(){
        this.stop  = true;
    }
    @Override
    public void run() {
        while(!stop){
            try{
                //休眠时间为1秒，无论是否有读写发生，selector每隔1s就被唤醒一次
                selector.select(1000);
                //当有就绪的Channel时，selector返回该Channel的SelectionKey集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                //对处于就绪状态的Channel集合进行迭代，进行网络的异步读写操作
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    try{
                        handleInput(key);
                    }catch(Exception e){
                        if(key != null ){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
        if(selector !=null ){
            try{
                selector.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    private void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            //判断“操作位”，获得网络事件的类型
            if(key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //监听到有新的客户端接入，处理新的接入请求，完成TCP三次握手，建立物理链路
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                //Read the data
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                //对三种情况进行判断；>0读到字节并解码；=0未读到字节，关闭；<0链路已关闭，需关闭SocketChannel,并释放资源
                if(readBytes >0){
                    //将缓冲区当前的limit设置为position,position设置为0，用于后续对缓冲区的读写操作
                    readBuffer.flip();
                    //根据缓冲区可读字节个数创建字节数组
                    byte[] bytes = new byte[readBuffer.remaining()];
                    //利用get（）将缓冲区的可读字节复制到新建的字节数组中。
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time server receive order "+ body);
                    String currentTime = "Query TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
                    doWrite(sc,currentTime);
                }else if(readBytes <0){
                    key.cancel();
                    sc.close();
                }else{
                    // zero bytes
                }
            }
        }
    }
    public void doWrite(SocketChannel channel,String response)throws IOException{
        if(response!=null && response.trim().length()>0){
            byte[] bytes= response.getBytes(StandardCharsets.UTF_8);
            ByteBuffer writeBuffer  = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
