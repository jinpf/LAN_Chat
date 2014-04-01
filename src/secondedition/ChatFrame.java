package secondedition;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	//该线程接收和判断所有收到的信息
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
		Cframe.setVisible(Visible);
		
		ListenT=new Thread(
				new Runnable(){
					
					public void run(){
						try {
							DataInputStream in=new DataInputStream(Listener.getInputStream());
							DataOutputStream out=new DataOutputStream(Listener.getOutputStream());
							while(true){
								String str;
								try {
									str = in.readUTF();
									if(!Cframe.isVisible())
										Cframe.setVisible(true);
									String temp[]=str.split("   ");
									
									if(temp[0].equals("FileName:")){	//如果对方要传输文件
										
										SendMB.setEnabled(false);//防止因发送信息而导致出错
										
										int n = JOptionPane.showConfirmDialog(null, "是否接收"+Name+"传输的："+temp[1], "选择", JOptionPane.YES_NO_OPTION);
										if(n==0){	//如果接收文件
											File ReceiveF=null;
											JFileChooser fileChooser = new JFileChooser() ;
											fileChooser.setApproveButtonText("保存");
											fileChooser.setDialogTitle("请选择要保存的位置");
											fileChooser.setSelectedFile(new File(temp[1]));
											int result = fileChooser.showSaveDialog(Cframe);
											if(result==JFileChooser.APPROVE_OPTION){
												ReceiveF=fileChooser.getSelectedFile();
												for(int i=9000;i<9100;i++){
													try{
														ServerSocket FListener=new ServerSocket(i);
														//添加接收代码
														
														
														out.writeUTF("SendFile:   "+i);
														ShowT.append("选择接收"+temp[1]+"存放在: "+ReceiveF.getPath()+"\n");
														break;
													}catch(Exception E){	
													}
												}
											}else{
												JOptionPane.showMessageDialog(Cframe.getContentPane(),
													       "放弃接收该文件！", "注意！", JOptionPane.WARNING_MESSAGE);
												out.writeUTF("SendFile:   "+(-1));
												ShowT.append("拒绝接收"+temp[1]+"\n");
											}
											
										}else{	//如果拒绝接收文件
											out.writeUTF("SendFile:   "+(-1));
											ShowT.append("拒绝接收"+temp[1]+"\n");
										}
										
										SendMB.setEnabled(true);//防止因发送信息而导致出错
										
										
									}else if(temp[0].equals("SendFile:")){	//接收对方对自己传文件的选择
										int FC=Integer.parseInt(temp[1]);//选择是否接收文件
										if(FC==-1){
											ShowT.append("对方拒绝接收\n");
										}else{
											ShowT.append("对方接收地址"+Listener.getInetAddress().getHostAddress()+":"+FC+"\n");
										}

										
									}else{	//正常发送信息
										ShowT.append(Name+":"+str+"\n");
									}
										
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
		
		//注意！！！该按钮只是告诉对方自己要发文件，但真正是否要发送文件还需要ListenT线程进行判断
		SendFB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==SendFB){
							SendMB.setEnabled(false);
							
							File SendF=null;	//要发送的文件
							int result=0;	//文件选择结果
							JFileChooser fileChooser = new JFileChooser() ;
							fileChooser.setApproveButtonText("发送");
							fileChooser.setDialogTitle("请选择要发送的文件");
							result = fileChooser.showOpenDialog(Cframe);
							if(result==JFileChooser.APPROVE_OPTION){
								SendF=fileChooser.getSelectedFile();
								
								try {
									
									DataOutputStream out=new DataOutputStream(Listener.getOutputStream());
									DataInputStream in=new DataInputStream(Listener.getInputStream());
									out.writeUTF("FileName:   "+SendF.getName());
									ShowT.append("准备发送文件: "+SendF.getName()+" 等待对方接收\n");
									
								} catch (IOException e1) {
									ShowT.append("发送失败\n");
								}
								
								//get InfoT to auto show the last line
								ShowT.selectAll();
								ShowT.setCaretPosition(ShowT.getSelectedText().length());
								ShowT.requestFocus();
								
							}
							SendMB.setEnabled(true);
						}
					}
				}
				);
		
	}
	
	
}
