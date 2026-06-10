package studiplayer.ui;
import java.io.File;
import java.net.URL;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
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
	
	//Thread Variblen deklarieren
	private PlayerThread playerThread;
	private TimerThread timerThread;
	
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
			this.playList = new PlayList();
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
	
	//Hilfsmethode zum Aktualisieren der Zustände von Buttons und Labels
	//Buttons
	private void setButtonStates (boolean playButtonState, boolean pauseButtonState, 
									boolean stopButtonState, boolean nextButtonState) {
		this.playButton.setDisable(playButtonState);
		this.pauseButton.setDisable(pauseButtonState);
		this.stopButton.setDisable(stopButtonState);
		this.nextButton.setDisable(nextButtonState);
	}
	
	//Label
	private void updateSongInfo (AudioFile af, String time) {
		Platform.runLater(() -> {
			//timeLabel aktualisieren
			this.playTimeLabel.setText(time);
			
			//SongInfo aktualisieren
			if (af == null) {
				this.currentSongLabel.setText(NO_CURRENT_SONG);
			} else {
				this.currentSongLabel.setText(af.toString());
			}
		});
	}
	
	//Player Logik schreiben 
	//Play-Button
	//Button true, wenn gerade nicht verwendbar
	//Button false, wenn gerade verwendbar
	private void playCurrentSong() {
		AudioFile current = this.playList.currentAudioFile();
		
		if(current != null) {
			System.out.println("Playing " + current.toString());
			System.out.println("Filename is " + current.getFilename()); //vielleicht auch getPathname()
			
			if(this.playerThread != null) {
				
				//aus der Pause holen
				current.togglePause();
				
				//aktuelle Position beibehalten
				updateSongInfo(current, current.formatPosition());
			} else {
				
			//song info updaten, auch Position
			updateSongInfo(current, INITIAL_PLAY_TIME_LABEL);
			}
			
			//labels richtig setzen
			setButtonStates(true, false, false, false);
		}
		
		//Steuermethode start mit false aufrufen => Timer und Player erzugt und starten
		startThreads(false);
	}
	
	//Pause-Button
	private void pauseCurrentSong() {
		AudioFile current = this.playList.currentAudioFile();
		
		if(current != null) {
			System.out.println("Pausing " + current.toString());
			System.out.println("Filename is " + current.getFilename());
		}
		
		current.togglePause();
		setButtonStates(true, true, false, false);
		//Steuermethode terminate mit true aufrufen => nur Timer stoppt
		terminateThreads(true);
	}
	
	//Stop-Button
	private void stopCurrentSong() {
		AudioFile current = this.playList.currentAudioFile();
		
		if(current != null) {
			System.out.println("Stopping " + current.toString());
			System.out.println("Filename is " + current.getFilename());
			
			setButtonStates(false, true, true, false);
			
			updateSongInfo(current, INITIAL_PLAY_TIME_LABEL);
		}
		
		//Steuermethode terminate mit false aufrufen => Timer und Player stoppen
		terminateThreads(false);
	}
	
	//Next- Button
	private void nextSong() {
		System.out.println("Switching to next audio file: stopped = false, paused = true");
				
		//aktuellen Song stoppen
		//stopCurrentSong();
		
		//Steuermethode terminate mit false aufrufen => Timer und Player stoppen
		//Threads sauber beenden bevor neuer Song startet
		terminateThreads(false);
		
		//nächsten Song laden
		this.playList.nextSong();
		
		//jetzt aktuellen Song abspielen
		playCurrentSong();
	}
	
	//Hilfsmethoden für Steuerung
	//startThreads
	private void startThreads(boolean onlyTimer) {
		
		//Starten von timerThread
		if(this.timerThread == null) {
			
			//Thread erzeugen und zuweisen
			this.timerThread = new TimerThread();
			
			//Thread starten
			this.timerThread.start();
		}
		
		//Starten von playerThread
		if(onlyTimer == false && this.playerThread == null) {
			this.playerThread = new PlayerThread();
			this.playerThread.start();
		}
	}
	
	//terminateThreads
	private void terminateThreads(boolean onlyTimer) {
		
		//timerThread stoppen
		if(this.timerThread != null) {
			//stoppen
			this.timerThread.terminate();
			//auf null setzen, weil gestoppt wurde
			this.timerThread = null;
		}
		
		//playerThread stoppen
		if(onlyTimer == false && this.playerThread != null) {
			this.playerThread.terminate();
			this.playerThread = null;
		}
	}
	
	//Fenster aufrufen
	@Override
	public void start (Stage stage) throws Exception {
		if (this.useCertPlayList && this.playList == null) {
			loadPlayList("playList.cert.m3u");
		} else if(this.playList == null){
			FileChooser fileChooser = new FileChooser();
			
			File selectedFile = fileChooser.showOpenDialog(stage);
			
			if (selectedFile != null) {
				String currentFile = selectedFile.getAbsolutePath();
				loadPlayList(currentFile);
			} else {
				loadPlayList(null);
			}
		}
		
		this.filterButton = new Button("Anzeigen");
		this.playButton = createButton("play.jpg");
		this.pauseButton = createButton("pause.jpg");
		this.stopButton = createButton("stop.jpg");
		this.nextButton = createButton("next.jpg");
		
		setButtonStates(false, true, true, false);
		
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
		if(this.playList != null && this.playList.currentAudioFile() != null) {
			this.currentSongLabel = new Label (playList.currentAudioFile().toString());
		} else {
			this.currentSongLabel = new Label (NO_CURRENT_SONG);
		}
		
		//Tabelle machen
		if(this.playList == null) {
			this.playList = new PlayList(); // leere Playlis
		}
		
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
		
		//Buttonbereich bauen 
		//Info-Grid
		GridPane infoGrid = new GridPane();
		infoGrid.setHgap(10);
		infoGrid.setVgap(5);
		
		//gridInfo.add(new Label("...: "), Spalte, Zeile)
		//gridInfo.add(..., Spalte, Zeile)
		//Playlist
		infoGrid.add(new Label("Playlist: "), 0, 0); //links Text
		infoGrid.add(this.playListLabel, 1, 0); //rechts Wert
		
		//Song
		infoGrid.add(new Label("Aktueller Song: "), 0, 1);
		infoGrid.add(this.currentSongLabel, 1, 1);
		
		//Spielzeit
		infoGrid.add(new Label("Spielzeit: "), 0, 2);
		infoGrid.add(this.playTimeLabel, 1, 2);
		
		//ButtonBox einfügen
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.getChildren().addAll(this.playButton, this.pauseButton, this.stopButton,
										this.nextButton);
		buttonBox.setAlignment(Pos.CENTER);
		
		//Container zusammenführen
		VBox ContainerBottom = new VBox();
		ContainerBottom.setSpacing(15);
		ContainerBottom.getChildren().addAll(infoGrid, buttonBox);
		
		BorderPane hauptPane = new BorderPane();
		
		//Segmente fertigstelle
		hauptPane.setTop(titledPane);
		hauptPane.setCenter(this.songTable);
		hauptPane.setBottom(ContainerBottom);
		
		Scene scene = new Scene(hauptPane, 600, 400);
		
		stage.setTitle("StudiPlayer");
		stage.setScene(scene);
		
		hauptPane.setPadding(new Insets(10));
		
		//Eventhandling für Anzeigen-Button
		this.filterButton.setOnAction(e -> { 
			
			//Suchtext auslesen
			String seatchText = this.searchTextField.getText();
			//Sortierkriterium auslesen
			SortCriterion criterion = (SortCriterion) this.sortChoiceBox.getValue();
			
			//Suchtext übergeben
			this.playList.setSearch(seatchText);
			//Sortierung übergeben
			this.playList.setSortCriterion(criterion);
			
			//Tabelle aktualiesieren
			this.songTable.refreshSongs();
		});
		
		//Buttons mit Event-Handler verknüpfen 
		//play
		playButton.setOnAction(e -> {
			playCurrentSong();
		});
		
		//pause
		pauseButton.setOnAction(e -> {
			pauseCurrentSong();
		});
		
		//stop
		stopButton.setOnAction(e -> {
			stopCurrentSong();
		});
		
		//next
		nextButton.setOnAction(e -> {
			nextSong();
		});
		
		//Tabellen-Klick handler einrichten
		this.songTable.setRowSelectionHandler(e -> {
			Song selectedSong = this.songTable.getSelectionModel().getSelectedItem();
			
			if(selectedSong != null) {
				
				//song abbrechen
				terminateThreads(false);
				
				//AudioFile laden
				AudioFile selectedFile = selectedSong.getAudioFile();
				
				//zu neuem Song springen
				this.playList.jumpToAudioFile(selectedFile);
				
				//jetzigen Song abspielen
				playCurrentSong();
			}
			
		});
		
		
		//Fenster anzeigen
		stage.show();		
	}
	
	//playerThread innere Klasse entwerfen
	//erbt von threads
	private class PlayerThread extends Thread {
		
		private boolean stopped = false;
		
		//stoppen
		public void terminate() {
			AudioFile currentSong = playList.currentAudioFile();
			
			stopped = true;
			if(currentSong != null) {
				currentSong.stop();
			}
		}
		
		//starten
		@Override
		public void run() {
			while(!stopped) {
				AudioFile currentFile = playList.currentAudioFile();
				
				//abbruch wenn null
				if(currentFile == null) {
					break;
				}
				
				Platform.runLater(() -> {
					songTable.selectSong(currentFile);
				});
				
				try {
					currentFile.play();
				} catch (NotPlayableException e){
					e.printStackTrace();
				}
				
				if(!stopped) {
					playList.nextSong();
				}
			}
		}
	}
	
	//timerthread innere Klasse entwerfen
	private class TimerThread extends Thread {
		private boolean stopped = false;
		
		//stoppen und schlafen legen
		public void terminate() {
			stopped = true;
			//aufwecken, falls er schläft
			this.interrupt();
		}
		
		//starten
		@Override
		public void run() {
			while(!stopped) {
				//aktuelles Lied holen
				AudioFile currentFile = playList.currentAudioFile();
				
				if(currentFile == null) {
					updateSongInfo(null, INITIAL_PLAY_TIME_LABEL);
				} else {
					updateSongInfo(currentFile, currentFile.formatPosition());
				}
				
				//Thread schlafen legen
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	}
}
