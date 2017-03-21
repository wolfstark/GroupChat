package com.GroupChat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 群聊服务端的主程序，用于不断接收连接过来的Socket 并同时创建一个Thread通过Socket与客户端建立连接;
 * 实现能够同时多个线程与客户端进行通信;
 */
public class GroupChatServer {
	public static void main(String[] args) {
		try {
			new GroupChatServer().GroupChatStart();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务器启动后不断监听端口，当有Socket尝试连接时，创建对方的Socket进行链接，同时记录对客户端的Socket放在List集合中用于广播信息
	 * 并将该通信链交予服务端多线程处理，主程序继续等待客户端连接
	 */
	public void GroupChatStart() throws IOException {
		ArrayList<Socket> l = new ArrayList<Socket>();// 存储所有的Socket集合用于群发
		ServerSocket ss = new ServerSocket(10086);
		for (int count = 1;; count++) {
			Socket s = ss.accept();
			l.add(s);
			new ServerThread(s, l).start();// 每当一个Socket尝试连接服务器就创建一个服务线程与客户端连接
			System.out.println("成功创建一个服务端线程,当前总连接数" + count);
		}
	}
}
