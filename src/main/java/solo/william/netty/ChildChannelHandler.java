package solo.william.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/12/5 11:43
 **/
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new CounterTimeServerHandler());

    }
}
