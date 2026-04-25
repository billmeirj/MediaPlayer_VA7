import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile{
	public TaggedFile(String path) {
		super(path);
		
		readAndStoreTags();
	}

	private String album = "";
	
	public void readAndStoreTags() {
		Map<String, Object> tagMap = TagReader.readTags(getPathname());
		
		//aufrufen von Werten aus der Map mit .get
		//author
		if (tagMap.get("author") != null) {
			this.author = ((String) tagMap.get("author"));
		}
		
		//titel
		if (tagMap.get("title") != null) {
			this.title = ((String) tagMap.get("title"));
		}
		
		//album
		if (tagMap.get("album") != null) {
			this.album = ((String) tagMap.get("album"));
		}
		
		//duration
		if (tagMap.get("duration") != null) {
			this.duration = (long) tagMap.get("duration");
		}
	}
	
	public String getAlbum (){
		return album;
	}
	
	@Override
	public String toString() {
		String base = super.toString();
		if (album.isEmpty()) {
			return base;
		}
		return base + " [" + album + "]";
	}

	
}
