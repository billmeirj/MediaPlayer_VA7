package studiplayer.audio;

public class AudioFileFactory {

	public AudioFileFactory() {
		
	}
	
	public static AudioFile createAudioFile(String path) throws NotPlayableException{
		path = path.trim();
		
		if (!path.toLowerCase().endsWith(".mp3") 
				&& !path.toLowerCase().endsWith(".wav") 
				&& !path.toLowerCase().endsWith(".ogg")) {
			throw new NotPlayableException (path, "Unbekannte Endung der Datei.");
		}
		
		int lastIndex = path.lastIndexOf(".");
		
		if(lastIndex == -1) {
			throw new NotPlayableException("Unknown suffix for AudioFile \"" + path + "\"",path);
		}
		
		String extension = path.substring(lastIndex +1).toLowerCase();
		
		if(extension.equals("wav")){
			return new WavFile(path);
		} 
		else if (extension.equals("mp3") || extension.equals("ogg")) {
			return new TaggedFile(path);
		} else {
			throw new NotPlayableException("Unknown suffix for AudioFile \"" + path + "\"", path);
		}
		
	}
}
