package studiplayer.ui;
import java.io.File;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.NotPlayableException;

public class Player extends Application {

	private PlayList playList;
	private boolean useCertPlayList;
	public final static String DEFAULT_PLAYLIST ="playlists/DefaultPlayList.m3u";
	
	
	public Player () {
		
	}
	
	public void setUseCertPlayList (boolean value) {
		this.useCertPlayList = value;
	}
	
	//Playlist setzen
	public void setPlayList (String pathname) {
		loadPlayList(pathname);
	}
	
	//falls playlist null oder leer ist
	public void loadPlayList (String pathname) {
		if (pathname == null || pathname.isEmpty()) {
			pathname = DEFAULT_PLAYLIST;
		}
		try {
			this.playList = new PlayList(pathname);
		} catch (Exception e) {
			System.err.println("Fehler beim Laden der Playlist: " + e.getMessage());
		}
		
	}
	
	//Java FX starten
	public static void main(String[] args) {
		launch(args);
	}
	
	
	//Fenster aufrufen
	@Override
	public void start(Stage stage) {
		if (this.useCertPlayList) {
			loadPlayList("playList.cert.m3u");
		} else {
			FileChooser fileChooser = new FileChooser();
			
			File selectedFile = fileChooser.showOpenDialog(stage);
			
			if (selectedFile != null) {
				String currentFile = selectedFile.getAbsolutePath();
				loadPlayList(currentFile);
			} else {
				loadPlayList(null);
			}
		}
		
		BorderPane hauptPane = new BorderPane();
		Scene scene = new Scene(hauptPane, 600, 400);
		
		stage.setTitle("Test");
		stage.setScene(scene);
		
		stage.show();
	}

}
