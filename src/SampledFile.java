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
		return timeFormatter(getDuration());
	}
	
	public String formatPosition () {
		return timeFormatter(BasicPlayer.getPosition());
	}
	
	//umrechnung in Minuten und Sekunden
	public static String timeFormatter (long timeInMicroSeconds) {
		if (timeInMicroSeconds < 0L || timeInMicroSeconds > 5999999999L) {
			throw new RuntimeException("Zeitangabe außerhalb des gültigen Bereichs!");
		}
		
		long totalSec = timeInMicroSeconds / 1000000L;
		long minutes = totalSec / 60;
		long seconds = totalSec % 60;
		
		return String.format("%02d:%02d", minutes, seconds);
	}
}
