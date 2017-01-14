package sikder.nehan.memorygame;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    private static final String deckKey = "deck";
    private static final String displayedKey = "displayed";
    private static final String inputKey = "input";
    private static final String checkKey = "check";
    private ArrayList<String> cards = new ArrayList<String>();
    final private ArrayList<EditText> answerF = new ArrayList<EditText>();
    private int cardsDisplayed;
    private Button reset;
    private Button check;
    private LinearLayout main;
    private String [] inputs = new String[52];
    private static final String TAG = "test";
    private boolean checkPressed = false;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    public static Intent newIntent(Context packageContext, ArrayList<String> deck, int numberOfCardsDisplayed) {
        Intent i = new Intent(packageContext, Result.class);
        i.putExtra(deckKey, deck);
        i.putExtra(displayedKey, numberOfCardsDisplayed);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));
        main = (LinearLayout) findViewById(R.id.main);


        if (savedInstanceState != null) {
            cards = savedInstanceState.getStringArrayList(deckKey);
            cardsDisplayed = savedInstanceState.getInt(displayedKey);
            inputs = savedInstanceState.getStringArray(inputKey);
            checkPressed =  savedInstanceState.getBoolean(checkKey);
            createInputFields();
            updateInputFields();
        } else {
            cards = getIntent().getStringArrayListExtra(deckKey);
            cardsDisplayed = getIntent().getIntExtra(displayedKey, 0);
            createInputFields();
        }
        if(checkPressed){
            check();
        }

        reset = (Button) findViewById(R.id.restart);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Result.this, Manual.class);
                startActivity(i);
            }
        });

        check = (Button) findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPressed = true;
                check();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void updateInputFields() {
        for (int i = 0; i < cardsDisplayed; i++) {
            EditText answerField = (EditText) findViewById(i);
            Log.d(TAG, "updateInputFields: " +inputs[i]);
            answerField.setText(inputs[i]);
        }
    }

    private void createInputFields(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < cardsDisplayed; i++) {
            final int index = i;
            answerF.add(new EditText(this));
            answerF.get(i).setId(i);
            answerF.get(i).setLayoutParams(params);
            answerF.get(i).setGravity(Gravity.CENTER_VERTICAL);
            answerF.get(i).getBackground().setColorFilter(getResources().getColor(R.color.orange),  PorterDuff.Mode.SRC_IN);
            if(i == 0){
                answerF.get(i).setHint("Enter answer: (Example: 9 spades)");
            }
            answerF.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String value = "";
                    for (int i = 0; i < s.length(); i++){
                        value+=s.charAt(i);
                    }
                    inputs[index] = value;
                }
            });
            main.addView(answerF.get(i));
        }
    }

    private void check(){
        for (int i = 0; i < cardsDisplayed; i++){
            EditText answerField = (EditText) findViewById(i);
            String answer = answerField.getText().toString();
//            Log.d(TAG, "check: " + answer);
            if(answer.length() > 0){
                if(answerCorrect(answer, i)){
//                    answerField.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
                    answerField.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_correct));
                }else{
//                    answerField.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
                    answerField.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_incorrect));
                };
            }
        }
    }

    private boolean answerCorrect(String answer, int cardNumber){
        String correct = (cards.get(cardNumber)).toLowerCase();
        String userAnswer =  answer.toLowerCase();
        if (userAnswer.contains(getRank(correct)) && userAnswer.contains(getSuit(correct))){
            return true;
        } else {
            return  false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(displayedKey, cardsDisplayed);
        savedInstanceState.putStringArrayList(deckKey, cards);
        savedInstanceState.putStringArray(inputKey, inputs);
        savedInstanceState.putBoolean(checkKey, checkPressed);

    }

//    public void initializeInputs(){
//        for(int i = 0; i < inputs.size(); i++){
//            inputs.add("hello");
//            Log.d(TAG, "initializeInputs: " + inputs.get(i));
//        }
//    }

    public String format(String card) {
        String formattedName = "";
        int cardLength = card.length() - 1;
        if (card.charAt(cardLength) == 'g') {
            formattedName = card.substring(cardLength - 3) + " of " + card.substring(0, cardLength - 3);
        } else if (card.charAt(cardLength) == 'n') {
            formattedName = card.substring(cardLength - 4) + " of " + card.substring(0, cardLength - 4);
        } else if (card.charAt(cardLength) == 'k') {
            formattedName = card.substring(cardLength - 3) + " of " + card.substring(0, cardLength - 3);
        } else if (card.charAt(cardLength) == 'e') {
            formattedName = card.substring(cardLength - 2) + " of " + card.substring(0, cardLength - 2);
        } else if (card.charAt(cardLength) == '0') {
            formattedName = card.substring(cardLength - 2) + " of " + card.substring(0, cardLength - 2);
        } else {
            formattedName = card.substring(cardLength) + " of " + card.substring(0, cardLength);
        }
        return formattedName;
    }

    public String getRank(String card) {
        String rank = "";
        int cardLength = card.length() - 1;
        if (card.charAt(cardLength) == 'g') {
            rank = card.substring(cardLength - 3);
        } else if (card.charAt(cardLength) == 'n') {
            rank = card.substring(cardLength - 4) ;
        } else if (card.charAt(cardLength) == 'k') {
            rank = card.substring(cardLength - 3);
        } else if (card.charAt(cardLength) == 'e') {
            rank = card.substring(cardLength - 2);
        } else if (card.charAt(cardLength) == '0') {
            rank = card.substring(cardLength - 2);
        } else {
            rank = card.substring(cardLength);
        }
        return rank.toLowerCase();
    }

    public String getSuit(String card){
        String suit = "";
        int cardLength = card.length() - 1;
        if (card.charAt(cardLength) == 'g') {
            suit = card.substring(0, cardLength - 3);
        } else if (card.charAt(cardLength) == 'n') {
            suit = card.substring(0, cardLength - 4);
        } else if (card.charAt(cardLength) == 'k') {
            suit = card.substring(0, cardLength - 3);
        } else if (card.charAt(cardLength) == 'e') {
            suit = card.substring(0, cardLength - 2);
        } else if (card.charAt(cardLength) == '0') {
            suit = card.substring(0, cardLength - 2);
        } else {
            suit = card.substring(0, cardLength);
        }
        return suit.toLowerCase();
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Result Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }
}
