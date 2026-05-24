package studiplayer.audio;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class PlayList {
	
	private int current;
	private String search;
	private SortCriterion sortCriterion;
	
	
	public PlayList() {
		this.current = 0;
		this.sortCriterion = SortCriterion.DEFAULT;
	}
	
	public PlayList(String m3uPathname) throws NotPlayableException {
		this.sortCriterion = SortCriterion.DEFAULT;
		this.loadFromM3U(m3uPathname);
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
	
	public void loadFromM3U (String pathname) throws NotPlayableException {
		
		this.audioFile.clear();
		this.current = 0;
		Scanner scanner = null;
		
		try {
			scanner =new Scanner(new File(pathname));
			
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				
				if (line.isEmpty()) {
					continue;
				}
				
				if (line.startsWith("#")) {
					continue;
				}
				
				try {
					AudioFile audioFile = AudioFileFactory.createAudioFile(line);
					this.audioFile.add(audioFile);
				} catch (NotPlayableException e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			throw new NotPlayableException("Fehler beim Lesen der Datei " + pathname, pathname);
		} finally {
			System.out.println("File " + pathname + " read!");
			scanner.close();
		}
	}
	
	public void saveAsM3U (String pathname) {
		FileWriter writer = null;
		String seperator = System.getProperty("line.separator");
		
		try {
			//neue File "öffnen"
			//schreiben in die Datei
			writer = new FileWriter(pathname);
			for (AudioFile file : this.audioFile) {
				String path = file.getPathname();
				writer.write(path + seperator);
			}
		} catch (IOException e) {
			throw new RuntimeException("Schreiben der Datei " + pathname + " nicht möglich!");
		} finally {
				try {
					System.out.println("File " + pathname + " write!");
					writer.close();
				} catch (Exception e){
					
				}
			}
	}
	
	public int getCurrent() {
		return current;
	}
	
	//Wert aktualisieren
	public void setCurrent(int current) {
		this.current = current;		
	}
	
	//Wert aktualisieren
	public void setSearch (String value) {
		this.search = value;
	}
	
	public String getSearch () {
		return search;
	}
	
	//Wert aktualisieren
	public void setSortCriterion (SortCriterion value) {
		this.sortCriterion = value;
	}
	
	public SortCriterion getSortCriterion () {
		return sortCriterion;
	}
	
}
