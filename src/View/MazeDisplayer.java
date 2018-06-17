package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionColumn;
    private Position goal_pos;
    private Solution solution;
    boolean isGenerated = false;
    boolean wantSolution = false;


    public MazeDisplayer() {
        widthProperty().addListener(e->redraw());
        heightProperty().addListener(e->redraw());
    }

    public void setMaze(Maze maze) {
        this.maze = maze.getM_arr();
        isGenerated = true;
        wantSolution = false;
        solution = null;
        goal_pos = maze.getGoalPosition();
        redraw();
    }

    public int[][] getMaze(){
        return maze;
    }

    public void setSolution (Solution sol) {
        if (maze != null) {
            solution = sol;
            wantSolution = true;
            redraw();
            //wantSolution = false;
        }
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        if (solution != null)
            wantSolution = true;
        redraw();
    }

/*    public void setGoalPosition (int row, int column) {
        goalPositionRow = row;
        goalPositionColumn = column;
        redraw();
    }*/

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze[0].length;
            double cellWidth = canvasWidth / maze.length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileGoal.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                //Draw Maze
                for (int i = 0; i < maze.length; i++) { // i == row - y axis
                    for (int j = 0; j < maze[i].length; j++) { // j == col - x axis
                        if (maze[i][j] == 1) {
                            gc.drawImage(wallImage, j * cellWidth, i* cellHeight, cellWidth, cellHeight);
                        }
                        else {
                            gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                        if (wantSolution && isGenerated) {
                            //wantSolution = false;
                            gc.setFill(Color.RED);
                            List<AState> drawSol = solution.getSolutionPath();
                            for (int k = 0; k < drawSol.size(); k++) {
                                MazeState state =(MazeState) drawSol.get(k);
                                if ((state.getPosition().getColumnIndex() == j && state.getPosition().getRowIndex() == i)) {
                                    gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                                    break;
                                }
                            }
                            gc.setFill(Color.BLACK);
                        }
                        if (j==goal_pos.getColumnIndex() && i==goal_pos.getRowIndex()) {
                            gc.drawImage(goalImage, goal_pos.getColumnIndex() * cellWidth, goal_pos.getRowIndex() * cellHeight, cellWidth, cellHeight);
                        }
                        if (i == characterPositionRow && j == characterPositionColumn) {
                            gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
                //wantSolution = false;
                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                //gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
                //gc.drawImage(goalImage, goalPositionColumn * cellWidth, goalPositionRow * cellHeight, cellWidth, cellHeight);

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileGoal = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileGoal(String imageFileGoal) {
        this.ImageFileGoal.set(imageFileGoal);
    }

    public String getImageFileGoal() {
        return ImageFileGoal.get();
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public void setWantSolution(boolean wantSolution) {
        this.wantSolution = wantSolution;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
}








