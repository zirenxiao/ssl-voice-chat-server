package client;

import java.util.Base64;

import javax.sound.sampled.LineUnavailableException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        
        
        JSONObject content;
		JSONParser parser = new JSONParser();
		
		try {
			content = (JSONObject) parser.parse(msg);
			byte[] m = Base64.getDecoder().decode(content.get(Settings.AUDIO).toString());
			play(m);
		} catch (ParseException e) {
			ExceptionHandler.parse();
		} catch (ClassCastException exc) {
			ExceptionHandler.classCast();
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
    
}
