package com.zts.client;

import com.zts.util.EchoHandler;
import com.zts.util.ReconnectHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class ClientHandlersInitializer extends ChannelInitializer<SocketChannel> {

    private ReconnectHandler reconnectHandler;
    private EchoHandler echoHandler;

    public ClientHandlersInitializer(TcpClient tcpClient) {
        //Assert.assertNull("TcpClient can not be null.",tcpClient);
        this.reconnectHandler = new ReconnectHandler(tcpClient);
        this.echoHandler = new EchoHandler();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(this.reconnectHandler);
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new Pinger());
    }
}