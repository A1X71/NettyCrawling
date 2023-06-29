package solo.william.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/19 12:15
 **/
public class AcceptCompletionHandler  implements CompletionHandler<AsynchronousSocketChannel,AsyncTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
         attachment.asynchronousServerSocketChannel.accept(attachment,this);
         ByteBuffer buffer = ByteBuffer.allocate(1024);
         result.read(buffer,buffer,new ReadCompletionHandler(result));
         attachment.latch.countDown();
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
            attachment.latch.countDown();
    }
}
