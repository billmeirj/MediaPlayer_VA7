import studiplayer.basic.BasicPlayer;
import java.io.*;

public abstract class AudioFile {
	
	private String pathname;
	private String filename;
	protected String author;
	protected String title;
	protected long duration;

	public AudioFile(){
		this("");
	}
	
	public AudioFile (String path) {
		this.pathname = path;
		
		parsePathname(path);
		parseFilename(this.filename);
		
	}
	
	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
	
	public void parsePathname(String path) {
		//Leerzeichen vorne und hinten entfernen
		path = path.trim();
		if(path.isEmpty()) {
			this.pathname = "";
			this.filename = "";
		}
		
		//Pfadseperatoren anpassen
		String seperator;
		if(isWindows()) {
			seperator = "\\";
		} else {
			seperator = "/";
		}
		
		//Pfadseperatoren vereinheitlichen und zusammenfassen
		String result ="";
		char vorher = 0;
		
		for(int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);
			
			if (c == '/' || c == '\\') {
				if (vorher != seperator.charAt(0)) {
					result += seperator.charAt(0);
					vorher = seperator.charAt(0);
				} 
			} else {
				result += c;
				vorher = c;
				}
		}
		
		//Laufwerkbuchstaben bearbeiten NUR wenn nicht Windows
		if (!isWindows() && result.length() >= 2) {
			char char1 = result.charAt(0);
			char char2 = result.charAt(1);
			
			if((Character.toLowerCase(char1) >= 'a' && Character.toLowerCase(char1) <= 'z')
					&& char2 == ':') {
				char laufWerk = char1;
				String rest = result.substring(2);
				
				if(rest.startsWith(seperator)) {
					rest = rest.substring(1);
				}
				
				result = seperator + laufWerk  + seperator + rest;
			}
		}
		
		//pathname speichern
		if(!result.equals(" - ")) {
			result= result.trim();
		}
		this.pathname = result;
		
		//filename speicher
		int lastSeperator = pathname.lastIndexOf(seperator);
		if(lastSeperator >= 0) {
			this.filename = pathname.substring(lastSeperator+1).trim();
		} else {
			this.filename = pathname.trim();
		}

		File file = new File(this.pathname);
		if (!file.canRead()) {
			throw new RuntimeException("Datei ist nicht lesbar: " + this.pathname);
		}
	}
	
	public String getPathname() {
		return pathname;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void parseFilename (String nameDatei) {
		
		String spezial = " - ";
		if (nameDatei.equals(spezial)) {
			this.author = "";
			this.title = "";
			return;
		}
		
		//Leerzeichen entfernen
		if (nameDatei.trim().isEmpty() || nameDatei.equals("")) {
			this.author = "";
			this.title = "";
			return;
		}
		
		//Endung entfernen
		int punkt = nameDatei.lastIndexOf(".");
		String result = "";
		
		if (punkt >= 0) {
			result = nameDatei.substring(0, punkt);
		} else {
			result = nameDatei;
		}
		
		//Autor finden, Bindestrich suchen
		int seperator = result.indexOf(" - ");
		int laengeTrenner = 3;
				
		//Autor und Titel richtig trennen und speichern
		if(seperator >= 0 && result.length() > 1) {
			String interpret = result.substring(0, seperator);
			String nameSong = result.substring(seperator + laengeTrenner);
			
			this.author = interpret.trim();
			this.title = nameSong.trim();
		} else {
			this.author = "";
			this.title = result.trim();
		}		
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String toString() {
		if (getAuthor().trim().isEmpty() || getAuthor().equals("")) {
			return getTitle();
		} else {
			return getAuthor() + " - " + getTitle();
		}
		
	}
	
	public abstract void play() ;
	
	public abstract void togglePause();
	
	public abstract void stop ();
	
	public abstract String formatDuration ();
	
	public abstract String formatPosition ();
	
	public long getDuration() {
		return duration;
	}
	
}
