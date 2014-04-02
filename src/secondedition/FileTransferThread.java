package secondedition;

import java.awt.BorderLayout;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class FileTransferThread extends Thread{
	private JFrame Cframe=null;
	private Socket SendS;
	private File SendF;
	private JProgressBar progressBar=null;
	
	public FileTransferThread(File f,Socket s) {
		SendF=f;
		SendS=s;
		
		Cframe=new JFrame("发送:"+f.getName());
		progressBar = new JProgressBar(0,100);
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		
//		Cframe.setResizable(false);//不能最大化
		Cframe.setSize(250, 60);
		Cframe.add(progressBar,BorderLayout.CENTER);
		Cframe.setLocationRelativeTo(null);//在屏幕中央
		Cframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Cframe.setVisible(true);
	}
	
	public void run(){
		try{
			
			DataOutputStream dout=null;
			FileInputStream fi=null;
			byte[] sendBytes=null;
			int len=0;	//read file data length at one time
			Boolean Sflag=false; //send successful or not 
				
			try{
				dout=new DataOutputStream(SendS.getOutputStream());
				fi=new FileInputStream(SendF);
				sendBytes=new byte[1024];
				long L=SendF.length();
				long sumL=0;
				//send file size
				dout.writeLong(L);
				//send file
				
				while( (len=fi.read(sendBytes, 0, sendBytes.length))>0 ){
					dout.write(sendBytes, 0, len);
					dout.flush();
					sumL+=len;		
					progressBar.setValue((int) (sumL*100/L));
//					System.out.println("send "+(sumL*100/L)+"%");
				}
				if(sumL==L)
					Sflag=true;
			}catch(Exception e){
				Sflag=false;
			}finally{
				if(fi!=null){
					fi.close();
				}
				if(dout!=null){
					dout.close();
				}
				if(SendS!=null){
					SendS.close();
				}
			}	
			if(Sflag){
				JOptionPane.showMessageDialog(Cframe.getContentPane(),
					       "文件发送完毕！", "注意！", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(Cframe.getContentPane(),
					       "发送文件失败！", "注意！", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Cframe.getContentPane(),
				       "发送文件失败！", "注意！", JOptionPane.WARNING_MESSAGE);
		}
		Cframe.dispose();
	}

}
