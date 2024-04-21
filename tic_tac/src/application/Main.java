package application;

import java.io.File;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Main extends Application {
//	static Stage play = new Stage();

	@Override
	public void start(Stage primaryStage) {
		try {
			// main screen
			// buttons
			// add buttons
			Button start = new Button("Start");

			// layout the buttons
			String buttonStyle = "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 40px; -fx-min-width: 120px;";
			start.setStyle(buttonStyle);

			// Set up mouseover effect for buttons
			start.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> start.setStyle(
					"-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 50px; -fx-min-width: 150px;"));
			start.addEventHandler(MouseEvent.MOUSE_EXITED, e -> start.setStyle(buttonStyle));

			// set actions
			start.setOnAction(e -> {
				Pane root = GUI();
				Scene scene1 = new Scene(root, 900, 500);
				primaryStage.setScene(scene1);
			});

			// Layout the title and buttons
			VBox vbox = new VBox(20); // Increased spacing between title and buttons
			vbox.getChildren().addAll(start);
			vbox.setAlignment(Pos.CENTER);

			// add image
			Image backgroundImage = new Image("tic.png");
			BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

			StackPane stackPane = new StackPane();
			stackPane.setBackground(new Background(background));

			// stackPane.getChildren().add(imageView);
			stackPane.getChildren().add(vbox);
			// scene
			Scene scene = new Scene(stackPane, 900, 500);
			primaryStage.setTitle("<<TIC TAC TOE>>");
			primaryStage.getIcons().add(new Image("i.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BorderPane GUI() {
		BorderPane g = new BorderPane();
		// buttons
		// add buttons
		Button random = new Button("one Player");
		Button player = new Button("Player VS player");
		Button ai = new Button("AI VS player");

		// Set up button styles
		String buttonStyle = "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; "
				+ "-fx-min-height: 70px; -fx-min-width: 120px; -fx-background-radius: 10;";

		random.setStyle(buttonStyle);
		player.setStyle(buttonStyle);
		ai.setStyle(buttonStyle);

		// Set up mouseover effect for buttons
		setMouseOverEffect(random);
		setMouseOverEffect(player);
		setMouseOverEffect(ai);
		// set actions
		random.setOnAction(e -> {

			randomP.RGUI();
		});
		player.setOnAction(e -> {
			player2.RGUI();
		});
		ai.setOnAction(e -> {
			AI.RGUI();
		});
		// ---------------
		// Create a label for the title
		Label titleLabel = new Label("~<* Select how do you want to play *>~");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 24));
		titleLabel.setTextFill(javafx.scene.paint.Color.LIGHTBLUE);

		// Set up styles for the title label
		String labelStyle = "-fx-background-color: #800080; -fx-background-radius: 10; -fx-padding: 10px;";
		titleLabel.setStyle(labelStyle);

		// Set up the title label in the layout
		g.setTop(titleLabel);
		BorderPane.setAlignment(titleLabel, Pos.CENTER);

		// Set up the scale transition for continuous font size animation
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), titleLabel);
		scaleTransition.setFromX(1.2);
		scaleTransition.setToX(0.8);
		scaleTransition.setFromY(1.2);
		scaleTransition.setToY(0.8);
		scaleTransition.setAutoReverse(true);
		scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
		scaleTransition.play();

		// Set up a fade transition for the title label
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), titleLabel);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.5);
		fadeTransition.setAutoReverse(true);
		fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
		fadeTransition.play();

		HBox vbox = new HBox(20); // Increased spacing between title and buttons
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(random, player, ai);
		g.setBottom(vbox);
		BorderPane.setAlignment(vbox, Pos.TOP_CENTER);
		// add image
		Image backgroundImage = new Image("tic.png");
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

		g.setBackground(new Background(background));

		return g;

	}

	// Method to set up mouseover effect for buttons
	private void setMouseOverEffect(Button button) {
		button.addEventHandler(MouseEvent.MOUSE_ENTERED,
				e -> button.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; "
						+ "-fx-min-height: 50px; -fx-min-width: 150px; -fx-background-radius: 15;"));
		button.addEventHandler(MouseEvent.MOUSE_EXITED,
				e -> button.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; "
						+ "-fx-min-height: 70px; -fx-min-width: 120px; -fx-background-radius: 10;"));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
