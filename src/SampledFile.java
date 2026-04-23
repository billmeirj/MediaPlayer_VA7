import studiplayer.basic.BasicPlayer;
public abstract class SampledFile extends AudioFile {
	public SampledFile () {
		super();
	}
	
	public SampledFile (String path) {
		super(path);
	}
	
	public void play () {
		BasicPlayer.play(getPathname());
	}
	
	public void togglePause() {
		BasicPlayer.togglePause();
	}
	
	public void stop () {
		BasicPlayer.stop();
	}
	
	public String formatDuration() {
		return "";
	}
	
	public String formatPosition () {
		return "";
	}
	
	public static String timeFormatter (long timeInMicroSeconds) {
		return "";
	}
	
	public long getDuration() {
		return 0L;
	}
}
