package com.example.bobbywang.flashdash;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RecursiveDisplayQuiz extends AppCompatActivity {

    String message;
    String first;
    String second;
    TextView termTextView;
    TextView defTextView;
    boolean order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recursive_display_quiz);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String [] temp = message.split("\n");
        message = "";
        randomize(temp);
        for (int i = 0; i < temp.length; i++) {
            message += temp[i] + '\n';
        }
        termTextView = (TextView) findViewById(R.id.termTextView);
        defTextView = (TextView) findViewById(R.id.defTextView);
        Random rnd = new Random();
        int seq = rnd.nextInt(80) % 2;
        first = first(message);
        second = second(message);
        if (seq == 0) {
            termTextView.setText(first);
            order = true;
        } else {
            order = false;
            termTextView.setText(second);
        }
    }

    static void randomize(String[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void check (View view) {
        if (order == true) {
            defTextView.setText(second);
        } else {
            defTextView.setText(first);
        }
    }

    public void next (View view) {


        while (message.charAt(0) != '\n') {
            message = message.substring(1);
        }
        message = message.substring(1);
        //finish();
        if (message.length() != 0) {

            Random rnd = new Random();
            int seq = rnd.nextInt(80) % 2;
            first = first(message);
            second = second(message);
            if (seq == 0) {
                termTextView.setText(first);
                order = true;
            } else {
                order = false;
                termTextView.setText(second);
            }
            defTextView.setText("");
        } else {
            finish();
        }


    }

    public void save (View view) {

        String current = first + ":" + second + '\n';
        message = message + current;
        next(view);
    }

    public String first (String data) {
        String result = "";
        while (data.charAt(0) != ':') {
            result += data.charAt(0);
            data = data.substring(1);
        }
        return result;
    }

    public String second (String data) {
        String result = "";


        while (data.charAt(0) != ':') {
            data = data.substring(1);
        }

        data = data.substring(1);

        while (data.charAt(0) != '\n') {
            result += data.charAt(0);
            data = data.substring(1);
        }
        return result;
    }
}
