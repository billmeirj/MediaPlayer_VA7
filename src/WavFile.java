import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
	public WavFile(String path) {
		super(path);
		
		readAndSetDurationFromFile();
	}

	
	public static long computeDuration(long numbersOfFrames, float frameRate) {
		//berechnung der gasamtspielzeit in millisekunden
		if (frameRate <= 0.0f) {
			return 0L;
		}
		
		double duration = (numbersOfFrames* 1000000) / frameRate;
		return (long) duration; //long als rückgabewert
	}
	
	public void readAndSetDurationFromFile() {
		//abrufen von Werten und aufrufen der Methode
		WavParamReader.readParams(getPathname());
		
		float rate = WavParamReader.getFrameRate();
		long frame = WavParamReader.getNumberOfFrames();
		
		this.duration= computeDuration(frame, rate);
	}
	
	@Override
	public String toString() {
		String base = super.toString();
		return base + " - " + formatDuration();
	}
}
