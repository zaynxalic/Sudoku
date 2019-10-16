package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//use websource http://www.geometer.org/mathcircles/sudoku.pdf and https://medium.com/@rossharrison/generating-sudoku-boards-pt-1-structures-algorithms-a1e62feeb32
public class Main extends Application {
	static boolean startgame = false;
	static boolean pausethegame = false;
	Stage initalWindow = new Stage();
	static Stage sudokuWindow = new Stage();
	Scene sudokuScene;
	Scene orginalScene;
	boolean choosethefield = false;
	String id;
	String column;
	String row;
	// check if sudoku is complete
	static boolean complete = true;
	int[][] storeRandomArray = new int[9][9];
	// use two dimensional storeRandomArray to store the generation of random array
	// made by createSudoku
	int[][] storeArrayCopy = new int [9][9];
	int[][] storeArray = new int[9][9];
	//storearray is to store the sudoku game array
	Button[][] sudoku = new Button[9][9];

	
	int[][] seedArray = { { 5, 3, 4, 6, 7, 8, 9, 1, 2 }, { 6, 7, 2, 1, 9, 5, 3, 4, 8 }, { 1, 9, 8, 3, 4, 2, 5, 6, 7 },
			{ 8, 5, 9, 7, 6, 1, 4, 2, 3 }, { 4, 2, 6, 8, 5, 3, 7, 9, 1 }, { 7, 1, 3, 9, 2, 4, 8, 5, 6 },
			{ 9, 6, 1, 5, 3, 7, 2, 8, 4 }, { 2, 8, 7, 4, 1, 9, 6, 3, 5 }, { 3, 4, 5, 2, 8, 6, 1, 7, 9 } };

	/*
	 * Generate a random Sudoku matrix from a one-dimensional array and the original
	 * array. Traverse the data in the two-dimensional array, find the position of
	 * the current value in a one-dimensional array, and assign the current position
	 * of the one-dimensional array to the current two-dimensional position. In the
	 * array. The purpose is to cyclically exchange the 9 data in a random order
	 * according to the one-dimensional array to generate a random Sudoku matrix.
	 */

