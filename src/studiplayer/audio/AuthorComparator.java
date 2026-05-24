package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile>{

	//Sortieren nach Autor
	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		
		if (o1 == null || o2 == null) {
			throw new RuntimeException("Parameter darf nicht null sein.");
		}
		
		String author1 = o1.getAuthor();
		String author2 = o2.getAuthor();
		
		return author1.compareTo(author2);
	}
}
