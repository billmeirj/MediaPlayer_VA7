package studiplayer.audio;
import java.io.File;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
	
	public WavFile(String path) throws NotPlayableException {
		super(path);
		
//		if(title.isEmpty() || !new File(title).canRead()) {
//			throw new NotPlayableException(title, "File kann nicht gelesen werden");
//		}
		
		if (title.isEmpty() || !title.toLowerCase().endsWith(".wav")) {
			throw new NotPlayableException(title, "WavFile kann keine MP3 Dateien laden.");
		}
		
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
	
	public void readAndSetDurationFromFile() throws NotPlayableException {
		if(title.isEmpty() || !new File(title).canRead()) {
			throw new NotPlayableException(title, "File kann nicht gelesen werden");
		}
		
		//abrufen von Werten und aufrufen der Methode
		WavParamReader.readParams(getPathname());
		
		float rate = WavParamReader.getFrameRate();
		long frame = WavParamReader.getNumberOfFrames();
		
		this.duration= computeDuration(frame, rate);
	}
	
	//überschreiben von toString (), für Spielzeit + Trenner
	@Override
	public String toString() {
		String base = super.toString();
		return base + " - " + formatDuration();
	}
}
