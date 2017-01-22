package com.example.android.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.checked;
import static android.R.attr.name;
import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity {

    int numberOfAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("numberOfAttempts", numberOfAttempts);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfAttempts = savedInstanceState.getInt("numberOfAttempts");
    }

    public void calculateScore (View view) {
        int score = 0;

        //EditText for Prime Minister Question
        //Correct Answer: Shinzo Abe
        EditText nameField = (EditText) findViewById(R.id.name);
        if(nameField.getText().toString().trim().equals("")){
            Toast.makeText(this, "No name was entered.", Toast.LENGTH_SHORT).show();
            nameField.setError( "Your name is required!" );
            return;
        }
        String name = nameField.getText().toString();

        EditText primeMinisterField = (EditText) findViewById(R.id.question_prime_minister);
        String primeMinister = primeMinisterField.getText().toString();
        if (primeMinister.compareToIgnoreCase("Shinzo Abe") == 0) {
            score++;
        }
        else if (primeMinister.compareToIgnoreCase("Shinzo") == 0 || primeMinister.compareToIgnoreCase("Abe") == 0) {
            primeMinisterField.setError( "You must include both his first and last name." );
        }

        //Checkboxes for Prefectures of Kyushu Question
        //Answer Choices: Kumamoto, Fukuoka, Kagoshima, and Kanagawa
        //Correct Answer: Kumamoto, Fukuoka, and Kagoshima all must be checked
        CheckBox checkboxKumamoto = (CheckBox) findViewById(R.id.checkbox_kumamoto);
        boolean checkedKumamoto = checkboxKumamoto.isChecked();
        CheckBox checkboxFukuoka = (CheckBox) findViewById(R.id.checkbox_fukuoka);
        boolean checkedFukuoka = checkboxFukuoka.isChecked();
        CheckBox checkboxKagoshima = (CheckBox) findViewById(R.id.checkbox_kagoshima);
        boolean checkedKagoshima = checkboxKagoshima.isChecked();
        CheckBox checkboxKanagawa = (CheckBox) findViewById(R.id.checkbox_kanagawa);
        boolean checkedKanagawa = checkboxKanagawa.isChecked();

        if (checkedKumamoto && checkedFukuoka && checkedKagoshima && !checkedKanagawa) {
            score ++;
        }

        //EditText for Number of Prefectures Question
        //Correct Answer: 47
        EditText numberOfPrefecturesField = (EditText) findViewById(R.id.number_of_prefectures);
        int numberOfPrefectures;

        if (numberOfPrefecturesField.getText().toString().trim().equals(""))
        {
            numberOfPrefectures = 0;
        } else {
            numberOfPrefectures = Integer.parseInt(numberOfPrefecturesField.getText().toString());
        }
        if (numberOfPrefectures == 47) {
            score ++;
        }

        //RadioButton for Sakurajima Question
        //Correct Answer: Kagoshima == true
        RadioButton radioButtonKagoshima = (RadioButton) findViewById(R.id.radio_kagoshima);
        boolean radioCheckedKagoshima = radioButtonKagoshima.isChecked();

        if (radioCheckedKagoshima) {
            score ++;
        }

        Toast.makeText(this, "You got " + score + " out of 4 questions correct.", Toast.LENGTH_SHORT).show();
        numberOfAttempts++;

        if (score == 4) {
            String message;
            if (numberOfAttempts == 1) {
                message = getString(R.string.message_one_attempt, name);
            }
            else {
                message = getString(R.string.message_more_attempts, numberOfAttempts, name);
            }
            composeEmail(getString(R.string.subject, name), message);
        }
    }

    public void composeEmail(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
