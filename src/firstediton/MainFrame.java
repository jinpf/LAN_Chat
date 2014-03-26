package firstediton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class MainFrame {

	public static JFrame Mframe=new JFrame("聊天程序");
	
	public static JButton ListenB=new JButton("监听");
	public static JButton StopB=new JButton("停止");
	public static JButton ConnectB=new JButton("连接");
	public static JButton SendB=new JButton("发送");
	public static JButton ReplyB=new JButton("回复");
	
	public static JTextField LPortT=new JTextField();
	public static JTextField ReplyT=new JTextField();
	public static JTextField SendT=new JTextField();
	public static JTextField SHostT=new JTextField();
	public static JTextField SPortT=new JTextField();
	
	public static JTextArea InfoT=new JTextArea();
	
	private static JLabel InfoL=new JLabel("消息框：");
	//special attention :JLabel lay on JScrollPane ,not JFrame
	private static JScrollPane InfoScr=new JScrollPane(InfoT);
	
	public static Ssocket MSsocket=null;
	public static Csocket MCsocket=null;
	
	public static void main(String[] args) {
		
		Font fnt = new Font("微软雅黑",Font.PLAIN +Font.BOLD,12);
		
		ListenB.setFont(fnt);
		StopB.setFont(fnt);
		ConnectB.setFont(fnt);
		SendB.setFont(fnt);
		ReplyB.setFont(fnt);
		InfoL.setFont(fnt);
		
		ListenB.setBounds(130, 20, 60, 20);
		StopB.setBounds(200, 20, 60, 20);
		ConnectB.setBounds(200, 275, 60, 20);
		ReplyB.setBounds(200, 245, 60, 20);
		SendB.setBounds(200, 300, 60, 20);
		LPortT.setBounds(30, 20, 90, 20);
		ReplyT.setBounds(30, 245, 160, 20);
		SendT.setBounds(30, 300, 160, 20);
		SHostT.setBounds(30, 275, 100, 20);
		SPortT.setBounds(140, 275, 50, 20);
		InfoL.setBounds(30, 45, 80, 20);
		InfoScr.setBounds(30, 70, 230, 160);
		
		//the content of IntoT can`t be changed
		InfoT.setEditable(false);
		
		//set Layout style
		Mframe.setLayout(null);
		//disable maximize
		Mframe.setResizable(false);
		Mframe.add(ListenB);
		Mframe.add(StopB);
		Mframe.add(ConnectB);
		Mframe.add(ReplyB);
		Mframe.add(SendB);
		Mframe.add(LPortT);
		Mframe.add(ReplyT);
		Mframe.add(SendT);
		Mframe.add(SHostT);
		Mframe.add(SPortT);
		Mframe.add(InfoScr);
		Mframe.add(InfoL);
		
		Mframe.setSize(295, 370);
		//set frame at the center of the screen
		Mframe.setLocationRelativeTo(null);
		//set background color
		Mframe.setBackground(Color.BLUE);
		Mframe.setVisible(true);
		
		Mframe.addWindowListener(
				new WindowAdapter(){
					/**
					 * it happens when close the windows
					 */
					public void windowClosing(WindowEvent e){
						System.exit(1) ;
					}
				}	
				);
		
		ListenB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==ListenB){
							if(MSsocket==null||MSsocket.is_Closed()){
								try{
									int port=Integer.parseInt(LPortT.getText());
									MSsocket=new Ssocket(port);
								}catch(Exception E){
									JOptionPane.showMessageDialog(Mframe.getContentPane(),
										       "端口号必须为整数!", "注意！", JOptionPane.WARNING_MESSAGE);
									
								}	
										
							}else{
								JOptionPane.showMessageDialog(Mframe.getContentPane(),
									       "已在监听!", "注意！", JOptionPane.WARNING_MESSAGE);
							}
	
						}
					}
					
				}
				
				);
		
		StopB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==StopB){
							if(MSsocket!=null && !MSsocket.is_Closed()){
								if(!MSsocket.Close())
									JOptionPane.showMessageDialog(Mframe.getContentPane(),
										       "关闭失败!", "注意！", JOptionPane.WARNING_MESSAGE);
							}
							else{
								JOptionPane.showMessageDialog(Mframe.getContentPane(),
									       "未开启监听!", "注意！", JOptionPane.WARNING_MESSAGE);
							}
							
						}												
					}	
				}
				);
		
		ReplyB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==ReplyB){
							MSsocket.SendMessage(ReplyT.getText());
						}
					}	
				}
				
				);
		
		ConnectB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==ConnectB){
							if(MCsocket==null||MCsocket.is_Closed()){
								try{
									int port=Integer.parseInt(SPortT.getText());
									MCsocket=new Csocket(SHostT.getText(),port);
								}catch(Exception E){
									JOptionPane.showMessageDialog(Mframe.getContentPane(),
										       "端口号必须为整数!", "注意！", JOptionPane.WARNING_MESSAGE);
								}
							}else{
								JOptionPane.showMessageDialog(Mframe.getContentPane(),
									       "已经连接!", "注意！", JOptionPane.WARNING_MESSAGE);
							}
							
						}
					}	
				}
				);
		
		SendB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==SendB){
							MCsocket.SendMessage(SendT.getText());
						}
					}	
				}
				);
		
	}

}
