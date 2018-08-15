package utility;

import java.util.Base64;

import javax.sound.sampled.AudioFormat;

import org.json.simple.JSONObject;


public class Settings {
	public static final String SERVER_ADDRESS = "127.0.0.1";
	public static final String OUT_PORT = "8007";
	public static final String CERT_PATH = "certificates/cert.pem";
	public static final String PRIVATE_KEY_PATH = "certificates/key.pem";
	
	public static final String COMMAND = "command";
	public static final String AUDIO = "audio";
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
		return obj.toJSONString();
	}
}
