package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile>{
	
	public int compare (AudioFile o1, AudioFile o2) {
		
		if (o1 == null || o2 == null) {
			throw new RuntimeException("Paramenter darf nicht null sein.");
		}
		
		boolean hasAlbum1 = o1 instanceof TaggedFile;
		boolean hasAlbum2 = o2 instanceof TaggedFile;
		
		if(!hasAlbum1 && !hasAlbum2) {
			return 0;
		}
		else if (!hasAlbum1) {
			return -1;
		}
		else if(!hasAlbum2) {
			return 1;
		}else {
			String album1 = ((TaggedFile) o1).getAlbum();
			String album2 = ((TaggedFile) o2).getAlbum();
			
			return album1.compareTo(album2);
		}
	}
}
