package secondedition;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 聊天视图
 * @author jinpf
 *
 */
public class ChatFrame {
	private JFrame Cframe=null;
	private JButton SendMB=new JButton("发 送");
	private JButton SendFB=new JButton("传文件");
	private JTextArea ShowT=new JTextArea();
	private JTextArea SendT=new JTextArea();
	private JScrollPane ShowScr=new JScrollPane(ShowT,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane SendScr=new JScrollPane(SendT,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private Socket Listener=null;
	private String Name=null;
	private Boolean Visible;
	private Thread ListenT;
	/**
	 * 构造聊天视图
	 * @param socket
	 * 传递通信用的Socket，可以是ServerSocket接收产生的，也可以是通过点击某一项设定的
	 * @param name
	 * 通信对方的昵称
	 * @param visible
	 */
	public ChatFrame(Socket socket,String name,Boolean visible) {
		Listener=socket;
		Name=name;
		Visible=visible;
		
		Cframe=new JFrame(Name);
		Font fnt = new Font("微软雅黑",Font.PLAIN +Font.BOLD,12);
		Cframe.setFont(fnt);
		SendMB.setFont(fnt);
		SendFB.setFont(fnt);
		ShowT.setFont(fnt);
		SendT.setFont(fnt);
		
		ShowT.setLineWrap(true);//设置自动换行
		ShowT.setWrapStyleWord(true);
		SendT.setLineWrap(true);//设置自动换行
		SendT.setWrapStyleWord(true);
		
		
		ShowScr.setBounds(25, 20, 300, 150);
		SendScr.setBounds(25, 190, 300, 70);
		SendMB.setBounds(150, 275, 70, 30);
		SendFB.setBounds(250, 275, 70, 30);
		
		ShowT.setEditable(false);
		Cframe.setLayout(null);
		Cframe.add(ShowScr);
		Cframe.add(SendScr);
		Cframe.add(SendMB);
		Cframe.add(SendFB);
		Cframe.setResizable(false);//不能最大化
		Cframe.setSize(360, 350);
		Cframe.setLocationRelativeTo(null);//在屏幕中央
		Cframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//点击关闭按钮隐藏并释放窗体
		Cframe.setVisible(visible);
		
		ListenT=new Thread(
				new Runnable(){
					public void run(){
						try {
							DataInputStream in=new DataInputStream(Listener.getInputStream());
							while(true){
								String str;
								try {
									str = in.readUTF();
									if(!Cframe.isVisible())
										Cframe.setVisible(true);
									ShowT.append(Name+":"+str+"\n");
								} catch (IOException e) {
									if(e.getMessage()==null){
										ShowT.append("对方断开连接\n");
										
									}else{
										ShowT.append("接收消息失败\n");
									}
									break;
								}
								
								//get InfoT to auto show the last line
								ShowT.selectAll();
								ShowT.setCaretPosition(ShowT.getSelectedText().length());
								ShowT.requestFocus();
							}
						}catch(Exception e){
							
						}
					}
				}
				);
		ListenT.start();
		
		SendMB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==SendMB){
							try {
								DataOutputStream out=new DataOutputStream(Listener.getOutputStream());
								out.writeUTF(SendT.getText());
								ShowT.append("我:"+SendT.getText()+"\n");
								SendT.setText("");
							} catch (IOException e1) {
								ShowT.append("发送失败\n");
							}
							
							//get InfoT to auto show the last line
							ShowT.selectAll();
							ShowT.setCaretPosition(ShowT.getSelectedText().length());
							ShowT.requestFocus();
						}
					}
				}
				);
		
	}
	
	
}
