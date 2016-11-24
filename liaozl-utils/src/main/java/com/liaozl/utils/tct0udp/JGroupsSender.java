package com.liaozl.photo.util.tct0udp;

import org.jgroups.Address;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;

/**
 * 利用JGroups发送消息
 * @author liaozuliang
 * @date 2015年8月14日
 */
public class JGroupsSender {

	private JChannel channel;
	public static final String groupName = "test";
	
	public void start() {
		try {
			if (channel == null) {
				channel = new JChannel();
			}
			channel.connect(groupName);
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) {
		if(channel==null){
			start();
		}
		
		try {
			Address dest = null;//广播，任何人都可以接收到
			
			Message message = new Message(dest, channel.getLocalAddress(), msg.getBytes("UTF-8"));
			channel.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if (channel == null) {
			channel.close();
		}
	}
	
	public static void main(String[] args) {
		JGroupsSender sender = new JGroupsSender();
		sender.start();
		sender.sendMsg("测试是一下");
		sender.close();
	}

}