	// now we generate a one-dimensional random arraylist
	public ArrayList<Integer> randomOneDimensional() {
		ArrayList<Integer> oneDimensionalarray = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++) {
			oneDimensionalarray.add(i + 1);
		}
		Collections.shuffle(oneDimensionalarray);
		return oneDimensionalarray;
	}

	// create sudoku list by using the seedarray and randomlist generated above
	public int[][] createSudoku(int[][] seedArray, ArrayList<Integer> randomList) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 9; k++) {
					if (seedArray[i][j] == randomList.get(k)) {
						seedArray[i][j] = randomList.get((k + 1) % 9);
						break;
					}
				}
			}
		}
		return seedArray;
	}

	// checksudokustatus by checking users' array is equal to the array or not
	public boolean checkSudokuStatus(int[][] sudoku) {
		boolean flag = true;
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(sudoku[i][j] !=Math.abs(storeArray[i][j])) {
					flag = false;
				}
			}
		}
		return flag;
	}	

	
	/*the information we get from the internet that the least number of the sudoku
	which contains only one solution is 17,so we randomly remove 40 number of soduku and
	generate the sudoku by removing some numbers and after the number has been generated
	then use the boolean flag which is defined in the stage s to check the sudoku has a
	unique solution or not*/
	public int[][] generateSudoku(int[][] originalSudoku) {
		ArrayList<String> removenumberarray = new ArrayList<String>();
		ArrayList<Integer> columnArray = new ArrayList<Integer>();
		ArrayList<Integer> rowArray = new ArrayList<Integer>();
		int[][] changedSudoku = new int [9][9];
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				changedSudoku [i][j] = originalSudoku[i][j];
			}
		}
		int column = 0;
		int row = 0;
		for (int i = 0; i < 40;i++) {
			// randomly generate the row and column which is wanted to be removed
			Random generate = new Random();
			if (removenumberarray != null) {
				// check the removearray if it has the same element in the removenumberarray
				while (removenumberarray.contains(row + "," + column)) {
					column = generate.nextInt(9);
					row = generate.nextInt(9);
				}//randomly generate the column and row elements divided by comma
				removenumberarray.add(row + "," + column);
			} else {
				column = generate.nextInt(9);
				row = generate.nextInt(9);
				removenumberarray.add(row + "," + column);
			}
		}//now we use 'split' to split the row and column array
		for (int i = 0; i < 40; i++) {
			rowArray.add(Integer.parseInt(removenumberarray.get(i).split(",")[0]));
			columnArray.add(Integer.parseInt(removenumberarray.get(i).split(",")[1]));
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 40; k++) {
					if (rowArray.get(k) == i && columnArray.get(k) == j) {
						// if the element is more than 0, change elements to the opposite number
						changedSudoku[i][j] = -originalSudoku[i][j];
					}
				}
			}
		}
		return changedSudoku;
	}
	
	// create stage of sudoku
	public void sudoku(Stage s) throws Exception {
		try {
			VBox sudoku_vb = new VBox();
			sudokuScene = new Scene(sudoku_vb, 450, 500); // the next scene
			HBox setOfSudoku = new HBox();
			HBox[] hbSudoku = new HBox[9]; // 9 row of the sudoku
			for (int i = 0; i < 9; i++) {
				hbSudoku[i] = new HBox();
			}
			// set a 9-3*3 sudoku regions
			Button[][] sudoku = new Button[9][9];// set size of Button
			int[][] storeArrayCopy = new int[9][9];
			
			// at first we generate a complete sudoku then remove some number to demonstate
			// to the users
			Main ma = new Main();
			storeRandomArray = ma.createSudoku(seedArray, ma.randomOneDimensional());
		
			// activate the randomlymovethenumber function
			// generate storearray which is directly show the sudoku to users
			storeArrayCopy = generateSudoku(storeRandomArray);
		
			/* if the sudoku solution doesn't fit the answer we get from the 
			 'solve'(in the Sudoku java file) then it will frequently random the number
			 because it definitely doesn't have a unique solution. However, if the count of
			 the sudoku solution is more than 1 then again random the number
			 */
			boolean flag = Sudoku.isEqualsToSudokuSolution(storeArrayCopy) &&
					Sudoku.countOfSudoku(storeArrayCopy)==1;
			while((!flag)) {
				storeArrayCopy = generateSudoku(storeRandomArray);
				flag = Sudoku.isEqualsToSudokuSolution(storeArrayCopy) &&
						Sudoku.countOfSudoku(storeArrayCopy)==1;
			}
			storeArray = storeArrayCopy.clone();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					sudoku[i][j] = new Button();// set the size of every Button 30*30
					sudoku[i][j].setPrefSize(30, 30);
					HBox.setMargin(sudoku[i][j], new Insets(2)); // set margin 2
					// set id which is "row" + "," + "column"
					sudoku[i][j].setId(Integer.toString(i) + "," + Integer.toString(j));
					if ((storeArray[i][j]) < 0) {
						sudoku[i][j].setText("");
					} else {
						sudoku[i][j].setText(Integer.toString(storeArray[i][j]));
						sudoku[i][j].getStyleClass().add("given");
						sudoku[i][j].setDisable(true);
					}
					// set css style hover and focused
					sudoku[i][j].getStyleClass().add("sudokubutton");
					hbSudoku[i].getChildren().add(sudoku[i][j]);
				}
			}
			// center the hbox of sudoku
			for (int i = 0; i < 9; i++) {
				hbSudoku[i].setAlignment(Pos.CENTER);
			}

			HBox[] hbtnset = new HBox[3];
			for (int i = 0; i < 3; i++) {
				hbtnset[i] = new HBox();
				hbtnset[i].setAlignment(Pos.CENTER);
				hbtnset[i].setPadding(new Insets(5));
			}
			HBox hbtncheck = new HBox();
			VBox vbtnleft = new VBox();
			Button hint = new Button("Hint");
			hint.setPrefSize(70, 15);
			// set the size of hint clear pause button are all 50*15
			Button clear = new Button("Clear");
			clear.setPrefSize(70, 15);
			Button pause = new Button("Pause");
			pause.setPrefSize(70, 15);
			Button[] number = new Button[9];
			HBox.setMargin(hint, new Insets(10));
			HBox.setMargin(clear, new Insets(10));
			HBox.setMargin(pause, new Insets(10));

			// button of the number
			for (int i = 0; i < 9; i++) {
				number[i] = new Button(Integer.toString(i + 1));
				number[i].setPrefSize(30, 25);
				HBox.setMargin(number[i], new Insets(10));
			}
			Button btncheck = new Button("Check");
			btncheck.setPrefSize(70, 70);
			hbtncheck.getChildren().add(btncheck);
			HBox.setMargin(btncheck, new Insets(50));
			setOfSudoku.setAlignment(Pos.CENTER);
			// set children
			hbtnset[0].getChildren().addAll(hint, number[0], number[1], number[2]);
			hbtnset[1].getChildren().addAll(clear, number[3], number[4], number[5]);
			hbtnset[2].getChildren().addAll(pause, number[6], number[7], number[8]);
			vbtnleft.getChildren().addAll(hbtnset[0], hbtnset[1], hbtnset[2]);
			setOfSudoku.getChildren().addAll(vbtnleft, hbtncheck);
			// add every row of Button and the setofsudoku
			sudoku_vb.getChildren().addAll(hbSudoku[0], hbSudoku[1], hbSudoku[2], hbSudoku[3], hbSudoku[4], hbSudoku[5],
					hbSudoku[6], hbSudoku[7], hbSudoku[8], setOfSudoku);
			// set padding insets : 10
			sudoku_vb.setPadding(new Insets(10));

			// if one of the 9*9 button has been chosen, then locate the button and get the
			// button number
			// corresponding to the original sudoku
			final EventHandler<ActionEvent> getHint = new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					if (choosethefield) {
						String value = Integer
								.toString(Math.abs(storeRandomArray[Integer.parseInt(row)][Integer.parseInt(column)]));
						sudoku[Integer.parseInt(row)][Integer.parseInt(column)].setText(value);
					}
				}
			};
			
			//here you get the coordinate of the button location
			final EventHandler<ActionEvent> chooseItem = new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					choosethefield = true;
					Button btn = (Button) event.getSource();
					id = btn.getId();
					row = id.split(",")[0];
					// split the row and column by the ","
					column = id.split(",")[1];
				}
			};

			final EventHandler<ActionEvent> addnumber = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// get the value from the button and and the number to place which is chosen
					if (choosethefield) {
						String value = ((Button) event.getSource()).getText().toString();
						sudoku[Integer.parseInt(row)][Integer.parseInt(column)].setText(value);
					} else {
						return;
					}
				}
			};

			// check the sudoku
			final EventHandler<ActionEvent> checkSudoku = new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					int[][] sudokuCheck = new int[9][9];
					 /* initialize the complete situation, if the situation is changed, then complete
					 becomes false */
					complete = true;
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							if (sudoku[i][j].getText().equals("")) {
								complete = false;
							}
						}
					}
					if (complete) {
						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 9; j++) {
								sudokuCheck[i][j] = Math.abs(Integer.parseInt(sudoku[i][j].getText()));
							}
						}
					}
					/* print out the sudoku status
					if the button has not filled completely then give the retrybox otherwise check the status*/
					if (complete) {
						if (checkSudokuStatus(sudokuCheck)) {
							AlertBox.display("Checking Result", "Your answer is correct. Do you want to try again?");
						} else {
							RetryBox.display("Checking Result",
									"Your answer is incorrect. Would you like to start again?");
						}
					} else {
						RetryBox.display("Checking Result", "Your answer is incorrect. Would you like to start again?");
					}

				}

			};

			// if users click the clear button then initialize the situation
			final EventHandler<ActionEvent> clearnumber = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							//if the number is less then 0 then set the button ""
							if ((storeArray[i][j]) < 0) {
								sudoku[i][j].setText("");
							} else {
								sudoku[i][j].setText(Integer.toString(storeArray[i][j]));
							}
						}
					}
				}
			};

			 /*when you clicked the pause button then you can't click the new game button
			 after go back to the menu*/
			final EventHandler<ActionEvent> pauseTheGame = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					startgame = true;
					pausethegame = true;
					sudokuWindow.close();
					try {
						ma.start(initalWindow);
					} catch (Exception e) {
						e.printStackTrace();
					}
					initalWindow.show();
				}
			};
			//add eventhandle of keyevent so that the users can type in the number from key board
			final EventHandler<KeyEvent> typeTheNum = new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					event.getCode();
					if(event.getCode().isDigitKey() && //exclude 0 number
							event.getCode()!=KeyCode.DIGIT0) {
					if(choosethefield) {
						String str = event.getCode().getName();
						sudoku[Integer.parseInt(row)][Integer.parseInt(column)].setText(str);
					}
				}
				}
			};
			
			//set actions on the 1-9 buttons
			for (int i = 0; i < 9; i++) {
				number[i].setOnAction(addnumber);
			}
			
			//set actions on the 9*9 button at the above
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					sudoku[i][j].setOnAction(chooseItem);
					sudoku[i][j].setOnKeyPressed(typeTheNum);
				}
			}
			//set actions on clear, hint, btncheck, pause
			clear.setOnAction(clearnumber);
			hint.setOnAction(getHint);
			btncheck.setOnAction(checkSudoku);
			pause.setOnAction(pauseTheGame);
			//set application.css
			sudokuScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			s.setScene(sudokuScene);
			s.setTitle("Ass2-sudoku");
			s.show();
			sudokuWindow = s;
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Main main = new Main();
			VBox vb = new VBox(); // vb is the start stage
			HBox hbtext = new HBox();
			Text welcome = new Text("Welcome to the sudoku world!");
			welcome.setFont(new Font(20));
			hbtext.getChildren().add(welcome);
			hbtext.setAlignment(Pos.CENTER); // let the hbtext hbox align in the centre
			HBox.setMargin(welcome, new Insets(40)); // set margin of hbox
			HBox hbbtnew = new HBox();
			HBox hbbtncont = new HBox();
			Button new_game = new Button("NEW GAME");
			Button cont = new Button("CONTINUE");
			new_game.setPrefSize(140, 40); // set size of the button
			hbbtnew.setAlignment(Pos.CENTER);
			HBox.setMargin(new_game, new Insets(40));
			cont.setPrefSize(140, 40); // set size of the button
			hbbtncont.setAlignment(Pos.CENTER);
			HBox.setMargin(cont, new Insets(40));
			hbbtnew.getChildren().add(new_game);
			hbbtncont.getChildren().add(cont);
			vb.getChildren().addAll(hbtext, hbbtnew, hbbtncont);
			orginalScene = new Scene(vb, 400, 400);// the first scene
			primaryStage.setScene(orginalScene);
			primaryStage.setTitle("Ass2-sudoku");
			primaryStage.show();
			initalWindow = primaryStage;
			final EventHandler<ActionEvent> newgame = new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					try {
						main.sudoku(sudokuWindow);
					} catch (Exception e) {
						e.printStackTrace();
					}
					primaryStage.close();
					sudokuWindow.show();
				}
			};

			final EventHandler<ActionEvent> resume = new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					if (startgame) {
						sudokuWindow.show();
					}
					primaryStage.close();
					sudokuWindow.show();
				}
			};
			cont.setDisable(!pausethegame); // disable the button
			cont.setOnAction(resume);
			// set an action so that to open a sudoku stage
			new_game.setOnAction(newgame); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
