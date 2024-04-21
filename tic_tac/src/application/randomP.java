package application;

import java.util.*;
import java.util.Optional;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class randomP {
	static Stage play = new Stage();
	public String[] shuffle;

	public int[][] m = new int[3][3];// 2 = O, 1 = X;
	public int[][] oflag = new int[3][3];
	public int[][] xflag = new int[3][3];

	static boolean turnX = true;
	static boolean end = false;
	static boolean flag = true;
	GraphicsContext gc;
//	int level;
//	int v, a, b;
	int ihold, jhold;

	static int Rounds = 1;
	static int scoure1 = 0;
	static int scoure2 = 0;

	static void RGUI() {
		String player1Name = "";
		String player2Name = "";
		int numberOfRounds = 0;

		// Create a GridPane to arrange the input fields
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		// Player 1 name input
		TextField player1NameField = new TextField();
		player1NameField.setPromptText("Enter Player 1 Name:(X)");
		gridPane.add(player1NameField, 0, 0);

		// Player 2 name input
		TextField player2NameField = new TextField();
		player2NameField.setPromptText("Enter Player 2 Name:(O)");
		gridPane.add(player2NameField, 0, 1);

		// Number of rounds input
		ChoiceBox<Integer> roundsChoiceBox = new ChoiceBox<>();
		roundsChoiceBox.getItems().addAll(1, 2, 3, 4, 5);
		roundsChoiceBox.setValue(1);
		gridPane.add(new Label("Number of Rounds:"), 0, 2);
		gridPane.add(roundsChoiceBox, 1, 2);

		// Number of rounds input
		ChoiceBox<String> startBox = new ChoiceBox<>();
		startBox.getItems().addAll("X", "O");
		startBox.setValue("X");
		gridPane.add(new Label("The Start player:"), 0, 3);
		gridPane.add(startBox, 1, 3);

		// Show the input window
		Dialog<ButtonType> inputDialog = new Dialog<>();
		inputDialog.setTitle("Player Names and Rounds");
		inputDialog.getDialogPane().setContent(gridPane);
		inputDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		// Wait for user input
		Optional<ButtonType> result = inputDialog.showAndWait();

		if (result.isPresent() && result.get() == ButtonType.OK) {
			// User clicked OK, retrieve the entered values
			player1Name = player1NameField.getText();
			player2Name = player2NameField.getText();
			numberOfRounds = roundsChoiceBox.getValue();
			if (startBox.getValue().equals("X")) {
				turnX = true;
			} else {
				turnX = false;
			}
		}

		randomP in = new randomP();

		in.playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

	}

	void playRound(Stage play, String player1Name, String player2Name, int round, int numberOfRounds) {
		//
		if (Rounds <= numberOfRounds) {
			BorderPane rootPane = new BorderPane();
			Scene s = new Scene(rootPane);

			// Canvas setup
			Canvas c = new Canvas(600, 650);
			rootPane.setCenter(c);
			gc = c.getGraphicsContext2D();
			drawboard(gc);

			// Restart button
			Button reset = new Button("Restart");
			reset.setOnAction(event -> {
				play.close();
				Main t = new Main();
				t.start(play);
			});
			HBox top = new HBox(20);
			Label Round = new Label("Round :" + round);
			Round.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

			Text text = new Text();
			text.setX(270);
			text.setY(660);
			text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

			top.getChildren().add(Round);
			rootPane.setTop(top);
			BorderPane.setAlignment(top, Pos.CENTER);

			// Player names and scores setup
			Text player1Text = new Text(player1Name);
			player1Text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
			Text sc = new Text("Score:");
			sc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

			Text player2Text = new Text(player2Name);
			player2Text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
			Text sc1 = new Text("Score:");
			sc1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

			Label player1ScoreLabel = new Label(scoure1 + "");
			Label player2ScoreLabel = new Label(scoure2 + "");
			String labelStyle = "-fx-background-color: #89CFF0; -fx-background-radius: 20; -fx-padding: 20px;";
			player1ScoreLabel.setStyle(labelStyle);
			player2ScoreLabel.setStyle(labelStyle);

			reset.setStyle("-fx-background-color: #89CFF0; -fx-background-radius: 4; -fx-padding: 10px;");
//			Round.setStyle(labelStyle);

			Image image = new Image("x.png");
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(60);
			imageView.setFitWidth(60);

			Image image2 = new Image("o.png");
			ImageView imageView2 = new ImageView(image2);
			imageView2.setFitHeight(60);
			imageView2.setFitWidth(60);

			VBox player2InfoBox = new VBox(20); // Horizontal spacing between elements
			player2InfoBox.getChildren().addAll(imageView2, player2Text, sc, player2ScoreLabel);
			player2InfoBox.setAlignment(Pos.CENTER);
			rootPane.setRight(player2InfoBox);

			VBox player1InfoBox = new VBox(20); // Horizontal spacing between elements
			player1InfoBox.getChildren().addAll(imageView, player1Text, sc1, player1ScoreLabel);
			player1InfoBox.setAlignment(Pos.CENTER);

			rootPane.setLeft(player1InfoBox);

			HBox results = new HBox(50);
			// Round number setup
			// Algerian
//			roundNumber.setFont(Font.font("Algerian", FontWeight.BOLD, FontPosture.REGULAR, 20));
			Text roundNumber = new Text("  Round" + round + "/" + numberOfRounds);
			roundNumber.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

			results.getChildren().addAll(roundNumber, reset);
			BorderPane.setAlignment(results, Pos.CENTER);

			rootPane.setBottom(results);
			// Set background color
			rootPane.setStyle("-fx-background-color: #800080;");
			randomP in = new randomP();
//			turnX = true;
			if (turnX == true) {
				s.setOnMouseClicked(e -> {
					if (e.getButton() == MouseButton.PRIMARY) {
						if (!end && turnX) {
							if (in.getcoordinate(gc, (int) e.getX(), (int) e.getY())) {
								if (in.winX()) {
									scoure1++;
									text.setText("X Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

								} else if (in.winO()) {
									scoure2++;
									text.setText("O Win!");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

								} else if (in.noWin()) {
									text.setText("Draw");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

								}
								turnX = false;
							}
							if (!turnX && !end) {
								Random random = new Random();
								turnX = true;
								// Collect empty cells
								List<int[]> emptyCells = new ArrayList<>();
								for (int i = 0; i < 3; i++) {
									for (int j = 0; j < 3; j++) {
										if (in.m[i][j] == 0) {
											emptyCells.add(new int[] { i, j });
										}
									}
								}

								// If there are empty cells, randomly select one and make a move
								if (!emptyCells.isEmpty()) {
									int[] randomCell = emptyCells.get(random.nextInt(emptyCells.size()));

									// Make a random move
									in.m[randomCell[0]][randomCell[1]] = 2;

									// Draw the move
									in.drawO(gc, randomCell[0] * 200 + 36, randomCell[1] * 200 + 36);

									if (in.winX()) {
										scoure1++;
										text.setText("X Win!");
										top.getChildren().add(text);
										end = true;

										resetAndProceedToNextRound(round);
										resetGame(gc);
										drawboard(gc);
										playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

									} else if (in.winO()) {
										scoure2++;
										text.setText("O Win!");
										top.getChildren().add(text);
										end = true;

										resetAndProceedToNextRound(round);
										resetGame(gc);
										drawboard(gc);
										playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

									} else if (in.noWin()) {
										text.setText("Draw");
										top.getChildren().add(text);
										end = true;
										resetAndProceedToNextRound(round);
										resetGame(gc);
										drawboard(gc);
										playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

									}
//									turnX = true;
								}
								turnX = true;

							}
//							turnX = true;
						}
					}
				});

			} else {
				// O turn
//				if (!turnX && !end) {
				Random random = new Random();

				// Collect empty cells
				List<int[]> emptyCells = new ArrayList<>();
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						if (in.m[i][j] == 0) {
							emptyCells.add(new int[] { i, j });
						}
					}
				}

				// If there are empty cells, randomly select one and make a move
				if (!emptyCells.isEmpty()) {
					int[] randomCell = emptyCells.get(random.nextInt(emptyCells.size()));

					// Make a random move
					in.m[randomCell[0]][randomCell[1]] = 2;

					// Draw the move
					in.drawO(gc, randomCell[0] * 200 + 36, randomCell[1] * 200 + 36);
					if (in.winX()) {
						scoure1++;
						text.setText("X Win!");
						top.getChildren().add(text);
						end = true;
						resetAndProceedToNextRound(round);
						resetGame(gc);
						drawboard(gc);
						playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

					} else if (in.winO()) {
						scoure2++;
						text.setText("O Win!");
						top.getChildren().add(text);
						end = true;
						resetAndProceedToNextRound(round);
						resetGame(gc);
						drawboard(gc);
						playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

//						playRound(play, player1Name, player2Name, Rounds, numberOfRounds);
					} else if (in.noWin()) {
						text.setText("Draw");
						top.getChildren().add(text);
						end = true;
						resetAndProceedToNextRound(round);
						resetGame(gc);
						drawboard(gc);
						playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

					}

					turnX = true;
				}


				s.setOnMouseClicked(e -> {
					if (e.getButton() == MouseButton.PRIMARY) {
						if (!end & turnX) {
							if (in.getcoordinate(gc, (int) e.getX(), (int) e.getY())) {
								if (in.winX()) {
									scoure1++;
									text.setText("X Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);
								} else if (in.getcoordinate(gc, (int) e.getX(), (int) e.getY())) {
									if (in.winO()) {
										scoure2++;
										text.setText("O Win!");
										top.getChildren().add(text);
										end = true;

										resetAndProceedToNextRound(round);
										resetGame(gc);
										drawboard(gc);
										playRound(play, player1Name, player2Name, Rounds, numberOfRounds);
//								Rounds++;
									}
								} else if (in.noWin()) {
									text.setText("Draw");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);
//							drawboard(gc);
//							Rounds++;
								}
								turnX = false;
							}

							// O turn
							if (!turnX && !end) {
//								makeRandomMove();
//								drawO(gc, in.ihold * 200 + 36, in.jhold * 200 + 36);
								Random random1 = new Random();

								// Collect empty cells
								List<int[]> emptyCells1 = new ArrayList<>();
								for (int i = 0; i < 3; i++) {
									for (int j = 0; j < 3; j++) {
										if (in.m[i][j] == 0) {
											emptyCells1.add(new int[] { i, j });
										}
									}
								}

								// If there are empty cells, randomly select one and make a move
								if (!emptyCells1.isEmpty()) {
									int[] randomCell = emptyCells1.get(random1.nextInt(emptyCells1.size()));

									// Make a random move
									in.m[randomCell[0]][randomCell[1]] = 2;

									// Draw the move
									in.drawO(gc, randomCell[0] * 200 + 36, randomCell[1] * 200 + 36);
								}
								if (winO()) {
									scoure2++;
									text.setText("O Win!");
									top.getChildren().add(text);
									end = true;
									resetGame(gc);
									resetAndProceedToNextRound(round);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);
								} else if (in.winX()) {
									scoure1++;
									text.setText("X Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);
//							Rounds++;
								}else if (in.noWin()) {
									text.setText("Draw");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, player2Name, Rounds, numberOfRounds);

								}
								turnX = true;
							}
							turnX = true;
						}
					}
				});
			}

			play.setTitle("<<Start Game>>");
			play.setScene(s);
			play.getIcons().add(new Image("i.png"));
			play.show();
		} else {
			// Determine the final winner
			String finalWinner = scoure1 > scoure2 ? player1Name : (scoure1 < scoure2 ? player2Name : "It's a Draw!");

			// Create a custom dialog pane
			DialogPane dialogPane = new DialogPane();
			dialogPane.setStyle("-fx-background-color:  #89CFF0;");

			// Set the content text
			dialogPane.setContentText("Final Winner: " + finalWinner + "\n\n" + player1Name + "'s Score: " + scoure1
					+ "\n" + player2Name + "'s Score: " + scoure2);

			// Create the alert with the custom dialog pane
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Game Over");
			alert.setHeaderText("Final Result");
			alert.setDialogPane(dialogPane);

			// Add OK button
			alert.getDialogPane().getButtonTypes().add(ButtonType.OK);

			// Show the alert
			alert.showAndWait();
			play.close();
//			resetAndProceedToNextRound(round);
			resetGame(gc);
			drawboard(gc);
			Rounds = 1;
			scoure1 = 0;
			scoure2 = 0;
//			RGUI();
//			playRound(play, player1Name, player2Name, Rounds, numberOfRounds);
		}

	}

	private void resetAndProceedToNextRound(int round) {
		Rounds++;
		// Reset the game state for the next round
		end = false;
//		resetGame(); // You need to implement a method to reset the game state

		// Wait for user input before proceeding to the next round
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Round " + round + " Result");
		alert.setHeaderText("Round " + round + " Over");
		alert.setContentText("Click OK to proceed to the next round.");
		alert.showAndWait();
	}

	// Implement a method to reset the game state
	private void resetGame(GraphicsContext gc) {
		gc.clearRect(0, 0, 600, 600);
		m = new int[3][3];
		xflag = new int[3][3];
		oflag = new int[3][3];
		shuffle = new String[] { "00", "10", "20", "01", "11", "21", "02", "12", "22" };
//		a = b = v = ihold = jhold = 0;
		turnX = true;
	}

