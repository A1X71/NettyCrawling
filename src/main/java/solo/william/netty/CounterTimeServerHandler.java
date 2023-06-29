package solo.william.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * ----------------------------------------------
 * 增加计数器测试粘包现象
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/12/5 16:30
 **/
public class CounterTimeServerHandler extends ChannelHandlerAdapter {
    private int counter;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8").substring(0,req.length-System.getProperty("line.separator").length());
        System.out.println("The time server receive order : " + body + " ; counter is : " + ++counter);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(System.currentTimeMillis()).toString(): "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(resp);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
    }
}

