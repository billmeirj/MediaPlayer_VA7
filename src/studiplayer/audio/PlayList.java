package studiplayer.audio;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.management.RuntimeErrorException;


public class PlayList implements Iterable<AudioFile> {
	
	//private int current;
	private String search;
	private SortCriterion sortCriterion;
	private ControllablePlayListIterator playListIterator;
	private AudioFile currentSong;
	
	
	public PlayList() {
		//this.current = 0;
		this.sortCriterion = SortCriterion.DEFAULT;
		resetIterator();
	}
	
	public PlayList(String m3uPathname) {
		this.sortCriterion = SortCriterion.DEFAULT;
		try {
			this.loadFromM3U(m3uPathname);
		} catch (NotPlayableException e) {
			throw new RuntimeException(e);
		}
		
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
		resetIterator();
	}
	
	//remove Methode
	public void remove (AudioFile file) {
		this.audioFile.remove(file);
		resetIterator();
	}
	
	//Anzahl AudioFiles in Liste
	public int size() {
		int anzahl = this.audioFile.size();
		return anzahl;
	}
	
	public AudioFile currentAudioFile() {
		//wenn leer
		if (audioFile.isEmpty()) {
			return null;
		}
		
		//wenn null
		if(playListIterator == null) {
			resetIterator();
		}
		
		//Song bekannt
		if (currentSong != null) { //|| currentSong >= audioFile.size()
			return currentSong;
		}
		
		//Song unbekannt
		if(currentSong == null) {
			if(playListIterator.hasNext()) {
				currentSong = playListIterator.next();
			} else {
				resetIterator();
				currentSong = playListIterator.next();
			}
		}
			
		return currentSong;
		//return audioFile.get(current);
	}
	
	public void nextSong() {
		if(audioFile.isEmpty()) {
			currentSong = null;
			return;
		}
		
		//falls currentSong noch nicht gesetzt wurde
		if(currentSong == null) {
			if(playListIterator.hasNext()) {
				playListIterator.next();
			}
		}
		//nächsten Song abspielen
		if (playListIterator.hasNext()) {
			currentSong = playListIterator.next();
		} else {
			//Playlist zurücksetzten
			resetIterator();
			if (playListIterator.hasNext()) {
				currentSong = playListIterator.next();
			} else {
				currentSong = null;
			}
			
		}
		
//		current ++;
//		
//		if (current < 0 || current >= audioFile.size()) {
//			current = 0;
//		}
//		
	}
	
	public void loadFromM3U (String pathname) throws NotPlayableException {
		
		this.audioFile.clear();
		//this.current = 0;
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
		resetIterator();
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
	
//	public int getCurrent() {
//		return current;
//	}
//	
//	//Wert aktualisieren
//	public void setCurrent(int current) {
//		this.current = current;		
//	}
	
	//Wert aktualisieren
	public void setSearch (String value) {
		this.search = value;
		resetIterator();
	}
	
	public String getSearch () {
		return search;
	}
	
	//Wert aktualisieren
	public void setSortCriterion (SortCriterion value) {
		this.sortCriterion = value;
		resetIterator();
	}
	
	public SortCriterion getSortCriterion () {
		return sortCriterion;
	}
	

	@Override
	public Iterator<AudioFile> iterator() {
		// prüfen ob null
		if (playListIterator == null) {
			resetIterator();
		}
		
		return this.playListIterator;
		//ControllablePlayListIterator aufrufen
		//return new ControllablePlayListIterator(audioFile, search, sortCriterion);
	}
	
	public void resetIterator() {
		this.playListIterator = new ControllablePlayListIterator(audioFile, search, sortCriterion);
		this.currentSong = null;
	}
	
	public void jumpToAudioFile (AudioFile file) {
		
		//Iterator null
		if(playListIterator == null) {
			resetIterator();
		} 
		
		playListIterator.jumpToAudioFile(file);
		currentSong = file;
		
//		if(playListIterator.hasNext()) {
//			currentSong = playListIterator.next();
//		} else {
//			currentSong = null;
//		}
	}
	
}
