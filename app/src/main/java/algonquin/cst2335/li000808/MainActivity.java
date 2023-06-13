package algonquin.cst2335.li000808;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button loginButton;
    EditText emailEditText;

    private static String TAG = "MainActivity";
    private SharedPreferences prefs; // Declare SharedPreferences object
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailEditText);


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //String emailAddress = prefs.getString("LoginName", "");


        loginButton.setOnClickListener( clk-> {
            String emailAddress = emailEditText.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", emailAddress);
            prefs.getFloat("Hi", 0); //the 0 specifies what to return in case the variable "Hi" is not in the file.
            prefs.getInt("Age", 0); //the 0 specifies what to return in case the variable "Age" is not in the file.
            editor.apply();

            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", emailAddress);
            startActivity( nextPage);
        });


        Log.w(TAG, "In onCreate() - Loading Widgets" );
    }


    @Override //now visible on screen
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "In onStart() - The application is now visible on screen" );
    }

    @Override //now responding to input touch
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "In onResume() - The application is now responding to user input");
        SharedPreferences prefs = getSharedPreferences("MyData",Context.MODE_PRIVATE);

        String emailAddress = prefs.getString("EmailAddress","");
        emailEditText.setText(emailAddress);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "In onPause() - The application no longer responds to user input");
        SharedPreferences prefs = getSharedPreferences("MyData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("EmailAddress",emailEditText.getText().toString());
        editor.apply();

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG,"In onStop() - The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG,"In onDestroy() - Any memory used by the application is freed");
    }
}
