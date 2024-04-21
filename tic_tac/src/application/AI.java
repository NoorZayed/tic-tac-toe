package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

public class AI {
	static Stage play = new Stage();
	public String[] shuffle;

	public int[][] m = new int[3][3];// 2 = O, 1 = X;
	public int[][] oflag = new int[3][3];
	public int[][] xflag = new int[3][3];

	static boolean turnX = true;
	static boolean end = false;
	static boolean flag = true;
	GraphicsContext gc;
	int v, a, b;
	int besti, bestj;
	StringBuilder movesInfoBuilder = new StringBuilder();
	int[][] possibleMoves;
	static TextArea area;
	static int Rounds = 1;
	static int scoure1 = 0;
	static int scoure2 = 0;
	int num = 9;
	int bestMoveRow = 0;
	int bestMoveCol = 0;

	static void RGUI() {
		String player1Name = "";
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
			numberOfRounds = roundsChoiceBox.getValue();
			if (startBox.getValue().equals("X")) {
				turnX = true;
			} else {
				turnX = false;
			}
		}

		AI in = new AI();
		in.playRound(play, player1Name, Rounds, numberOfRounds);
	}

	void playRound(Stage play, String player1Name, int round, int numberOfRounds) {
		//
		String player2Name = "AI";
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

			Text player2Text = new Text("AI");
			player2Text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
			Text sc1 = new Text("Score:");
			sc1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

			Label player1ScoreLabel = new Label(scoure1 + "");
			Label player2ScoreLabel = new Label(scoure2 + "");
			String labelStyle = " -fx-background-radius: 20; -fx-padding: 20px;";
			player1ScoreLabel.setStyle(labelStyle);
			player2ScoreLabel.setStyle(labelStyle);
			player1ScoreLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			player2ScoreLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

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

			area = new TextArea();
			area.setPrefHeight(160);
			area.setPrefWidth(180);
			area.setEditable(false);
//			area.setBackground(Color.GRAY);
			VBox player2InfoBox = new VBox(20); // Horizontal spacing between elements

			player2InfoBox.getChildren().addAll(imageView2, player2Text, sc, player2ScoreLabel, area);
			player2InfoBox.setAlignment(Pos.CENTER);
			rootPane.setRight(player2InfoBox);

			VBox player1InfoBox = new VBox(20); // Horizontal spacing between elements
			player1InfoBox.getChildren().addAll(imageView, player1Text, sc1, player1ScoreLabel);
			player1InfoBox.setAlignment(Pos.CENTER);

			rootPane.setLeft(player1InfoBox);

			HBox results = new HBox(50);
			// Round number setup
			Text roundNumber = new Text("  Round" + round + "/" + numberOfRounds);
			roundNumber.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

			results.getChildren().addAll(roundNumber, reset);
			BorderPane.setAlignment(results, Pos.CENTER);

			rootPane.setBottom(results);
			// Set background color
			rootPane.setStyle("-fx-background-color: #800080;");
			AI in = new AI();
			if (turnX == true) {
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
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.winO()) {
									scoure2++;
									text.setText("O Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.noWin()) {
									text.setText("Draw");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							drawboard(gc);
//							Rounds++;
								}
								turnX = false;
							}
							// O turn
							if (!turnX & !end) {
//								findBestMoveForO();
								StringBuilder messages = new StringBuilder("Hint!!\n Empty Cell Probabilities:\n");

								// Randomize AI's first move
								in.a = Integer.MIN_VALUE;
								in.b = Integer.MAX_VALUE;

								for (int i2 = 0; i2 < 3; i2++) {
									for (int j2 = 0; j2 < 3; j2++) {
										if (in.m[i2][j2] == 0) {
											in.m[i2][j2] = 2;
											turnX = true;
											in.v = in.mini_max_algo(0, in.a, in.b, 7);

											// Undo the move
											in.m[i2][j2] = 0;

											if (in.v < in.b) {
												in.b = in.v;
												in.besti = i2;
												in.bestj = j2;
											}
											// Calculate probabilities based on the evaluation result
											int winProbability = (in.v == 1) ? 1 : 0;
											int drawProbability = (in.v == 0) ? 1 : 0;
											int lossProbability = (in.v == -1) ? 1 : 0;

											messages.append("Row ").append(i2).append(", Column ").append(j2)
													.append(": Probability -> Draw: ").append(drawProbability)
													.append(", loss: ").append(winProbability).append(", win: ")
													.append(lossProbability).append("\n");

										}
									}
								}
								// Make the best move
								in.m[in.besti][in.bestj] = 2;
								System.out.println("O played at Row " + in.besti + ", Column " + in.bestj);
								messages.append("O played at Row ").append(in.besti).append(", Column ")
										.append(in.bestj);
								System.out.println(messages.toString()); // Print the accumulated messages
								// Show alert with accumulated messages
								showAlertWithMessages(messages);
								in.drawO(gc, in.besti * 200 + 36, in.bestj * 200 + 36);

								if (in.winX()) {
									scoure1++;
									text.setText("X Win!");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.winO()) {
									scoure2++;
									text.setText("O Win!");
									top.getChildren().add(text);
									end = true;
								
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.noWin()) {
									text.setText("Draw");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							drawboard(gc);
//							Rounds++;
								}
								turnX = true;
							}
						}
					}
				});
			} else {
				// User chose "O", so it's AI's turn (randomized starting move)
				// Randomize AI's first move
				StringBuilder messages = new StringBuilder("Hint!!  Empty Cell Probabilities:\n");

				in.a = Integer.MIN_VALUE;
				in.b = Integer.MAX_VALUE;

				for (int i2 = 0; i2 < 3; i2++) {
					for (int j2 = 0; j2 < 3; j2++) {
						if (in.m[i2][j2] == 0) {
							in.m[i2][j2] = 2;
							in.turnX = true;
							in.v = in.mini_max_algo(0, in.a, in.b, 7);

							// Undo the move
							in.m[i2][j2] = 0;

							if (in.v < in.b) {
								in.b = in.v;
								in.besti = i2;
								in.bestj = j2;
							}
//							// Calculate probabilities based on the evaluation result
//			                int winProbability = (in.v == 1) ? 1 : 0;
//			                int drawProbability = (in.v == 0) ? 1 : 0;
//			                int lossProbability = (in.v == -1) ? 1 : 0;
//
//			                messages.append("Row ").append(i2).append(", Column ").append(j2)
//			                        .append(": Probability -> Draw: ").append(drawProbability)
//			                        .append(", Win: ").append(winProbability)
//			                        .append(", Loss: ").append(lossProbability)
//			                        .append("\n");

						}
					}
				}
				// Make the best move
				in.m[in.besti][in.bestj] = 2;
				System.out.println("O played at Row " + in.besti + ", Column " + in.bestj);
//				 messages.append("O played at Row ").append(in.besti).append(", Column ").append(in.bestj);
//				System.out.println(messages.toString()); // Print the accumulated messages
				// Show alert with accumulated messages
//				showAlertWithMessages(messages);
				in.drawO(gc, in.besti * 200 + 36, in.bestj * 200 + 36);

				if (in.winX()) {
					scoure1++;
					text.setText("X Win!");
					top.getChildren().add(text);
					end = true;

					resetAndProceedToNextRound(round);
					drawboard(gc);
					playRound(play, player1Name, Rounds, numberOfRounds);
//			Rounds++;
				} else if (in.winO()) {
					scoure2++;
					text.setText("O Win!");
					top.getChildren().add(text);
					end = true;

					resetAndProceedToNextRound(round);
					drawboard(gc);
					playRound(play, player1Name, Rounds, numberOfRounds);
//			Rounds++;
				} else if (in.noWin()) {
					text.setText("Draw");
					top.getChildren().add(text);
					end = true;
					resetAndProceedToNextRound(round);
					resetGame(gc);
					drawboard(gc);
					playRound(play, player1Name, Rounds, numberOfRounds);
//			drawboard(gc);
//			Rounds++;
				}
				turnX = true;

				// Set up mouse click event for the user to make moves
				s.setOnMouseClicked(e -> {
					System.out.println("Mouse clicked");
					if (e.getButton() == MouseButton.PRIMARY) {
						System.out.println("Mouse clicked1");

						if (!end & turnX) {
							System.out.println("x turn");

							if (in.getcoordinate(gc, (int) e.getX(), (int) e.getY())) {
								if (in.winX()) {
									scoure1++;
									text.setText("X Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.winO()) {
									scoure2++;
									text.setText("O Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.noWin()) {
									text.setText("Draw");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							drawboard(gc);
//							Rounds++;
								}
								turnX = false;
							}
							// O turn
							if (!turnX & !end) {

								in.a = Integer.MIN_VALUE;
								in.b = Integer.MAX_VALUE;

								for (int i2 = 0; i2 < 3; i2++) {
									for (int j2 = 0; j2 < 3; j2++) {
										if (in.m[i2][j2] == 0) {
											in.m[i2][j2] = 2;

											in.turnX = true;
											in.v = in.mini_max_algo(0, in.a, in.b, 7);
//											showMoveMessages();
											// Undo the move
											in.m[i2][j2] = 0;

											if (in.v < in.b) {
												in.b = in.v;
//												in.v = in.b;
												in.besti = i2;
												in.bestj = j2;
											}
											// Calculate probabilities based on the evaluation result
											int winProbability = (in.v == 1) ? 1 : 0;
											int drawProbability = (in.v == 0) ? 1 : 0;
											int lossProbability = (in.v == -1) ? 1 : 0;

											messages.append("Row ").append(i2).append(", Column ").append(j2)
													.append(": Probability -> Draw: ").append(drawProbability)
													.append(", loss: ").append(winProbability).append(", win: ")
													.append(lossProbability).append("\n");

										}
									}
								}
								// Make the best move
								in.m[in.besti][in.bestj] = 2;

								System.out.println("O played at Row " + in.besti + ", Column " + in.bestj);
								messages.append("O played at Row ").append(in.besti).append(", Column ")
										.append(in.bestj);
								System.out.println(messages.toString()); // Print the accumulated messages
								// Show alert with accumulated messages
								showAlertWithMessages(messages);
								in.drawO(gc, in.besti * 200 + 36, in.bestj * 200 + 36);

								if (in.winX()) {
									scoure1++;
									text.setText("X Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.winO()) {
									scoure2++;
									text.setText("O Win!");
									top.getChildren().add(text);
									end = true;

									resetAndProceedToNextRound(round);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							Rounds++;
								} else if (in.noWin()) {
									text.setText("Draw");
									top.getChildren().add(text);
									end = true;
									resetAndProceedToNextRound(round);
									resetGame(gc);
									drawboard(gc);
									playRound(play, player1Name, Rounds, numberOfRounds);
//							drawboard(gc);
//							Rounds++;
								}
								turnX = true;
							}

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
			play.close();
			// Show the alert
			alert.showAndWait();
			resetGame(gc);
			drawboard(gc);
			Rounds = 1;
			scoure1 = 0;
			scoure2 = 0;
		}

	}

	private void resetAndProceedToNextRound(int round) {
		Rounds++;
		// Reset the game state for the next round
		end = false;
//		resetGame(); // a method to reset the game state

		// Wait for user input before proceeding to the next round
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Round " + round + " Result");
		alert.setHeaderText("Round " + round + " Over");
		alert.setContentText("Click OK to proceed to the next round.");
		alert.showAndWait();
	}

	int mini_max_algo(int depth, int a, int b, int level) {
		int[][] cellSelectionCount = new int[3][3]; // Counter for each cell

//		Check if X has won the game (winX()). If true, return 1 as X is the maximizing player.
		if (winX()) {
			return 1;
		}
//		Check if O has won the game (winO()). If true, return -1 as O is the minimizing player.
		if (winO()) {
			return -1;
		}
//		Check if the board is full (flag is set to true). If true, return 0 as it's a draw.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (m[i][j] == 0) {
					flag = false;
				}
			}
		}
		if (flag) {
			return 0;
		}
		flag = true;
//		Depth Limit Reached
//			Check if the current depth has reached the specified level. If true, return 0
		if (depth == level)

			return 0;
//		Minimax Algorithm
		if (turnX) {
			turnX = false;
//			ArrayList<int[]> moves = new ArrayList<int[]>();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (m[i][j] == 0 & xflag[i][j] == 0) {
//						int[] place = { i, j };
//						moves.add(place);
//						System.out.println(place); // Make a move
						m[i][j] = 1;
						xflag[i][j] = 1;
						turnX = false;
//						System.out.println("else turn o");

						// Recursive call
						v = mini_max_algo(depth + 1, a, b, level);

						// Update 
						a = Math.max(v, a);

						// Update cell selection count
						cellSelectionCount[i][j]++;

						// Undo the move
						m[i][j] = 0;

					}
				}

			}
			// Reset flags 
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					xflag[i][j] = 0;
				}
			}
			if (depth == 0) {
				System.out.println("Cell Selection Percentages:");
				printCellSelectionPercentages(cellSelectionCount, area);
			}

			return a;

		} else {

//			minimizing player.
			turnX = true;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (m[i][j] == 0 & oflag[i][j] == 0) {

						// Make a move
						m[i][j] = 2;
						oflag[i][j] = 1;
						turnX = true;

						// Recursive call
						v = mini_max_algo(depth + 1, a, b, level);
						// Update
						b = Math.min(v, b);

						// Undo the move
						m[i][j] = 0;

					}
				}

			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					oflag[i][j] = 0;
				}
			}

			return b;
		}

	}

	void printCellSelectionPercentages(int[][] cellSelectionCount, TextArea area) {
		area.setText("");
		int totalSelections = 0;
		StringBuilder info = new StringBuilder("Hint!!\\nPossible Moves Info:\n\n");

		// Calculate the total number of selections
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				totalSelections += cellSelectionCount[i][j];
			}
		}

		// Print the percentages
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int percentage = (int) Math.round((double) cellSelectionCount[i][j] / totalSelections * 100);
				// Draw the percentage on the canvas
				System.out.println("Cell [" + i + "][" + j + "]: " + percentage + "%");
				info.append("Move (").append(i).append(", ").append(j).append("): ").append("Percentage = ")
						.append(percentage).append("%\n");
				area.appendText("Move (" + i + ", " + j + "): Percentage = " + String.valueOf(percentage) + "%\n");

			}
		}
	}

	// Method to display an alert with information about all possible moves and
	// their scores
	void showPossibleMovesInfo() {
		StringBuilder info = new StringBuilder("Possible Moves Info:\n\n");

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (m[i][j] == 0) {
					m[i][j] = turnX ? 1 : 2;
					int score = mini_max_algo(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
					m[i][j] = 0;

					info.append("Move (").append(i).append(", ").append(j).append("): ").append("Score = ")
							.append(score).append("\n");
				}
			}
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Possible Moves Information");
		alert.setHeaderText(null);
		alert.setContentText(info.toString());
		alert.showAndWait();
	}

	// Implement a method to reset the game state
	private void resetGame(GraphicsContext gc) {
		gc.clearRect(0, 0, 600, 600);
		m = new int[3][3];
		xflag = new int[3][3];
		oflag = new int[3][3];
		a = b = v = besti = bestj = 0;
		turnX = true;
	}

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

//	// max
//	public int max(int a, int b) {
//		if (a >= b) {
//			return a;
//		} else {
//			return b;
//		}
//	}
//
//	// min
//	public int min(int a, int b) {
//		if (a <= b) {
//			return a;
//		} else {
//			return b;
//		}
//	}

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

	private void showAlertWithMessages(StringBuilder messages) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AI Move");
		alert.setHeaderText(null);
		alert.setContentText(messages.toString());
		alert.showAndWait();
		messages.setLength(0);
	}

	void drawText(GraphicsContext gc, String text, double x, double y) {
		gc.clearRect(0, 0, 600, 600); // Clear the canvas

		// Set font and color for the text
		gc.setFont(Font.font("Arial", 18));
		gc.setFill(Color.BLACK);

		// Draw the text on the canvas
		gc.fillText(text, x, y);
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
