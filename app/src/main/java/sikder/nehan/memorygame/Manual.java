package sikder.nehan.memorygame;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Manual extends AppCompatActivity {

    private static final String TAG = "test";
    private ImageView window;
    private ArrayList<String> cards  = new ArrayList<>();
    private int index = 0;
    private Resources res;
    private int cardDisplayed  = 0;
    private Button next;
    private Button stop;
    private String cardKey = "cards";
    private String indexKey = "index";
    private String displaydKey = "shown";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create");

        setContentView(R.layout.activity_manual);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));

        loadCards();
//        Log.d(TAG, "Array size "+cards.size());
        window = (ImageView) findViewById(R.id.view);

        if(savedInstanceState != null){
            cards = savedInstanceState.getStringArrayList(cardKey);
            index = savedInstanceState.getInt(indexKey);
            cardDisplayed = savedInstanceState.getInt(displaydKey);
            Log.d(TAG, "next index" + index);
            window.setImageResource(getResources().getIdentifier(cards.get(index), "drawable", getPackageName()));
        } else {
            Collections.shuffle(cards);
            cardDisplayed++;
        }

        window.setImageResource(getResources().getIdentifier(cards.get(index), "drawable", getPackageName()));
        window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCards();
            }
        });

        next = (Button) findViewById(R.id.Next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCards();
            }
        });

        stop = (Button) findViewById(R.id.Stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startResultScreen();
            }
        });

    }

    public void startResultScreen(){
        Intent result = Result.newIntent(Manual.this, cards, cardDisplayed);
        startActivity(result);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "OnSaveInstanceState");
        savedInstanceState.putStringArrayList(cardKey,cards);
        savedInstanceState.putInt(indexKey,index);
        savedInstanceState.putInt(displaydKey, cardDisplayed);

    }

    private void updateCards(){
        index = (index+1)%cards.size();
        res = getResources();
        String mDrawableName = cards.get(index);
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        window.setImageResource(resID);
        cardDisplayed++;
        if(cardDisplayed == cards.size()){
//          display end messge and display the output list of cards
            startResultScreen();
        }
    }

    private void loadCards(){
        String[] types = {"clubs","diamonds","hearts","spades"};
        for (int j = 0; j < types.length;j++){
            for (int i =2; i < 11; i++){
                cards.add(types[j]+i);
            }
            cards.add(types[j]+"ace");
            cards.add(types[j]+"king");
            cards.add(types[j]+"queen");
            cards.add(types[j]+"jack");
        }
    }
}
