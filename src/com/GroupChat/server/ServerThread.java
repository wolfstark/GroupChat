package com.GroupChat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 服务端的多线程程序，主要功能实现服务端对客户端的I/O流传输 1.实现不断接受客户端发送的群聊信息； 2.将客户端发送的群聊信息转发给每一个在线的客户端；
 * 3.判断与客户端的连接于何时关闭
 */
public class ServerThread extends Thread {
	private Socket s;
	private ArrayList<Socket> l;
	private boolean control;
	private String str;

	public void run() {// ----------------主要实现不断收听客户端发送的信息--------------------
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (control) {
				if ((str = br.readLine()) != null) {// 用read方法阻塞直至客户端Socket发送信息
					clientClose();
					System.out.println("收到客户端发送的数据：" + str + "准备开始广播");
					broadCast(); // 调用广播方法将读取到的数据发送至每个客户端
				}
			}
		} catch (SocketException se) {
			System.out.println(se.getMessage() + "断开了一个客户端会话");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 每次接收的信息通过遍历Socket集合实现广播 */

	public void broadCast() throws IOException {// 广播Socket，通过遍历集合将读取的信息输出至每一个Socket
		Iterator<Socket> i = l.iterator();
		Socket sockets = null;
		while (i.hasNext()) {
			sockets = i.next();
			PrintWriter pw = new PrintWriter(sockets.getOutputStream(), true);// 为在线的客户端建立输出流
			pw.println(str);
			System.err.println("开始广播信息‘" + str + "’");
		}
	}

	/** 用于过滤判客户端发来的退出请求并做出相应处理 */
	public void clientClose() throws IOException {
		if (str.contains("#exit#")) {
			System.out.println("收到一个退出群聊的请求");
			str = "          " + str.substring(6, str.length()) + "退出了群聊室！";
			s.close();// 关闭Socket及I/O流资源
			l.remove(s);// 从广播列表中删除Socket信息
			control = false;// 中断线程
		}
	}
	
	public ServerThread(Socket s, ArrayList<Socket> l) {// 初始化对象获取其他的客户端Socket集合，和通信的客户端
		this.s = s;
		this.l = l;
		control = true;
	}
}
