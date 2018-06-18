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
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionColumn;
    private Position goal_pos;
    private Solution solution;
    private boolean isGenerated = false;
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
        }
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        if (solution != null)
            wantSolution = true;
        redraw();
    }

    public void redraw() {
        if (maze != null) {
            /*double cellHeight = getWidth() / maze[0].length;
            double cellWidth = getHeight() / maze.length;*/
            double cellWidth = getWidth() / maze[0].length;
            double cellHeight = getHeight() / maze.length;
            /*cellHeight-=32;
            cellWidth-=178;*/
            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileGoal.get()));
                Image mazeImage = new Image(new FileInputStream(ImageFileMaze.get()));
                Image ballImage = new Image(new FileInputStream(ImageFileBall.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.drawImage(mazeImage, 0, 0, getWidth(), getHeight());
                //Draw Maze
                //gc.setFill(Color.TRANSPARENT);

                for (int i = 0; i < maze.length; i++) { // i == row - y axis
                    for (int j = 0; j < maze[i].length; j++) { // j == col - x axis
                        if (maze[i][j] == 1) {
                            gc.drawImage(wallImage, j*cellWidth, i* cellHeight, cellWidth, cellHeight);
                        }
                        if (wantSolution && isGenerated) {
                            List<AState> drawSol = solution.getSolutionPath();
                            for (int k = 0; k < drawSol.size(); k++) {
                                MazeState state =(MazeState) drawSol.get(k);
                                if ((state.getPosition().getColumnIndex() == j && state.getPosition().getRowIndex() == i)) {
                                    gc.drawImage(ballImage,  j*cellWidth, i* cellHeight, cellWidth, cellHeight);
                                    break;
                                }
                            }
                            gc.setFill(Color.BLACK);
                        }
                        if (j==goal_pos.getColumnIndex() && i==goal_pos.getRowIndex()) {
                            gc.drawImage(goalImage, j*cellWidth, i* cellHeight, cellWidth, cellHeight);
                        }
                        if (i == characterPositionRow && j == characterPositionColumn) {
                            gc.drawImage(characterImage, j*cellWidth, i* cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
            } catch (FileNotFoundException e) { e.printStackTrace(); }
        }
    }

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileGoal = new SimpleStringProperty();
    private StringProperty ImageFileMaze = new SimpleStringProperty();
    private StringProperty ImageFileBall = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public String getImageFileBall() {
        return ImageFileBall.get();
    }
    public String getImageFileMaze() {
        return ImageFileMaze.get();
    }
    public String getImageFileGoal() {
        return ImageFileGoal.get();
    }
    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }
    public void setImageFileBall(String imageFileBall) {
        this.ImageFileBall.set(imageFileBall);
    }
    public void setImageFileMaze(String imageFileMaze) {
        this.ImageFileMaze.set(imageFileMaze);
    }
    public void setImageFileGoal(String imageFileGoal) {
        this.ImageFileGoal.set(imageFileGoal);
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








