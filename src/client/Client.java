package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import utility.Settings;



/**
 * Simple SSL chat client modified from {@link TelnetClient}.
 */
public final class Client {

    static final String HOST = System.getProperty("host", Settings.SERVER_ADDRESS);
    static final int PORT = Integer.parseInt(System.getProperty("port", Settings.OUT_PORT));
    static EventLoopGroup group = new NioEventLoopGroup();

    public static void main(String[] args) throws Exception {
//        EventLoopGroup group = new NioEventLoopGroup();
    	ClientUserInterface cui = new ClientUserInterface();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ClientInitializer());

            Settings.clientPrintOnGUI("开始连接.....");
            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();
            

            Settings.clientPrintOnGUI("连接服务器[" + Settings.SERVER_ADDRESS + "]成功!");
            cui.init(ch);
//            action(ch);
        } catch (Exception e) {
        	Settings.clientPrintOnGUI("连接出现错误！");
        } finally {
            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
        }
    }

//    private static void action(Channel ch){
//    	// Read commands from the stdin.
//        ChannelFuture lastWriteFuture = null;
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            String line;
//			try {
//				line = in.readLine();
//				if (line == null) {
//	                break;
//	            }
//	            // Sends the received line to the server.
//	            lastWriteFuture = ch.writeAndFlush(line + "\r\n");
//
//	            // If user typed the 'bye' command, wait until the server closes
//	            // the connection.
//	            if ("bye".equals(line.toLowerCase())) {
//	                ch.closeFuture().sync();
//	                break;
//	            }
//			} catch (IOException e) {
//				ExceptionHandler.io();
//			} catch (InterruptedException e) {
//				ExceptionHandler.interrupted();
//			}
//        }
//
//        // Wait until all messages are flushed before closing the channel.
//        if (lastWriteFuture != null) {
//            try {
//				lastWriteFuture.sync();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				ExceptionHandler.interrupted();
//			}
//        }
//    }
}
