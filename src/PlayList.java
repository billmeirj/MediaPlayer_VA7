import java.util.LinkedList;
import java.util.List;

public class PlayList {
	
//	public PlayList() {
//	}

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
}
