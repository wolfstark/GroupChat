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
 * ����˵Ķ��̳߳�����Ҫ����ʵ�ַ���˶Կͻ��˵�I/O������ 1.ʵ�ֲ��Ͻ��ܿͻ��˷��͵�Ⱥ����Ϣ�� 2.���ͻ��˷��͵�Ⱥ����Ϣת����ÿһ�����ߵĿͻ��ˣ�
 * 3.�ж���ͻ��˵������ں�ʱ�ر�
 */
public class ServerThread extends Thread {
	private Socket s;
	private ArrayList<Socket> l;
	private boolean control;
	private String str;

	public void run() {// ----------------��Ҫʵ�ֲ��������ͻ��˷��͵���Ϣ--------------------
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (control) {
				if ((str = br.readLine()) != null) {// ��read��������ֱ���ͻ���Socket������Ϣ
					clientClose();
					System.out.println("�յ��ͻ��˷��͵����ݣ�" + str + "׼����ʼ�㲥");
					broadCast(); // ���ù㲥��������ȡ�������ݷ�����ÿ���ͻ���
				}
			}
		} catch (SocketException se) {
			System.out.println(se.getMessage() + "�Ͽ���һ���ͻ��˻Ự");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** ÿ�ν��յ���Ϣͨ������Socket����ʵ�ֹ㲥 */

	public void broadCast() throws IOException {// �㲥Socket��ͨ���������Ͻ���ȡ����Ϣ�����ÿһ��Socket
		Iterator<Socket> i = l.iterator();
		Socket sockets = null;
		while (i.hasNext()) {
			sockets = i.next();
			PrintWriter pw = new PrintWriter(sockets.getOutputStream(), true);// Ϊ���ߵĿͻ��˽��������
			pw.println(str);
			System.err.println("��ʼ�㲥��Ϣ��" + str + "��");
		}
	}

	/** ���ڹ����пͻ��˷������˳�����������Ӧ���� */
	public void clientClose() throws IOException {
		if (str.contains("#exit#")) {
			System.out.println("�յ�һ���˳�Ⱥ�ĵ�����");
			str = "          " + str.substring(6, str.length()) + "�˳���Ⱥ���ң�";
			s.close();// �ر�Socket��I/O����Դ
			l.remove(s);// �ӹ㲥�б���ɾ��Socket��Ϣ
			control = false;// �ж��߳�
		}
	}
	
	public ServerThread(Socket s, ArrayList<Socket> l) {// ��ʼ�������ȡ�����Ŀͻ���Socket���ϣ���ͨ�ŵĿͻ���
		this.s = s;
		this.l = l;
		control = true;
	}
}
