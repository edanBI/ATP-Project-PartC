package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IView {
    void displayMaze(Maze maze);
    void setViewModel(MyViewModel my_viewModel);
}
