package com.example.extreme_driving.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {
    // Constants representing values in the board
    private static final int EMPTY_CELL = 0;
    private static final int STONE_VALUE = 1;
    private static final int COIN_VALUE = 2;

    private int numCollisions = 0;
    private int score = 0;
    private final int life;
    private int carCol;

    private final int[][] board; //empty is 0,stone is 1, coin is 2
    private final int rows;
    private final int cols;
    private final Random rnd = new Random();

    public GameManager() {
        this(3, 5, 3);
    }

    public GameManager(int life, int rows, int cols) {
        this.life = life;
        this.carCol = 2;
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

    public static int getEmptyCell() {
        return EMPTY_CELL;
    }

    public static int getStoneValue() {
        return STONE_VALUE;
    }

    public static int getCoinValue() {
        return COIN_VALUE;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    public void setRandomRock() {
        int colIndex = getRandomAvailableColumn();
        if (colIndex != -1) {
            board[0][colIndex] = STONE_VALUE;
        }
    }

    public void setRandomCoin() {
        int colIndex = getRandomAvailableColumn();
        if (colIndex != -1) {
            board[0][colIndex] = COIN_VALUE;
        }
    }

    private int getRandomAvailableColumn() {
        List<Integer> availableColumns = new ArrayList<>();
        for (int col = 0; col < cols; col++) {
            if (board[0][col] == EMPTY_CELL) {
                availableColumns.add(col);
            }
        }
        if (availableColumns.isEmpty()) {
            return -1;
        }
        return availableColumns.get(rnd.nextInt(availableColumns.size()));
    }


    public void moveStonesAndCoins() {
        int type;
        for (int row = rows - 1; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col] != EMPTY_CELL) {
                    type = board[row][col];
                    board[row][col] = EMPTY_CELL;
                    if (row < rows - 1) {
                        board[row + 1][col] = (type == STONE_VALUE) ? STONE_VALUE : COIN_VALUE;
                    }
                }
            }
        }
    }

    public boolean checkCollisionWithStone() {
        boolean isCollision = false;
        if (board[rows - 1][carCol] == STONE_VALUE) {
            numCollisions++;
            board[rows - 1][carCol] = EMPTY_CELL;
            isCollision = true;
        }
        return isCollision;
    }

    public boolean checkCollisionWithCoin() {
        boolean isCollision = false;
        if (board[rows - 1][carCol] == COIN_VALUE) {
            board[rows - 1][carCol] = EMPTY_CELL;
            isCollision = true;
        }
        return isCollision;
    }

    public boolean isGameLost() {
        return life == numCollisions;
    }

}


