package studiplayer.audio;
import java.io.File;
import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile{
	
	public TaggedFile() throws NotPlayableException {
		super();
		
	}
	
	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		try {
			readAndStoreTags();
		} catch (RuntimeException e){
			throw new NotPlayableException(getPathname(), e);
		}
		
		
		readAndStoreTags();
	}

	private String album = "";
	
	public void readAndStoreTags() throws NotPlayableException {
		
		if(getPathname().isEmpty() || !new File(getPathname()).canRead()) { 
			throw new NotPlayableException(getPathname(), "File kann nicht gelesen werden");
		}
		
		Map<String, Object> tagMap = TagReader.readTags(getPathname());
		
		//aufrufen von Werten aus der Map mit get
		//Maps als Art "Telefonbuch" zu sehen
		//author
		if (tagMap.get("author") != null) {
			this.author = (String) tagMap.get("author").toString().trim();
		}
		
		//titel
		if (tagMap.get("title") != null) {
			this.title = (String) tagMap.get("title").toString().trim();
		}
		
		//album
		if (tagMap.get("album") != null) {
			this.album = (String) tagMap.get("album").toString().trim();
		}
		
		//duration
		if (tagMap.get("duration") != null) {
			this.duration = (long) tagMap.get("duration");
		}
	}
	
	public String getAlbum (){
		return album;
	}
	
	//Überschreiben von toString() für Album 
	@Override
	public String toString() {
		String base = super.toString().trim();
		String time = formatDuration();
		
		if (album.trim().isEmpty()) {
			return base + " - " + time;
		}
		return base + " - " + album.trim() + " - " + time;
	}
}
