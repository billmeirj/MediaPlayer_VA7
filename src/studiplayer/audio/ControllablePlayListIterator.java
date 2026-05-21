package studiplayer.audio;

import java.util.Iterator;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	private List<AudioFile> list;
	private int current;
	
	public ControllablePlayListIterator(List<AudioFile> list) {
		this.list = list;
		current = 0;
	}

	@Override
	public boolean hasNext() {
		return current < list.size();
	}

	@Override
	public AudioFile next() {
		
		AudioFile currentFile = list.get(current);
		current ++;
		return currentFile;
	}
	
	public AudioFile jumpToAudioFile (AudioFile file) {
		
		int findIndex = list.indexOf(file);
		
		if(findIndex == -1) {
			return null;
		} else {
			this.current = findIndex + 1;
			
			return file;
		}
		
	}
	
}
