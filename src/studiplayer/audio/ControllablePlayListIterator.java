package studiplayer.audio;

import java.util.ArrayList;
import java.util.Comparator;
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
	
	//Konstruktor, der search und sortCriterion erhält
	//erzeugen von Abspielliste, gefiltert und sortiert nach Vorgaben
	//search null/leer -> alle AudioInstanzen der Playlist verwenden, 
	//sonst nur die, die bei Album, Titel, Autor String enthalten
	
	public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sortCriterion) {
		this.current = 0;
		this.list = new ArrayList<AudioFile>();
		
		if (search == null || search.trim().isEmpty()) {
			this.list.addAll(list);
		} else {
			for(AudioFile file : list) {
				if(file.getAuthor() != null && file.getAuthor().contains(search)) {
					this.list.add(file);
				}
				else if (file.getTitle() != null && file.getTitle().contains(search)) {
					this.list.add(file);
				}
				else if ((file instanceof TaggedFile) 
						&& ((TaggedFile) file).getAlbum() != null
						&& ((TaggedFile) file).getAlbum().contains(search)){
					this.list.add(file);
				}
			}
		}
		
		if(sortCriterion == null || sortCriterion == SortCriterion.DEFAULT) {
		}
		else if (sortCriterion == SortCriterion.ALBUM) {
			this.list.sort(new AlbumComparator());
		}
		else if (sortCriterion == SortCriterion.AUTHOR) {
			this.list.sort(new AuthorComparator());
		}
		else if (sortCriterion == SortCriterion.TITLE) {
			this.list.sort(new TitleComparator());
		}
		else if (sortCriterion == SortCriterion.DURATION) {
			this.list.sort(new DurationComparator());
		}
		
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
