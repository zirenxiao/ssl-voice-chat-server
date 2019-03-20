package utility;

import java.util.Base64;

import javax.sound.sampled.AudioFormat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import client.ClientUserInterface;


public class Settings {
	public static final String SERVER_ADDRESS = "127.0.0.1";
	public static final String OUT_PORT = "8888";
	public static final String CERT_PATH = "certificates/cert.pem";
	public static final String PRIVATE_KEY_PATH = "certificates/key.pem";
	
	public static final String COMMAND = "command";
	public static final String TYPEAUDIO = "TA";
	public static final String TYPESERVERMSG = "TSM";
	public static final String TYPECLIENTMSG = "TCM";
	public static final String TYPEPING = "PING";
	public static final String AUDIO = "audio";
	public static final String MESSAGE = "msg";
	public static final String TIME = "time";
	public static final int LENGTHOFAUDIO = 2048;
	
	public static AudioFormat getAudioFormat() {
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 8000f;
		int sampleSize = 16;
		boolean bigEndian = false;
		int channels = 1;
		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	}
	
	@SuppressWarnings("unchecked")
	public static String ctAudioMessage(byte[] msg, Object len) {
		JSONObject obj = new JSONObject();
		String base64String = Base64.getEncoder().encodeToString(msg);
		obj.put(Settings.AUDIO, base64String);
		obj.put(Settings.COMMAND, Settings.TYPEAUDIO);
		return obj.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String ctPingMessage() {
		JSONObject obj = new JSONObject();
		obj.put(Settings.TIME, System.currentTimeMillis());
		obj.put(Settings.COMMAND, Settings.TYPEPING);
		return obj.toJSONString();
	}
	
	public static JSONObject JSONcheck(String msg) {
		JSONObject content;
		JSONParser parser = new JSONParser();
		try {
			content = (JSONObject) parser.parse(msg);
//			return content.containsKey(Settings.COMMAND);
			return content;
		} catch (ParseException e) {
			ExceptionHandler.parse();
		} catch (ClassCastException exc) {
			ExceptionHandler.classCast();
		}
		return null;
//    	return false;
    }
	
	public static void clientPrintOnGUI(String msg) {
		ClientUserInterface.setMessage(msg);
	}

}
