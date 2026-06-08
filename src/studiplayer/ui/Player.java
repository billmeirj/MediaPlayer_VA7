package studiplayer.ui;
import java.io.File;
import java.net.URL;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.*;



public class Player extends Application {

	private PlayList playList;
	private boolean useCertPlayList;
	public static final String DEFAULT_PLAYLIST ="playlists/DefaultPlayList.m3u";
	private static final String PLAYLIST_DIRECTORY = "";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = " - ";
	
	//Buttons
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Button filterButton;
	
	//Label
	private Label playListLabel;
	private Label playTimeLabel;
	private Label currentSongLabel;
	
	//Eingabe Ausgabe
	private TextField searchTextField;
	private ChoiceBox sortChoiceBox;
	
	//Tabelle
	private SongTable songTable;
	
	
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
	
	private Button createButton(String iconfile) { 
	    Button button = null; 
	    try { 
	        URL url = getClass().getResource("/icons/" + iconfile); 
	        Image icon = new Image(url.toString()); 
	        ImageView imageView = new ImageView(icon); 
	        imageView.setFitHeight(20);  
	        imageView.setFitWidth(20);  
	        button = new Button("", imageView); 
	        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); 
	        button.setStyle("-fx-background-color: #fff;"); 
	    } catch (Exception e) { 
	        System.out.println("Image " + "icons/"  
	            + iconfile + " not found!"); 
	        System.exit(-1); 
	    } 
	    return button; 
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
		
		this.filterButton = new Button("Filter");
		this.playButton = createButton("play.jpg");
		this.pauseButton = createButton("pause.jpg");
		this.stopButton = createButton("stop.jpg");
		this.nextButton = createButton("next.jpg");
		
		this.searchTextField = new TextField("");
		
		this.sortChoiceBox = new ChoiceBox<>();
		this.sortChoiceBox.getItems().addAll(SortCriterion.values());
		this.sortChoiceBox.setValue(SortCriterion.DEFAULT);
		
		if (this.playList != null && this.playList.size() > 0) {
			String currentPath = this.playList.getList().get(0).getPathname();
			this.playListLabel = new Label ("Playlist: " + currentPath);
		} else {
			this.playListLabel = new Label("Playlist: " + DEFAULT_PLAYLIST);
		}
		
		this.playTimeLabel = new Label (INITIAL_PLAY_TIME_LABEL);
		
		//current Song Name verwenden
		if(this.playList.currentAudioFile() != null) {
			this.currentSongLabel = new Label (playList.currentAudioFile().toString());
		} else {
			this.currentSongLabel = new Label (NO_CURRENT_SONG);
		}
		
		//Tabelle machen
		this.songTable = new SongTable(this.playList);
		
		HBox filterBox = new HBox();
		filterBox.setSpacing(10);
		
		Label searchLabel = new Label("Suchtext: ");
		Label sortLabel = new Label("Sortierung: ");
		
		//Filterbox befüllen
		filterBox.getChildren().addAll(searchLabel, this.searchTextField, sortLabel, 
										this.sortChoiceBox, this.filterButton);
		
		//Pane erstellen
		TitledPane titledPane = new TitledPane("Filter", filterBox);
		
		BorderPane hauptPane = new BorderPane();
		Scene scene = new Scene(hauptPane, 600, 400);
		
		stage.setTitle("Test");
		stage.setScene(scene);
		
		stage.show();
	}

}
