package com.GroupChat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ����ͻ��˵���·ģ�� 1.ʵ�ֲ��Ͻ��ܷ���˵����� 2.��Ҫ�������Ϣ�������ͻ���
 */
public class ClientThread extends Thread {
	private BufferedReader br;
	private PrintWriter pw;
	private ClientWindow cw;

	public ClientThread(Socket s, ClientWindow cw) throws IOException {
		this.cw = cw;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pw = new PrintWriter(s.getOutputStream(), true);
		System.out.println("�ͻ���:��ȡ���������������");
	}

	/* �ͻ�������������͵����� */
	public void send(String str) {
		if (str.contains("#exit#")) {
			pw.println(str);
		} else {
			SimpleDateFormat format = new SimpleDateFormat("[MM/dd  HH:mm:ss]");
			pw.println(cw.getName() + "˵:" + str + "\t" + format.format(new Date()));
		}
		System.out.println("�����˷������ݣ�" + str);
	}

	public void run() {
		try {
			String str = null;
			while (true) {
				if ((str = br.readLine()) != null) {
					System.out.println("�ͻ��ˣ��յ��������Ĺ㲥��Ϣ" + str);
					cw.getTa().append(str + "\r\n");
					System.out.println("�ͻ��ˣ��㲥��Ϣ�Ѵ�ӡ");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
