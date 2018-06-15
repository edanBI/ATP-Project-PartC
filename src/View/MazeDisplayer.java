package View;

import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
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
import java.util.ResourceBundle;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionColumn;
    private Solution solution;
    boolean isGenerated = false;
    boolean wantSolution = false;


    public MazeDisplayer() {
        widthProperty().addListener(e->redraw());
        heightProperty().addListener(e->redraw());
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
        redraw();
    }

    public int[][] getMaze(){
        return maze;
    }

    public void setSolution (Solution sol) {
        solution = sol;
        wantSolution = true;
        redraw();
        wantSolution = false;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getHeight();
    }

    @Override
    public double prefHeight(double width) {
        return getWidth();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void setGoalPosition (int row, int column) {
        goalPositionRow = row;
        goalPositionColumn = column;
        redraw();
    }

    private void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileGoal.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            //gc.fillRect(j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                        }
                    }
                }

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                gc.drawImage(goalImage, goalPositionColumn * cellHeight, goalPositionRow * cellWidth, cellHeight, cellWidth);
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
    //endregion

}

