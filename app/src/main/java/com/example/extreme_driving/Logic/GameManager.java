package com.example.extreme_driving.Logic;

import java.util.Random;

public class GameManager {
    private int numCollisions = 0;
    private int life;
    private int carCol;

    private int[][] board; //empty is 0,stone is 1 ,car is 2
    private int rows;
    private int cols;
    private Random rnd = new Random();

    public GameManager() {
        this(3, 5, 3);
    }

    public GameManager(int life, int rows, int cols) {
        this.life = life;
        this.carCol = 1;
        this.rows = rows;
        this.cols = cols;
        this.board = new int[rows][cols];
        clearBoard();
        setInitialCarPosition();
    }
    public int getNumCollisions() {
        return numCollisions;
    }

    public int getLife() {
        return life;
    }

    public int getCarCol() {
        return carCol;
    }

    public int[][] getBoard() {
        return board;
    }
    public void moveCarLeft() {
        if (carCol > 0) {
            board[rows - 1][carCol] = 0;
            carCol--;
            board[rows - 1][carCol] = 2;
        }
    }

    public void moveCarRight() {
        if (carCol < cols - 1) {
            board[rows - 1][carCol] = 0;
            carCol++;
            board[rows - 1][carCol] = 2;
        }
    }
    public void clearBoard() {
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = 0;
            }
        }
    }
    public void setInitialCarPosition() {
        board[rows - 1][carCol] = 2;
    }
    public void setRandomRock() {
        int colIndex = rnd.nextInt(cols);
        board[0][colIndex] = 1;
    }
    public boolean moveRocksAndCheckCollision() {
        boolean isCollision = false;
        for (int row = rows - 2; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col] == 1) {
                    board[row][col] = 0;
                    if (row == rows - 2 && col == carCol) {
                        numCollisions++;
                        isCollision = true;
                    }
                    board[row + 1][col] = 1;
                }
            }
        }
        return isCollision;
    }
    public void resetGame() {
        numCollisions = 0;
        clearBoard();
        setInitialCarPosition();
    }
    public boolean isGameLost() {
        return life == numCollisions;
    }
}
