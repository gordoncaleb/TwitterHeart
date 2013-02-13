package view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TwitterHeartFXApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();

		primaryStage.setScene(new Scene(root));

		HeartCanvasFX heartCanvas = new HeartCanvasFX();
		
		root.getChildren().add(heartCanvas);
		
		primaryStage.show();
	}

}
