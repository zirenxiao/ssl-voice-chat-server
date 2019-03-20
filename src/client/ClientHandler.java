package client;

import java.util.Base64;

import javax.sound.sampled.LineUnavailableException;

import org.json.simple.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import utility.ExceptionHandler;
import utility.Player;
import utility.Settings;


/**
 * Handles a client-side channel.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
	
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg){
//        System.err.println(msg);
    	JSONObject content = Settings.JSONcheck(msg);
        if (content!=null) {
			String stringContent = content.get(Settings.COMMAND).toString();
			if (stringContent.equals(Settings.TYPEAUDIO)) {
				// if it is audio message
				this.audioAction(content);
			}else if (stringContent.equals(Settings.TYPESERVERMSG)) {
				this.serverMessageAction(content);
			}
		}
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
    	
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
    	
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    public static void play(byte buf[]){
		Player p;
		try {
			p = new Player(buf, Settings.LENGTHOFAUDIO);
			new Thread(p).start();
		} catch (LineUnavailableException e) {
			ExceptionHandler.lineUnavailable();
		}
	}
    
    private void audioAction(JSONObject content){
		byte[] m = Base64.getDecoder().decode(content.get(Settings.AUDIO).toString());
		play(m);
    }
    
    private void serverMessageAction(JSONObject content) {
    	Settings.clientPrintOnGUI("[server] " + content.get(Settings.MESSAGE).toString());
    }
    
}
