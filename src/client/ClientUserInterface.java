package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import io.netty.channel.Channel;
import utility.Recorder;


public class ClientUserInterface extends JFrame{
	private static final long serialVersionUID = 2361313703736003082L;

	private JCheckBox doActivity = new JCheckBox("语音开关");
	private JLabel status = new JLabel("");
	private JLabel serverStatus = new JLabel("");
	private Recorder re = null;
	private Channel ch = null;

	public ClientUserInterface(Channel channel){	
		this.ch = channel;
		setLayout(new GridLayout(3,1));
		add(doActivity);
		add(serverStatus);
		add(status);

		
		doActivity.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (doActivity.isSelected()) {
					status.setText("语音打开");
					re = new Recorder(ch);
				}else {
					status.setText("语音关闭");
					re.stop();
				}
				
			}
		});
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				ch.close();
				System.exit(0);
			}
		});
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
