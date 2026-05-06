import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class PlayList {
	
	private int current;
	
	public PlayList() {
		this.current = 0;
	}
	
	public PlayList(String m3uPathname) {
		this();
	}

	//Liste für Aggregation
	private List<AudioFile> audioFile = new LinkedList<AudioFile>();
	
	//Attribute abfragen
	public List<AudioFile> getList() {
		return this.audioFile;
	}
	
	//add Mathode
	public void add (AudioFile file) {
		if (file != null) {
			this.audioFile.add(file);
		}
	}
	
	//remove Methode
	public void remove (AudioFile file) {
		this.audioFile.remove(file);
	}
	
	//Anzahl AudioFiles in Liste
	public int size() {
		int anzahl = this.audioFile.size();
		return anzahl;
	}
	
	public AudioFile currentAudioFile() {
		if (audioFile.isEmpty()) {
			return null;
		}
		
		if (current < 0 || current >= audioFile.size()) {
			return null;
		}
		
		return audioFile.get(current);
	}
	
	public void nextSong() {
		current ++;
		
		if (current < 0 || current >= audioFile.size()) {
			current = 0;
		}
		
	}
	
	public void loadFromM3U (String pathname) {
		
	}
	
	public void saveAsM3U (String pathname) {
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");
		
		try {
			writer = new FileWriter(pathname);
			for (AudioFile file : this.audioFile) {
				String path = getPathname();
				writer.write(path + sep);
			}
		}
	}
	
	public int getCurrent() {
		return current;
	}
	
	public void setCurrent(int current) {
		this.current = current;		
	}
	
}
