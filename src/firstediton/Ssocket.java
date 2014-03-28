package firstediton;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Ssocket {
	private ServerSocket server=null;
	public  Boolean flag=null;	//whether server is running
	private Socket client=null;
	private Thread t=null;
	
	public Ssocket(int port){
		try {
			server=new ServerSocket(port);
			MainFrame.InfoT.append("开始监听\n");
			
			t=new Thread(
					new Runnable(){
						public void run(){
							try {
								client=server.accept();
								DataInputStream in=new DataInputStream(client.getInputStream());
								flag=true;
								MainFrame.InfoT.append("Client from"+client.getInetAddress()+":"+client.getPort()+"\n");
							
								while(flag){
									String str;
									try {
										
										str = in.readUTF();
										MainFrame.InfoT.append("S收到:"+str+"\n");
										
									} catch (IOException e) {
										e.printStackTrace();
										if(e.getMessage()==null){
											MainFrame.InfoT.append("S:对方断开连接\n");
											
										}else{
											MainFrame.InfoT.append("S:接收消息失败\n");
											MainFrame.InfoT.append(e.getMessage()+"\n");
										}
										flag=false;
									}
									
									//get InfoT to auto show the last line
									MainFrame.InfoT.selectAll();
									MainFrame.InfoT.setCaretPosition(MainFrame.InfoT.getSelectedText().length());
									MainFrame.InfoT.requestFocus();
								}	
								client.close();
							} catch (IOException e) {
								MainFrame.InfoT.append(e.getMessage()+"\n");
							}										
						}						
					}
					);
			t.start();
		} catch (IOException e) {
			MainFrame.InfoT.append("监听失败\n");
			MainFrame.InfoT.append(e.getMessage()+"\n");
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
			MainFrame.InfoT.append("S发送:"+message+"\n");
//			out.close();
		} catch (IOException e) {
			MainFrame.InfoT.append("S发送失败\n");
			MainFrame.InfoT.append(e.getMessage()+"\n");
		}
		
		//get InfoT to auto show the last line
		MainFrame.InfoT.selectAll();
		MainFrame.InfoT.setCaretPosition(MainFrame.InfoT.getSelectedText().length());
		MainFrame.InfoT.requestFocus();
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
			server.close();
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
		return (server==null)||server.isClosed();
	}
}
