package studiplayer.ui;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studiplayer.audio.PlayList;

public class Player extends Application {

	private PlayList playList;
	private boolean useCertPlayList;
	
	public Player () {
		
	}
	
	public final static String DEFAULT_PLAYLIST () {
		return "";
	}
	
	public void setUserCertPlayList (boolean value) {
		value = this.useCertPlayList;
	}
	
	public void setPlayList (String pathname) {
		this.playList = new PlayList(pathname);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		BorderPane hauptPane = new BorderPane();
		Scene scene = new Scene(hauptPane, 600, 400);
		
		stage.setTitle("Test");
		stage.setScene(scene);
		
		stage.show();
	}

}
