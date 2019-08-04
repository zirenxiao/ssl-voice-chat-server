package client;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import io.netty.channel.Channel;
import utility.Recorder;


public class ClientUserInterface {
	private static final long serialVersionUID = 2361313703736003082L;

	private JMenuItem voiceSwitch = new JMenuItem("Voice Switch");
	private Recorder re = null;
	private Channel ch = null;
	private static JTextArea ta = new JTextArea(10,40);
	private static JFrame f = new JFrame("SSL Voice Communication");
	private boolean voiceStatus = false;

	public ClientUserInterface(){	
		f.setLayout(new GridLayout(1,0));
		
		// set text area
		ta.setEditable(false);
		ta.setLineWrap(true);
		
		// add menu
		JMenu menu = new JMenu("Menu");
		JMenuItem closeApp = new JMenuItem("Exit");
		menu.add(voiceSwitch);
		menu.add(closeApp);
		JMenuBar mb = new JMenuBar();
		mb.add(menu);
		f.setJMenuBar(mb);
		
		f.add(new JScrollPane(ta));
		
		
		voiceSwitch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (!voiceStatus) {
					setMessage("[self] Open Mic");
					re = new Recorder(ch);
					voiceStatus = true;
				}else {
					setMessage("[self] Close Mic");
					voiceStatus = false;
					re.stop();
				}
				
			}
		});
		
		closeApp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exitApp();
			}
		});
		
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exitApp();
			}
		});
		
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	public void init(Channel channel){	
		ch = channel;
	}
	
	private void exitApp() {
//		ch.close();
		Client.group.shutdownGracefully();
		System.exit(0);
	}
	
	public static void setMessage(String msg) {
		ta.setText(ta.getText() + msg + "\n");
	}
}
