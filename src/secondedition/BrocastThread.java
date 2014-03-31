package secondedition;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BrocastThread extends Thread{
	
	private DatagramSocket Listener;
	private String Name;
	public Boolean State;
	
	/**
	 * 进行定期广播来帮助发现用户
	 * @param UDPsocket
	 * 监听套接字
	 * @param name
	 * 用户昵称
	 * @param flag
	 * 是广播上线还是广播下线，true代表上线，false代表下线
	 */
	public BrocastThread(DatagramSocket UDPsocket,String name,Boolean flag) {
		Listener=UDPsocket;
		Name=name;
		State=flag;
	}

	public void run(){
		DatagramPacket BroadCast=null;
		if(State){//上线
			for (int i=3000;i<3005;i++){
				try {
					String SendS="h"+"  "+Name;
					BroadCast = new DatagramPacket(SendS.getBytes("UTF-8"),SendS.length(),InetAddress.getByName("255.255.255.255"),i) ;
					for(int k=0;k<3;k++){
						Listener.send(BroadCast);
						Thread.sleep(50);
					}
					Listener.send(BroadCast);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}else{//下线
			for (int j=3000;j<3005;j++){
				try {
					String SendS="hhh"+"  "+Name;
					BroadCast = new DatagramPacket(SendS.getBytes("UTF-8"),SendS.length(),InetAddress.getByName("255.255.255.255"),j) ;
					for(int L=0;L<3;L++){
						Listener.send(BroadCast);
						Thread.sleep(50);
					}	
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			State=true;
		}
		
	}
}
