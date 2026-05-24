package studiplayer.audio;

import java.util.Iterator;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	
	//Wiederabeliste von Audiodatein, erlaubt zu bestimmten Titel zu springen
	private List<AudioFile> list;
	private int current;
	
	public ControllablePlayListIterator(List<AudioFile> list) {
		this.list = list;
		current = 0;
	}

	//prüft, ob weitere Dateien in Liste vorhaden
	@Override
	public boolean hasNext() {
		return current < list.size();
	}

	//gibt aktuellen Titel zurück und wechselt auf nächsten titel
	@Override
	public AudioFile next() {
		
		AudioFile currentFile = list.get(current);
		current ++;
		return currentFile;
	}
	
	//Springt zu bestimmter Audiodatei
	//so gesetzt, dass 'next' Element nach aktueller Datei liefert
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
