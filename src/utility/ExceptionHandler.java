package utility;

public class ExceptionHandler {
	public static void io() {
		System.err.println("IOException");
	}
	public static void interrupted() {
		System.err.println("InterruptedException");
	}
	public static void parse() {
		System.err.println("ParseException");
	}
	public static void classCast() {
		System.err.println("ClassCastException");
	}
	public static void lineUnavailable() {
		System.err.println("LineUnavailableException");
	}
}
