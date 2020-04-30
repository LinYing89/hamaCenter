package com.bairock.iot.hamaServer.communication;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class PadServer {
	public static int PORT = 4045;

	private ServerBootstrap b;
	private ChannelFuture f;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public void run() throws Exception {
		bossGroup = new NioEventLoopGroup(); // (1)
		workerGroup = new NioEventLoopGroup();
		try {
			b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline ph = ch.pipeline();        
							// 以("\n")为结尾分割的 解码器        
							ph.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));        
							// 解码和编码，应和客户端一致       
							ph.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));        
							ph.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));   
							ph.addLast(new IdleStateHandler(90, -1, -1));
							ph.addLast(new PadHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			f = b.bind(PORT); // (7)
			// f.channel().closeFuture().sync();
			f.channel().closeFuture();
		} finally {
			// workerGroup.shutdownGracefully();
			// bossGroup.shutdownGracefully();
		}
	}

	public void close() {
		
		for(Channel cg : PadChannelBridge.channelGroup) {
			cg.close().syncUninterruptibly();
		}
		if (null != bossGroup) {
			bossGroup.shutdownGracefully().syncUninterruptibly();
		}
		if (null != workerGroup) {
			workerGroup.shutdownGracefully().syncUninterruptibly();
		}
	}
}
