package studiplayer.audio;

public class AudioFileFactory {

	public AudioFileFactory() {
		
	}
	
	public static AudioFile createAudioFile(String path) {
		int lastIndex = path.lastIndexOf(".");
		
		if(lastIndex == -1) {
			throw new RuntimeException("Unknown suffix for AudioFile \"" + path + "\"");
		}
		
		String extension = path.substring(lastIndex +1).toLowerCase();
		
		if(extension.equals("wav")){
			return new WavFile(path);
		} 
		else if (extension.equals("mp3") || extension.equals("ogg")) {
			return new TaggedFile(path);
		} else {
			throw new RuntimeException("Unknown suffix for AudioFile \"" + path + "\"");
		}
		
	}
}
