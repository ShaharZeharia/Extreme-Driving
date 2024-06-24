package com.example.extreme_driving.Logic;

import java.util.Random;

public class GameManager {
    private int numCollisions = 0;
    private final int life;
    private int carCol;

    private final int[][] board; //empty is 0,stone is 1
    private final int rows;
    private final int cols;
    private final Random rnd = new Random();

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
            carCol--;
        }
    }

    public void moveCarRight() {
        if (carCol < cols - 1) {
            carCol++;
        }
    }

    public void clearBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = 0;
            }
        }
    }

    public void setRandomRock() {
        int colIndex = rnd.nextInt(cols);
        board[0][colIndex] = 1;
    }

    public void moveStones() {
        for (int row = rows - 1; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col] == 1) {
                    board[row][col] = 0;
                    if (row < rows - 1) {
                        board[row + 1][col] = 1;
                    }
                }
            }
        }
    }

    public boolean checkCollision() {
        boolean isCollision = false;
        if (board[rows - 1][carCol] == 1) {
            numCollisions++;
            board[rows - 1][carCol] = 0;
            isCollision = true;
        }
        return isCollision;
    }

    public void resetGame() {
        numCollisions = 0;
        carCol = 1;
        clearBoard();
    }

    public boolean isGameLost() {
        return life == numCollisions;
    }
}


