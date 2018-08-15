package utility;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import io.netty.channel.Channel;


public class Recorder implements Runnable {

	private boolean runningStatus;
	private Channel ch;
	private Thread t;
	private AudioFormat af;	
	private TargetDataLine td;
	public Recorder(Channel ch) {
		super();
		this.ch = ch;
		af = Settings.getAudioFormat();
		try {
			td = (TargetDataLine) (AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, af)));
		} catch (LineUnavailableException e) {
			ExceptionHandler.lineUnavailable();
		}
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {		
			

			td.open(af);
			td.start();
			
			byte bts[] = new byte[Settings.LENGTHOFAUDIO];
			runningStatus = true;
			
			while (runningStatus) {
				int cnt = td.read(bts, 0, bts.length);
				if (cnt > 0) {
					ch.writeAndFlush(Settings.ctAudioMessage(bts, cnt) + "\n");
//					System.err.println("Sent:"+Settings.ctAudioMessage(bts, cnt));
				}				
			}
			td.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		td.close();
		t.stop();
	}
	

}
