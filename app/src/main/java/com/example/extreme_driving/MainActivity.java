package com.example.extreme_driving;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.extreme_driving.Logic.GameManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private static final long DELAY = 1000L;
    private AppCompatImageView[] main_IMG_hearts;
    private AppCompatImageView[][] main_IMG_stone;
    private AppCompatImageView[] main_IMG_racing_car;
    private FloatingActionButton main_BTN_left_arrow;
    private FloatingActionButton main_BTN_right_arrow;
    private GameManager gameManager;
    private final Handler handler = new Handler();
    private int iterationCounter = 0;
    private final Runnable gameRunnable= new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            gameManager.moveStones();
            if (iterationCounter % 2 == 0) {
                gameManager.setRandomRock();
            }
            boolean isCollision = gameManager.checkCollision();
            iterationCounter++;
            updateGameUI(isCollision);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length,
                main_IMG_stone.length,
                main_IMG_stone[0].length);
        initViews();
    }

private void initViews() {
    for (int i = 0; i < main_IMG_stone.length; i++) {
        for (int j = 0; j < main_IMG_stone[0].length; j++) {
            main_IMG_stone[i][j].setVisibility(View.INVISIBLE);
        }
    }
    updateCarPosition();
    main_BTN_left_arrow.setOnClickListener(v -> moveRacingCarLeft());
    main_BTN_right_arrow.setOnClickListener(v -> moveRacingCarRight());
}

    private void findViews() {
        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        main_IMG_racing_car = new AppCompatImageView[]{
                findViewById(R.id.racing_car_4_0),
                findViewById(R.id.racing_car_4_1),
                findViewById(R.id.racing_car_4_2)
        };

        main_IMG_stone = new AppCompatImageView[5][3];
        main_IMG_stone[0][0] = findViewById(R.id.stone_0_0);
        main_IMG_stone[0][1] = findViewById(R.id.stone_0_1);
        main_IMG_stone[0][2] = findViewById(R.id.stone_0_2);
        main_IMG_stone[1][0] = findViewById(R.id.stone_1_0);
        main_IMG_stone[1][1] = findViewById(R.id.stone_1_1);
        main_IMG_stone[1][2] = findViewById(R.id.stone_1_2);
        main_IMG_stone[2][0] = findViewById(R.id.stone_2_0);
        main_IMG_stone[2][1] = findViewById(R.id.stone_2_1);
        main_IMG_stone[2][2] = findViewById(R.id.stone_2_2);
        main_IMG_stone[3][0] = findViewById(R.id.stone_3_0);
        main_IMG_stone[3][1] = findViewById(R.id.stone_3_1);
        main_IMG_stone[3][2] = findViewById(R.id.stone_3_2);
        main_IMG_stone[4][0] = findViewById(R.id.stone_4_0);
        main_IMG_stone[4][1] = findViewById(R.id.stone_4_1);
        main_IMG_stone[4][2] = findViewById(R.id.stone_4_2);

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
    }

    private void updateCarPosition() {
        int carCol = gameManager.getCarCol();
        boolean isCollision = gameManager.checkCollision();
        if (isCollision){
            main_IMG_stone[main_IMG_stone.length - 1][carCol].setVisibility(View.INVISIBLE);
            handleCollisionUI();
        }
        for (int i = 0; i < main_IMG_racing_car.length; i++) {
            main_IMG_racing_car[i].setVisibility(i == carCol ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void updateBoard() {
        int[][] board = gameManager.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1) {
                    main_IMG_stone[i][j].setVisibility(View.VISIBLE);
                }  else {
                    main_IMG_stone[i][j].setVisibility(View.INVISIBLE);
                }
            }
        }
        updateCarPosition();
    }


    public void updateGameUI(boolean isCollision) {
        updateBoard();
        if (isCollision) {
            handleCollisionUI();
        }
    }

    public void handleCollisionUI() {
        main_IMG_hearts[gameManager.getNumCollisions() - 1]
                .setVisibility(View.INVISIBLE);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        Toast.makeText(this, "Crash occurred!", Toast.LENGTH_SHORT).show();

        if (gameManager.isGameLost()) {
            gameManager.resetGame();
            updateHearts();
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
    protected void onPause() {
        super.onPause();
        stopGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGame();
    }
}



