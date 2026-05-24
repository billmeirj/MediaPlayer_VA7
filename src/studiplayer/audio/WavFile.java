package studiplayer.audio;
import java.io.File;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
	
	public WavFile() throws NotPlayableException {
		super();
	}
	
	public WavFile(String path) throws NotPlayableException {
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
	
	public void readAndSetDurationFromFile() throws NotPlayableException {
		if(getPathname().isEmpty() || !new File(getPathname()).canRead()) {
			throw new NotPlayableException(getPathname(), "File kann nicht gelesen werden");
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
