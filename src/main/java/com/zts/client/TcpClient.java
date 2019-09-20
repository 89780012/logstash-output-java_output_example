package com.zts.client;

import com.zts.strategy.ExponentialBackOffRetry;
import com.zts.strategy.RetryPolicy;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TcpClient {

    private String host;
    private int port;
    private Bootstrap bootstrap;
    /** 重连策略 */
    private RetryPolicy retryPolicy;
    /** 将<code>Channel</code>保存起来, 可用于在其他非handler的地方发送数据 */
    private Channel channel;

    public TcpClient(String host, int port,int retry, Long retrytime) {
        this(host, port, new ExponentialBackOffRetry(retry, retrytime));
    }

    public TcpClient(String host, int port, RetryPolicy retryPolicy) {
        this.host = host;
        this.port = port;
        this.retryPolicy = retryPolicy;
        init();
    }

    /**
     * 向远程TCP服务器请求连接
     */
    public void connect() {
        synchronized (bootstrap) {
            ChannelFuture future = bootstrap.connect(host, port);
            future.addListener(getConnectionListener());
            this.channel = future.channel();
        }
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public Channel getChannel() {
        return this.channel;
    }

    private void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        // bootstrap 可重用, 只需在TcpClient实例化的时候初始化即可.
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)    //指定channel 类型
                .handler(new ClientHandlersInitializer(TcpClient.this)); //初始化器
    }

    private ChannelFutureListener getConnectionListener() {
        return new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        };
    }

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost", 26666,10,Long.valueOf(5000));
        tcpClient.connect();
    }

}