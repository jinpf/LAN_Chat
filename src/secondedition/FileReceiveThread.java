package secondedition;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;


public class FileReceiveThread extends Thread {
	private JFrame Cframe=null;
	private File ReceiveF;
	private ServerSocket ListenS;
	private JProgressBar progressBar=null;
	public FileReceiveThread(File f,ServerSocket S) {
		ReceiveF=f;
		ListenS=S;
		Cframe=new JFrame("接收:"+f.getName());
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
		try {
			Socket client=ListenS.accept();
			DataInputStream di=null;
			FileOutputStream fo=null;
			byte[] inputByte=null;
			int len=0;	//receive data length at one time
			Boolean Sflag=false; //receive successful or not 
	
			try{
				di=new DataInputStream(client.getInputStream());
				//receive file size
				long L=di.readLong();
				//receive file
				fo=new FileOutputStream(ReceiveF);
				inputByte=new byte[1024];
				long sumL=0;
				
				while( (len=di.read(inputByte, 0, inputByte.length)) >0){
					fo.write(inputByte,0,len);
					fo.flush();
					sumL+=len;
					progressBar.setValue((int) (sumL*100/L));
					
//					System.out.println("send "+(sumL*100/L)+"%");
				}
				if(sumL==L)
					Sflag=true;
			}finally{
				if(fo!=null){
					fo.close();
				}
				if(di!=null){
					di.close();
				}
				if(client!=null){
					client.close();
				}
				if(!ListenS.isClosed()){
					ListenS.close();
				}
			}
			if(Sflag){
				JOptionPane.showMessageDialog(Cframe.getContentPane(),
					       "文件接收完毕！", "注意！", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(Cframe.getContentPane(),
					       "接收文件失败！", "注意！", JOptionPane.WARNING_MESSAGE);
			}
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Cframe.getContentPane(),
				       "接收文件失败！", "注意！", JOptionPane.WARNING_MESSAGE);
		}
		Cframe.dispose();
	}

}
