package com.example.extreme_driving.Activity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import android.annotation.SuppressLint;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.extreme_driving.Interfaces.MoveCallback;
import com.example.extreme_driving.Logic.GameManager;
import com.example.extreme_driving.Models.Player;
import com.example.extreme_driving.Models.PlayerList;
import com.example.extreme_driving.R;
import com.example.extreme_driving.Utilities.MoveDetector;
import com.example.extreme_driving.Utilities.SoundPlayer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;


public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private double lat;
    private double lon;

    public static final String KEY_GAME_MODE = "KEY_GAME_MODE";
    public static final String KEY_SPEED = "KEY_SPEED";
    private String gameMode;
    private String speedMode;
    private SoundPlayer soundPlayer;
    private MoveDetector moveDetector;
    private long delay;
    private MaterialTextView main_LBL_score;
    private AppCompatImageView[] main_IMG_hearts;
    private AppCompatImageView[][] main_IMG_stone;
    private AppCompatImageView[][] main_IMG_coin;
    private AppCompatImageView[] main_IMG_racing_car;
    private FloatingActionButton main_BTN_left_arrow;
    private FloatingActionButton main_BTN_right_arrow;
    private GameManager gameManager;
    private final Handler handler = new Handler();
    private int iterationCounter = 0;
    private final Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, delay);
            gameManager.moveStonesAndCoins();
            updateGameElements();
            iterationCounter++;
            updateGameUI();
            updateScore();
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initViews() {
        for (int i = 0; i < main_IMG_stone.length; i++) {
            for (int j = 0; j < main_IMG_stone[0].length; j++) {
                main_IMG_stone[i][j].setVisibility(View.INVISIBLE);
                main_IMG_coin[i][j].setVisibility(View.INVISIBLE);
            }
        }
        updateCarPosition();
        main_BTN_left_arrow.setOnClickListener(v -> moveRacingCarLeft());
        main_BTN_right_arrow.setOnClickListener(v -> moveRacingCarRight());

        Intent previousIntent = getIntent();
        gameMode = previousIntent.getStringExtra(KEY_GAME_MODE);
        speedMode = previousIntent.getStringExtra(KEY_SPEED);
        setGameSpeed();
        setGameMode();
    }

    private void initMoveDetector() {
        moveDetector = new MoveDetector(this,
                new MoveCallback() {
                    @Override
                    public void onMoveLeft() {
                        moveRacingCarLeft();
                    }

                    @Override
                    public void onMoveRight() {
                        moveRacingCarRight();
                    }

                    @Override
                    public void onMoveForward() {
                        speedMode = "fast";
                        setGameSpeed();
                    }

                    @Override
                    public void onMoveBackward() {
                        speedMode = "slow";
                        setGameSpeed();
                    }
                }
        );
    }

    private void findViews() {
        main_LBL_score = findViewById(R.id.main_LBL_score);

        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        main_IMG_racing_car = new AppCompatImageView[]{
                findViewById(R.id.racing_car_4_0),
                findViewById(R.id.racing_car_4_1),
                findViewById(R.id.racing_car_4_2),
                findViewById(R.id.racing_car_4_3),
                findViewById(R.id.racing_car_4_4)

        };

        findGameBoard();
        main_BTN_left_arrow = findViewById(R.id.main_BTN_left_arrow);
        main_BTN_right_arrow = findViewById(R.id.main_BTN_right_arrow);
    }

    private void findGameBoard() {
        main_IMG_stone = new AppCompatImageView[5][5];
        main_IMG_coin = new AppCompatImageView[5][5];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                String stoneId = "stone_" + i + "_" + j;
                String coinId = "coin_" + i + "_" + j;

                main_IMG_stone[i][j] = findViewById(getResourceId(stoneId, getPackageName()));
                main_IMG_coin[i][j] = findViewById(getResourceId(coinId, getPackageName()));
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private int getResourceId(String name, String packageName) {
        return getResources().getIdentifier(name, "id", packageName);
    }

    private void setGameMode() {
        if ("sensor".equals(gameMode)) {
            initMoveDetector();
            main_BTN_left_arrow.setVisibility(View.GONE);
            main_BTN_right_arrow.setVisibility(View.GONE);
            moveDetector.start();
        } else {// Default mode buttons
            main_BTN_left_arrow.setVisibility(View.VISIBLE);
            main_BTN_right_arrow.setVisibility(View.VISIBLE);
            if (moveDetector != null) {
                moveDetector.stop();
            }
        }
    }

    private void setGameSpeed() {
        if ("fast".equals(speedMode)) {
            delay = 500L;
        } else {
            delay = 1000L; // Default to slow mode
        }
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
        boolean isCollisionWithStone = gameManager.checkCollisionWithStone();
        boolean isCollisionWithCoin = gameManager.checkCollisionWithCoin();

        if (isCollisionWithStone) {
            main_IMG_stone[main_IMG_stone.length - 1][carCol].setVisibility(View.INVISIBLE);
            handleCollisionWithStoneUI();
        }
        if (isCollisionWithCoin) {
            main_IMG_coin[main_IMG_coin.length - 1][carCol].setVisibility(View.INVISIBLE);
            handleCollisionWithCoinUI();
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
                    main_IMG_coin[i][j].setVisibility(View.INVISIBLE);
                } else if (board[i][j] == 2) {
                    main_IMG_coin[i][j].setVisibility(View.VISIBLE);
                    main_IMG_stone[i][j].setVisibility(View.INVISIBLE);
                } else {
                    main_IMG_stone[i][j].setVisibility(View.INVISIBLE);
                    main_IMG_coin[i][j].setVisibility(View.INVISIBLE);
                }
            }
        }
        updateCarPosition();
    }

    public void updateGameUI() {
        boolean isCollisionWithStone = gameManager.checkCollisionWithStone();
        boolean isCollisionWithCoin = gameManager.checkCollisionWithStone();

        updateBoard();
        if (isCollisionWithStone) {
            handleCollisionWithStoneUI();
        }

        if (isCollisionWithCoin) {
            handleCollisionWithCoinUI();
        }
    }

    public void handleCollisionWithCoinUI() {
        gameManager.setScore(gameManager.getScore() + 10);
        updateScore();
        soundPlayer = new SoundPlayer(this);
        soundPlayer.playSound(R.raw.coin_pickup);
    }

    public void handleCollisionWithStoneUI() {
        main_IMG_hearts[gameManager.getNumCollisions() - 1]
                .setVisibility(View.INVISIBLE);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(500, 30));
        Toast.makeText(this, "Crash occurred!", Toast.LENGTH_SHORT).show();
        soundPlayer = new SoundPlayer(this);
        soundPlayer.playSound(R.raw.car_accident_with_squeal_and_crash);

        if (gameManager.isGameLost()) {
            gameLost();
        }
    }

    private void gameLost() {
        int score = gameManager.getScore();
        PlayerList playerList = PlayerList.getInstance();
        if (playerList.getTopPlayers().size() < PlayerList.getNUM_OF_TOP_PLAYERS() || score > playerList.getTopPlayers().get(9).getScore()) {
            requestLocationPermissionAndProceed(score);
        } else {
            navigateToHighscores();
        }
    }

    private void requestLocationPermissionAndProceed(int score) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        } else {
                            lat = 0.0;
                            lon = 0.0;
                        }
                        addPlayerScoreAndSave(score);
                        navigateToHighscores();
                    });
        }
    }


    private void addPlayerScoreAndSave(int score) {
        Player newPlayer = new Player().setScore(score).setLat(lat).setLng(lon);
        PlayerList playerList = PlayerList.getInstance();
        playerList.addPlayer(newPlayer);
    }

    private void navigateToHighscores() {
        Intent intent = new Intent(this, HighscoresActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationPermissionAndProceed(gameManager.getScore());
            } else {
                navigateToHighscores();
            }
        }
    }

    private void updateGameElements() {
        if (iterationCounter % 2 == 0) {
            gameManager.setRandomRock();
        }
        if (iterationCounter % 4 == 0) {
            gameManager.setRandomRock();
        }
        if (iterationCounter % 5 == 0 && iterationCounter > 0) {
            gameManager.setRandomCoin();
        }
        gameManager.setScore(gameManager.getScore() + 1);
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

    public void updateScore() {
        main_LBL_score.setText(String.valueOf(gameManager.getScore()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGame();
        if ("sensor".equals(gameMode) && moveDetector != null) {
            moveDetector.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGame();
        soundPlayer.stopSound();
        if (moveDetector != null) {
            moveDetector.stop();
        }

    }
}



