package sikder.nehan.memorygame;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    private Button manual;
//    private Button timed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));

        manual = (Button) findViewById(R.id.manual);
//        timed = (Button) findViewById(R.id.timed);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Manual.class);
                startActivity(i);
            }
        });

//        timed.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent i = new Intent(Menu.this, Timed.class);
//                startActivity(i);
//            }
//        });
    }
}
