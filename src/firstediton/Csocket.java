package firstediton;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;


public class Csocket {
	private Socket client=null;
	public Boolean flag=null;
	private Thread t=null;
	/**
	 * Client socket  
	 * @param IP
	 * String ,IP address
	 * @param Port
	 * int ,connect port
	 */
	public Csocket(String IP,int Port){
		try {
			client=new Socket(IP,Port);
			flag=true;
			t=new Thread(
					new Runnable(){
						public void run(){
							try {
								DataInputStream in =new DataInputStream(client.getInputStream());
								while(flag){
									String str;
									try {
										
										str = in.readUTF();
										MainFrame.InfoT.append("C收到:"+str+"\n");
										
									} catch (IOException e) {
										e.printStackTrace();
										if(e.getMessage()==null){
											MainFrame.InfoT.append("C:对方断开连接\n");
											
										}else{
											MainFrame.InfoT.append("C:接收消息失败\n");
											MainFrame.InfoT.append(e.getMessage()+"\n");
										}
										flag=false;
									}	
									
									//get InfoT to auto show the last line
									MainFrame.InfoT.selectAll();
									MainFrame.InfoT.setCaretPosition(MainFrame.InfoT.getSelectedText().length());
									MainFrame.InfoT.requestFocus();
								}	
								
							} catch (Exception e) {
								MainFrame.InfoT.append(e.getMessage()+"\n");
							}
																	
						}						
					}
					);
			t.start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(MainFrame.Mframe.getContentPane(),
				       "连接失败，请检查连接地址！", "警告！", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * send out message to client
	 * @param message
	 * string ,the sending message
	 */
	public void SendMessage(String message){
		try {
			DataOutputStream out=new DataOutputStream(client.getOutputStream());
			out.writeUTF(message);
			MainFrame.InfoT.append("C发送:"+message+"\n");
//			out.close();
		} catch (IOException e) {
			MainFrame.InfoT.append("C发送失败\n");
			MainFrame.InfoT.append(e.getMessage()+"\n");
		}
	}
	
	/**
	 * close the server
	 * @return
	 * Boolean ,whether the server has been closed
	 */
	public Boolean Close(){
		flag=false;
		try{
			if(t.isAlive())
				t.interrupt();
			client.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * whether the server is closed
	 * @return
	 * Boolean if server closed then return true
	 */
	public Boolean is_Closed(){
		return (client==null)||client.isClosed();
	}
}
