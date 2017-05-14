package com.example.liufan.nerverchat;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.liufan.nerverchat.my.Titanic;
import com.example.liufan.nerverchat.my.TitanicTextView;
import com.example.liufan.nerverchat.my.Typefaces;

public class MainActivity extends AppCompatActivity {
    Titanic t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        // start animation
        t= new Titanic();
        t.setAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                MainActivity.this.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        t.start(tv);


    }
}
