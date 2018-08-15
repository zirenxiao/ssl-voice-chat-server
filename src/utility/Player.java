package utility;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Player implements Runnable{
	private byte buf[];
	private int len;
	private DataLine.Info sourceinfo;	
	private SourceDataLine sd;
	private AudioFormat af;
	
	public Player(byte _buf[],int _len) throws LineUnavailableException{
		buf = _buf;
		len = _len;
		af = Settings.getAudioFormat();
		sourceinfo = new DataLine.Info(SourceDataLine.class, af);	
		sd = (SourceDataLine) (AudioSystem.getLine(sourceinfo));
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			sd.open();
			sd.start();
			sd.write(buf, 0, len);	
			sd.drain();
			sd.stop();
			sd.close();					
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