//	void makeRandomMove() {
//		Random random = new Random();
//
//		// Collect empty cells
//		List<int[]> emptyCells = new ArrayList<>();
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				if (m[i][j] == 0) {
//					emptyCells.add(new int[] { i, j });
//				}
//			}
//		}
//
//		// If there are empty cells, randomly select one and make a move
//		if (!emptyCells.isEmpty()) {
//			int[] randomCell = emptyCells.get(random.nextInt(emptyCells.size()));
//
//			// Make a random move
//			m[randomCell[0]][randomCell[1]] = 2;
//
//			// Draw the move
//			drawO(gc, randomCell[0] * 200 + 36, randomCell[1] * 200 + 36);
//
//		}
//	}

	// draw tic tac toe board
	public static void drawboard(GraphicsContext gc) {
		gc.clearRect(0, 0, 600, 600);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Color babyBlueColor = Color.rgb(173, 216, 230);
				gc.setFill(babyBlueColor);
//				gc.setFill(Color.WHITE);
				gc.fillRect(i * 200, j * 200, 200, 200);
				gc.strokeRect(i * 200, j * 200, 200, 200);
				gc.setStroke(Color.BLACK);
			}
		}
	}

	// check if X win
	public boolean winX() {
		for (int i = 0; i < 3; i++) {
			if (m[i][0] == 1 & m[i][1] == 1 & m[i][2] == 1) {
				return true;
			}
		}
		for (int i = 0; i < 3; i++) {
			if (m[0][i] == 1 & m[1][i] == 1 & m[2][i] == 1) {
				return true;
			}
		}
		if (m[0][0] == 1 & m[1][1] == 1 & m[2][2] == 1) {
			return true;
		}
		return m[2][0] == 1 & m[1][1] == 1 & m[0][2] == 1;
	}

	// check if O win
	public boolean winO() {
		for (int i = 0; i < 3; i++) {
			if (m[i][0] == 2 & m[i][1] == 2 & m[i][2] == 2) {
				return true;
			}
		}
		for (int i = 0; i < 3; i++) {
			if (m[0][i] == 2 & m[1][i] == 2 & m[2][i] == 2) {
				return true;
			}
		}
		if (m[0][0] == 2 & m[1][1] == 2 & m[2][2] == 2) {
			return true;
		}
		return m[2][0] == 2 & m[1][1] == 2 & m[0][2] == 2;
	}

	// check if draw
	public boolean noWin() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (m[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	// find X coordinate on board
	public boolean getcoordinate(GraphicsContext gc, int X, int Y) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (X > i * 200 & X < (i + 1) * 200 & Y < (j + 1) * 200 & Y > j * 200) {
					if (m[i][j] == 0) {
						drawX(gc, i * 200 + 36, j * 200 + 36);
						m[i][j] = 1;
						return true;
					}
				}
			}
		}
		return false;
	}

	// draw X on board
	public void drawX(GraphicsContext gc, int x, int y) {
		Image im = new Image("x.png");
		gc.drawImage(im, x, y, 128, 128);
	}

	// draw O on board
	public void drawO(GraphicsContext gc, int x, int y) {
		Image im = new Image("o.png");
		gc.drawImage(im, x, y, 128, 128);
	}
}
