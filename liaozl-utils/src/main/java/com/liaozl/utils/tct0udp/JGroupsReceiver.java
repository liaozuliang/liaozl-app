package com.liaozl.utils.tct0udp;

import java.io.UnsupportedEncodingException;

import com.liaozl.photo.util.tct0udp.JGroupsSender;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.ReceiverAdapter;

/**
 * 利用JGroups接收消息
 * 
 * @author liaozuliang
 * @date 2015年8月14日
 */
public class JGroupsReceiver {

	private JChannel channel;

	public void start() {
		try {
			if (channel == null) {
				channel = new JChannel();
			}

			Receiver rc = new StrMsgReceiver();
			channel.setReceiver(rc);
			channel.connect(JGroupsSender.groupName);
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}

	public class StrMsgReceiver extends ReceiverAdapter {

		@Override
		public void receive(Message msg) {
			try {
				String msgStr = new String(msg.getBuffer(), "UTF-8");
				System.out.println("----接收到消息：" + msgStr);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		JGroupsReceiver receiver = new JGroupsReceiver();
		receiver.start();
	}
}
