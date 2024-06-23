package com.example.extreme_driving;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.extreme_driving.Logic.GameManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final long DELAY = 1000L;
    private AppCompatImageView[] main_IMG_hearts;
    private AppCompatImageView[][] main_board;
    private FloatingActionButton main_BTN_left_arrow;
    private FloatingActionButton main_BTN_right_arrow;
    private GameManager gameManager;
    private Handler handler = new Handler();
    private int iterationCounter = 0;
    private Runnable gameRunnable= new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            if (iterationCounter % 2 == 0) {
                gameManager.setRandomRock();
            }
            boolean isCollision = gameManager.moveRocksAndCheckCollision();
            iterationCounter++;
            refreshUI(isCollision);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length,
                main_board.length,
                main_board[0].length);
        initViews();
        startGame();
    }

    private void initViews() {
        main_board[4][0].setVisibility(View.INVISIBLE);
        main_board[4][1].setVisibility(View.VISIBLE);
        main_board[4][2].setVisibility(View.INVISIBLE);

        for (int i = 0; i < main_board.length - 1; i++) {
            for (int j = 0; j < main_board[0].length; j++) {
                main_board[i][j].setVisibility(View.INVISIBLE);
            }
        }
        main_BTN_left_arrow.setOnClickListener(v -> moveRacingCarLeft());
        main_BTN_right_arrow.setOnClickListener(v -> moveRacingCarRight());
    }


    private void findViews() {
        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        main_board = new AppCompatImageView[5][3];
        main_board[0][0] = findViewById(R.id.stone_0_0);
        main_board[0][1] = findViewById(R.id.stone_0_1);
        main_board[0][2] = findViewById(R.id.stone_0_2);
        main_board[1][0] = findViewById(R.id.stone_1_0);
        main_board[1][1] = findViewById(R.id.stone_1_1);
        main_board[1][2] = findViewById(R.id.stone_1_2);
        main_board[2][0] = findViewById(R.id.stone_2_0);
        main_board[2][1] = findViewById(R.id.stone_2_1);
        main_board[2][2] = findViewById(R.id.stone_2_2);
        main_board[3][0] = findViewById(R.id.stone_3_0);
        main_board[3][1] = findViewById(R.id.stone_3_1);
        main_board[3][2] = findViewById(R.id.stone_3_2);

        main_board[4][0] = findViewById(R.id.racing_car_4_0);
        main_board[4][1] = findViewById(R.id.racing_car_4_1);
        main_board[4][2] = findViewById(R.id.racing_car_4_2);

        main_BTN_left_arrow = findViewById(R.id.main_BTN_left_arrow);
        main_BTN_right_arrow = findViewById(R.id.main_BTN_right_arrow);
    }

    private void moveRacingCarLeft() {
        gameManager.moveCarLeft();
        updateCarPosition();
    }

    private void moveRacingCarRight() {
        gameManager.moveCarRight();
        updateCarPosition();
        //updateBoard();
    }

    private void updateCarPosition() {
        int racingCarCol = gameManager.getCarCol();
        main_board[4][0].setVisibility(racingCarCol == 0 ? View.VISIBLE : View.INVISIBLE);
        main_board[4][1].setVisibility(racingCarCol == 1 ? View.VISIBLE : View.INVISIBLE);
        main_board[4][2].setVisibility(racingCarCol == 2 ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateBoard() {
        int[][] board = gameManager.getBoard();
        for (int i = 0; i < board.length - 1; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1) {
                    main_board[i][j].setVisibility(View.VISIBLE);
                } else if (board[i][j] == 2) {
                    main_board[i][j].setVisibility(View.VISIBLE);
                } else {
                    main_board[i][j].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void refreshUI(boolean isCollision) {
        updateBoard();
        //updateHearts();
        if (isCollision) {
            main_IMG_hearts[gameManager.getNumCollisions() - 1]
                    .setVisibility(View.INVISIBLE);
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            Toast.makeText(this, "Crash occurred!", Toast.LENGTH_SHORT).show();

            if (gameManager.isGameLost()) {
                //Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show();
                gameManager.resetGame();
                updateHearts();
            }
        }
    }

    public void startGame() {
        handler.postDelayed(gameRunnable, 0);
    }


    public void stopGame() {
        handler.removeCallbacks(gameRunnable);
    }

    private void updateHearts() {
        int numCollisions = gameManager.getNumCollisions();
        for (int i = 0; i < main_IMG_hearts.length; i++) {
            main_IMG_hearts[i].setVisibility(i < (main_IMG_hearts.length - numCollisions) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGame();
    }
}



