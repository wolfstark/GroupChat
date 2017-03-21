package com.GroupChat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Ⱥ�ķ���˵����������ڲ��Ͻ������ӹ�����Socket ��ͬʱ����һ��Threadͨ��Socket��ͻ��˽�������;
 * ʵ���ܹ�ͬʱ����߳���ͻ��˽���ͨ��;
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
	 * �����������󲻶ϼ����˿ڣ�����Socket��������ʱ�������Է���Socket�������ӣ�ͬʱ��¼�Կͻ��˵�Socket����List���������ڹ㲥��Ϣ
	 * ������ͨ�����������˶��̴߳�������������ȴ��ͻ�������
	 */
	public void GroupChatStart() throws IOException {
		ArrayList<Socket> l = new ArrayList<Socket>();// �洢���е�Socket��������Ⱥ��
		ServerSocket ss = new ServerSocket(10086);
		for (int count = 1;; count++) {
			Socket s = ss.accept();
			l.add(s);
			new ServerThread(s, l).start();// ÿ��һ��Socket�������ӷ������ʹ���һ�������߳���ͻ�������
			System.out.println("�ɹ�����һ��������߳�,��ǰ��������" + count);
		}
	}
}
