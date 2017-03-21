package com.GroupChat.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * ����ͻ��˵�ͼ�ν��� 1.�����ͻ���ͼ�ν��棻 2.��׽���Ͱ�ť���¼������� 2.��׽�رհ�ť���¼�������
 */
@SuppressWarnings("serial")
public class ClientWindow extends JFrame implements ActionListener {
	private ClientThread ck;
	private Container c;
	private JTextField tf;
	private JTextArea ta;
	private String name;

	/* ��������ť�ļ�����������׽�¼�ʱ��������������������ģ�� */
	public void actionPerformed(ActionEvent e) {
		if (tf.getText().equals("")) {
			JOptionPane.showMessageDialog(c, "����Ϊ��!");
		} else {
			String str = tf.getText();
			System.out.println("�ͻ���:׼����������" + str);
			tf.setText("");
			ck.send(str);
		}
	}

	/**
	 * ���췽�������ڼ��ؿͻ��˽�������������ڴ����ͻ��˵�ͬʱ���������ģ��
	 */
	public ClientWindow() throws HeadlessException, UnknownHostException, IOException {
		super("Ⱥ����");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		String address = JOptionPane.showInputDialog(c, "����������������ĵ�ַ");
		name = JOptionPane.showInputDialog(c, "�����������ǳ�");
		Socket s = new Socket(address, 10086);//����ͨ����
		ck = new ClientThread(s, this);//��ͨ������������ģ�鴦��
		ck.start();
		JScrollPane ta = contentArea();//����
		JPanel p = outputArea(name);//����
		c = getContentPane();
		c.add(ta, BorderLayout.CENTER);
		c.add(p, BorderLayout.SOUTH);
		pack();
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			/** �󶨴��ڹرհ�ť�ļ�����������׽�¼�ʱ���͹ر����� */
			public void windowClosing(WindowEvent e) {
				int select = JOptionPane.showConfirmDialog(ClientWindow.this, "ȷ�Ϲر���", "ע�⣡",
						JOptionPane.YES_NO_OPTION);
				if (select == JOptionPane.YES_OPTION) {
					ck.send("#exit#" + name);
					System.out.println("���͹ر�����");
					dispose();
					System.exit(0);
				}
			}
		});
	}

	/** �ͻ��˽��汱�������ݣ��᷵��һ�������������ı��� */
	public JScrollPane contentArea() {
		ta = new JTextArea("\t<<<<<<��ӭ����������>>>>>>", 10, 30);
		ta.setFont(new Font("Microsoft", ABORT, 30));
		ta.setLineWrap(true);
		JScrollPane sp = new JScrollPane(ta);
		return sp;
	}

	/** �ͻ������������������������ͷ��Ͱ�ť */
	public JPanel outputArea(String name) {
		JPanel p = new JPanel();
		tf = new JTextField(20);
		JButton b = new JButton("����");
		JLabel jl = new JLabel(name);
		p.add(jl);
		p.add(tf);
		p.add(b);
		tf.addActionListener(this);
		b.addActionListener(this);
		return p;
	}

	public JTextArea getTa() {
		return ta;
	}

	public String getName() {
		return name;
	}

	public static void main(String[] args) {
		try {
			new ClientWindow();
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
