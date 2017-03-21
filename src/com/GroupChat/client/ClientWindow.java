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
 * 聊天客户端的图形界面 1.创建客户端图形界面； 2.捕捉发送按钮的事件并处理； 2.捕捉关闭按钮的事件并处理；
 */
@SuppressWarnings("serial")
public class ClientWindow extends JFrame implements ActionListener {
	private ClientThread ck;
	private Container c;
	private JTextField tf;
	private JTextArea ta;
	private String name;

	/* 绑定南区按钮的监听器，当捕捉事件时发送输入框的数据至网络模块 */
	public void actionPerformed(ActionEvent e) {
		if (tf.getText().equals("")) {
			JOptionPane.showMessageDialog(c, "内容为空!");
		} else {
			String str = tf.getText();
			System.out.println("客户端:准备发送数据" + str);
			tf.setText("");
			ck.send(str);
		}
	}

	/**
	 * 构造方法，用于加载客户端界面的主方法，在创建客户端的同时会调用网络模块
	 */
	public ClientWindow() throws HeadlessException, UnknownHostException, IOException {
		super("群聊室");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		String address = JOptionPane.showInputDialog(c, "请输入聊天服务器的地址");
		name = JOptionPane.showInputDialog(c, "请输入聊天昵称");
		Socket s = new Socket(address, 10086);//创建通信链
		ck = new ClientThread(s, this);//将通信链交与网络模块处理
		ck.start();
		JScrollPane ta = contentArea();//南区
		JPanel p = outputArea(name);//北区
		c = getContentPane();
		c.add(ta, BorderLayout.CENTER);
		c.add(p, BorderLayout.SOUTH);
		pack();
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			/** 绑定窗口关闭按钮的监听器，当捕捉事件时发送关闭请求 */
			public void windowClosing(WindowEvent e) {
				int select = JOptionPane.showConfirmDialog(ClientWindow.this, "确认关闭吗？", "注意！",
						JOptionPane.YES_NO_OPTION);
				if (select == JOptionPane.YES_OPTION) {
					ck.send("#exit#" + name);
					System.out.println("发送关闭请求");
					dispose();
					System.exit(0);
				}
			}
		});
	}

	/** 客户端界面北区的内容，会返回一个带滚动条的文本域 */
	public JScrollPane contentArea() {
		ta = new JTextArea("\t<<<<<<欢迎进入聊天室>>>>>>", 10, 30);
		ta.setFont(new Font("Microsoft", ABORT, 30));
		ta.setLineWrap(true);
		JScrollPane sp = new JScrollPane(ta);
		return sp;
	}

	/** 客户端南区的容器，包含输入框和发送按钮 */
	public JPanel outputArea(String name) {
		JPanel p = new JPanel();
		tf = new JTextField(20);
		JButton b = new JButton("发送");
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
