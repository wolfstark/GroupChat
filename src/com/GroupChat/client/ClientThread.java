package com.GroupChat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 聊天客户端的网路模块 1.实现不断接受服务端的数据 2.将要输入的信息发送至客户端
 */
public class ClientThread extends Thread {
	private BufferedReader br;
	private PrintWriter pw;
	private ClientWindow cw;

	public ClientThread(Socket s, ClientWindow cw) throws IOException {
		this.cw = cw;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pw = new PrintWriter(s.getOutputStream(), true);
		System.out.println("客户端:已取得与服务器的连接");
	}

	/* 客户端向服务器发送的数据 */
	public void send(String str) {
		if (str.contains("#exit#")) {
			pw.println(str);
		} else {
			SimpleDateFormat format = new SimpleDateFormat("[MM/dd  HH:mm:ss]");
			pw.println(cw.getName() + "说:" + str + "\t" + format.format(new Date()));
		}
		System.out.println("向服务端发送数据：" + str);
	}

	public void run() {
		try {
			String str = null;
			while (true) {
				if ((str = br.readLine()) != null) {
					System.out.println("客户端：收到服务器的广播信息" + str);
					cw.getTa().append(str + "\r\n");
					System.out.println("客户端：广播信息已打印");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
