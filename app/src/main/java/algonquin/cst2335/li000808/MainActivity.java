package algonquin.cst2335.li000808;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //look for sth with the id "theText", return that object
    TextView theText = findViewById(R.id.theText); //same as getElementById in JS

    Button b = findViewById(R.id.theButton);//will search XML for sth with id theButton

    //anonymous class
    b.setOnClickListener(new View.OnClickListener(){
        //Provide the missing function:

        @Override
        public void onClick(View v) {

            String words = theEdit.getText().toString(); //return what's in the EditText
            //change what is in the textView;
            theText.setText(words);
        }
    });

    EditText theEdit = findViewById( androidx.constraintlayout.widget.R.id.bestChoice);
}