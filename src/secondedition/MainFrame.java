package secondedition;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MainFrame {

	private JFrame Mframe=new JFrame("聊天程序");
	
	private JButton CstateB=new JButton("上线");
	
	private JLabel NameL=new JLabel("昵称:");
	private JTextField NameT=new JTextField();
	public static JList UserL=null;
	private JScrollPane UserScr=null;
	
	private DiscoverThread Dt;//discover thread
	private BrocastThread Bt_On;
	private BrocastThread Bt_Off;
	private DatagramSocket Listener;
	
	private Boolean State=false; //true代表当前在线，false代表当前不在线
	
	public MainFrame() {
		Font fnt = new Font("微软雅黑",Font.PLAIN +Font.BOLD,12);
		Mframe.setFont(fnt);
		CstateB.setFont(fnt);
		NameL.setFont(fnt);
		
		UserL=new JList();
		UserL.setBorder(BorderFactory.createTitledBorder("在线用户："));
		UserL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		UserL.setBackground(new Color(245,245,245));
		UserScr=new JScrollPane(UserL);
		
		NameL.setBounds(20, 15, 30, 20);
		NameT.setBounds(55, 15, 90, 20);
		CstateB.setBounds(155, 15, 60, 20);
		UserScr.setBounds(20, 50, 195, 340);
		
		Mframe.setLayout(null);
		Mframe.setResizable(false);
		Mframe.add(NameL);
		Mframe.add(NameT);
		Mframe.add(CstateB);
		Mframe.add(UserScr);
		
		Mframe.setSize(240, 440);
		//set frame at the center of the screen
		Mframe.setLocationRelativeTo(null);
		//set background color
		Mframe.getContentPane().setBackground(new Color(238,238,224));
		Mframe.setVisible(true);
		
		Mframe.addWindowListener(
				new WindowAdapter(){
					/**
					 * it happens when close the windows
					 */
					public void windowClosing(WindowEvent e){
						Mframe.setVisible(false);
						if(Listener!=null&&!Listener.isClosed()){//通知别人自己下线
							Bt_Off=new BrocastThread(Listener,NameT.getText(),false);
							Bt_Off.start();
							while(!Bt_Off.State);
						}
						System.exit(1) ;
					}
				}	
				);
		/**
		 * 上线、下线按钮添加监听
		 */
		CstateB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(e.getSource()==CstateB){
							if(!State){//如果未上线
								if(NameT.getText().equals("")){
									JOptionPane.showMessageDialog(Mframe.getContentPane(),
										       "昵称不能为空！", "注意！", JOptionPane.WARNING_MESSAGE);
									return;
								}
								if(Listener!=null&&(!Listener.isClosed())){
									Listener.close();
								}
								
								for (int i=3000;i<3005;i++){
									try {
										Listener=new DatagramSocket(i);
										Dt=new DiscoverThread(Listener,NameT.getText());
										Dt.start();
										Bt_On=new BrocastThread(Listener,NameT.getText(),true);
										Bt_On.start();
										
										
										CstateB.setText("下线");
										NameT.setEditable(false);
										State=true;
										break;
									} catch (Exception E) {
									}
								}
																														
							}else{//如果要下线
								try{
									Dt.State=false;//关掉发现线程
//									Bt_On.State=false;
									Bt_Off=new BrocastThread(Listener,NameT.getText(),false);
									Bt_Off.start();
									
									CstateB.setText("上线");
									NameT.setEditable(true);
									State=false;
								}catch(Exception g){	
								}
								
							}
							
							
						}
					}
				}
				);
	}
	
	public static void main(String args[]){
		new MainFrame();
	}

}
