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

            Settings.clientPrintOnGUI("Connecting.....");
            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();
            

            Settings.clientPrintOnGUI("Connected to [" + Settings.SERVER_ADDRESS + "]!");
            cui.init(ch);
//            action(ch);
        } catch (Exception e) {
        	Settings.clientPrintOnGUI("Error!");
        } finally {
            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
        }
    }
}
