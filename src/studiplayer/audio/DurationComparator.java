package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile>{

	public int compare (AudioFile o1, AudioFile o2) {
		if (o1 == null || o2 == null) {
			throw new RuntimeException("Parameter darf nicht null sein.");
		}
		
		boolean hasDuration1 = o1 instanceof SampledFile;
		boolean hasDuration2 = o2 instanceof SampledFile;
		
		if(!hasDuration1 && !hasDuration2) {
			return 0;
		}
		else if(!hasDuration1) {
			return -1;
		}
		else if(!hasDuration2) {
			return 1;
		} else {
			
			long duration1 = ((SampledFile) o1).getDuration();
			long duration2 = ((SampledFile) o2).getDuration();
			
			return Long.compare(duration1, duration2);
		}
	}
}
