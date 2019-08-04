/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package server;

import java.net.InetAddress;

import org.json.simple.JSONObject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import utility.Settings;

/**
 * Handles a server-side channel.
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        ctx.writeAndFlush(
                        		ctServerMessage("Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!")+ "\n");
                        ctx.writeAndFlush(
                        		ctServerMessage("Your session is protected by " +
                                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                                        " cipher suite.") + "\n");
                        channels.add(ctx.channel());
                        sendToAll(ctx, ctServerMessage("New connection ["+ctx.channel().id()+"]. Total Connections: " + channels.size() + "."), ctServerMessage("Total Connctions:" + channels.size()));
                    }
        });
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // Send the received message to all channels but the current one.
//    	System.err.println(msg);
    	
    	JSONObject content = Settings.JSONcheck(msg);
        if (content!=null) {
			String stringContent = content.get(Settings.COMMAND).toString();
			if (stringContent.equals(Settings.TYPEAUDIO)) {
				// if it is audio message
				this.sendToAll(ctx, msg, null);
			}else if (stringContent.equals(Settings.TYPECLIENTMSG)) {
				
			}
		}
    	
    	
        

        // Close the connection if the client has sent 'bye'.
//        if ("bye".equals(msg.toLowerCase())) {
//            ctx.close();
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    @SuppressWarnings("unchecked")
	private String ctServerMessage(String msg) {
		JSONObject obj = new JSONObject();
		obj.put(Settings.MESSAGE, msg);
		obj.put(Settings.COMMAND, Settings.TYPESERVERMSG);
		return obj.toJSONString();
	}
    
    private void sendToAll(ChannelHandlerContext ctx, String msgToOthers, String msgToEcho) {
    	for (Channel c: channels) {
            if (c != ctx.channel()) {
//                c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
                c.writeAndFlush(msgToOthers + '\n');
            } else {
//                c.writeAndFlush("[you] " + msg + '\n');
            	if (msgToEcho!=null) {
            		c.writeAndFlush(msgToEcho + '\n');
            	}
            	
            }
        }
    }
}
