package secondedition;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

/**
 * Listen and discover peers
 * @author jinpf
 *
 */		
public class DiscoverThread extends Thread{
	private DatagramSocket Listener;
	private Vector<String> v=new Vector<String>();
	private String Name;
	private int Port;
	public Boolean State;//线程是否执行
	
	/**
	 * 监听发现在线用户
	 * @param UDPsocket
	 * 扫描监听UDP套接字
	 * @param name
	 * 用户昵称
	 * @param port
	 * 通信套接字监听接口
	 */
	public DiscoverThread(DatagramSocket UDPsocket,String name,int port) {
		Listener=UDPsocket;
		Name=name;
		Port=port;
	}
	
	public void run(){
		DatagramPacket Receive=null;
		byte[] bufR = new byte[1024] ;
		Receive =new DatagramPacket(bufR,1024);
		String Vstr;	//Vector 一条里的内容,包含用户名、IP地址、端口号，以双空格隔开
		State=true;
		while(State){
			try {
				Receive.setLength(1024);
				Listener.receive(Receive);
				String str = new String(Receive.getData(),0,Receive.getLength(),"UTF-8");
				String ReceiveS[]=str.split("  ");			
			
				if( (ReceiveS[0].length()==1) && (Receive.getPort()!=Listener.getLocalPort()) ){//收到"h"
					String ReplyS="hh"+"  "+Name+"  "+Port;
					DatagramPacket Send= new DatagramPacket(ReplyS.getBytes("UTF-8"),ReplyS.length(),Receive.getAddress(),Receive.getPort()) ;
					Listener.send(Send);
					Vstr=ReceiveS[1]+"  "+Receive.getAddress().getHostAddress()+"  "+ReceiveS[2];
					if(!v.contains(Vstr)){
						v.add(Vstr);
						Collections.sort(v);
						MainFrame.UserL.setListData(v);
					}
				}else if(ReceiveS[0].length()==2){//收到"hh"
					Vstr=ReceiveS[1]+"  "+Receive.getAddress().getHostAddress()+"  "+ReceiveS[2];
					if(!v.contains(Vstr)){
						v.add(Vstr);
						Collections.sort(v);
						MainFrame.UserL.setListData(v);
					}
				}else if(ReceiveS[0].length()==3){
					Vstr=ReceiveS[1]+"  "+Receive.getAddress().getHostAddress()+"  "+ReceiveS[2];
					if(v.contains(Vstr)){
						v.remove(Vstr);
						MainFrame.UserL.setListData(v);
					}
				}
				

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		v.clear();
		MainFrame.UserL.setListData(v);
	}

}
