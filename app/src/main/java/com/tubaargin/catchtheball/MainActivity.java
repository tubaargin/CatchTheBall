package com.tubaargin.catchtheball;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tubaargin.catchtheball.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    int score;
    Handler handler;
    Runnable runnable;
    SharedPreferences sharedPreferences;
    ImageView[] imageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        imageArray = new ImageView[]{binding.imageView1, binding.imageView2, binding.imageView3, binding.imageView4,
                binding.imageView5, binding.imageView6, binding.imageView7, binding.imageView8, binding.imageView9,
                binding.imageView10, binding.imageView11, binding.imageView12, binding.imageView13,
                binding.imageView14, binding.imageView15, binding.imageView16,
                binding.imageView17, binding.imageView18, binding.imageView19, binding.imageView20};
        hideImages();
        score = 0;
        sharedPreferences = this.getSharedPreferences("com.tubaargin.catchtheball", Context.MODE_PRIVATE);


        int scored = sharedPreferences.getInt("Score", 0);
        if (scored == 0) {
            binding.scoreText.setText("Score: "+0);
        } else {
            binding.scoreText.setText("Score: " + scored);
        }
        new CountDownTimer(20000, 1000) {

            @Override
            public void onTick(long l) {
                binding.timeText.setText("Time:" + l / 1000);
            }

            @Override
            public void onFinish() {
                binding.timeText.setText("Time off");
                handler.removeCallbacks(runnable);
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(getString(R.string.restart));
                alert.setMessage(getString(R.string.sure_restart_game));
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //restart
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //stop

                        Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_LONG).show();
                    }
                });
                alert.show();
            }
        }.start();
    }

    public void increaseScore(View view) {
        score++;
        binding.scoreText.setText("Score:" + score);
        sharedPreferences.edit().putInt("Score", score).apply();

    }

    public void hideImages() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }
                Random random = new Random();
                int i = random.nextInt(20);
                imageArray[i].setVisibility(View.VISIBLE);
                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }
}