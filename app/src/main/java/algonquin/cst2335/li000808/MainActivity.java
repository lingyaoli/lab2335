package algonquin.cst2335.li000808;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * @author Lingyao LI
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**This holds the text at the centre of the screen*/
    TextView tv = null;
    /**This holds the password user need to typed in*/
    EditText et = null;
    /**This hold the Login button*/
    Button btn = null;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tv = findViewById(R.id.textView);
         et = findViewById(R.id.editText);
         btn = findViewById(R.id.button);

        btn.setOnClickListener( clk ->{
            String password = et.getText().toString();
            boolean isComplex = checkPasswordComplexity(password);

            if (isComplex) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
        });
    }

    /** This function should check if this password complexity
     *
     * @param pw The String object that we are checking
     * @return Return true if the password is complex enough, and false if it is not complex enough.
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            Toast.makeText(MainActivity.this, "Your password does not have an uppercase letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(MainActivity.this, "Your password does not have a lowercase letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(MainActivity.this, "Your password does not have a number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(MainActivity.this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            //Toast.makeText(MainActivity.this, "Your password meets all requirements", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
//        if (!Pattern.compile("[A-Z]").matcher(pw).find()) {
//            Toast.makeText(MainActivity.this, "Your password does not have an uppercase letter", Toast.LENGTH_SHORT).show();
//        } else if (!Pattern.compile("[a-z]").matcher(pw).find()) {
//            Toast.makeText(MainActivity.this, "Your password does not have a lowercase letter", Toast.LENGTH_SHORT).show();
//        } else if (!Pattern.compile("[0-9]").matcher(pw).find()) {
//            Toast.makeText(MainActivity.this, "Your password does not have a number", Toast.LENGTH_SHORT).show();
//        } else if (!Pattern.compile("[#$%^&*!@?]").matcher(pw).find()) {
//            Toast.makeText(MainActivity.this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, "Your password meets all requirements", Toast.LENGTH_SHORT).show();
//        }

    /** This function checks if a character is a special character.
     *
     * @param c  The character to be checked
     * @return Returns true if the character is one of the special characters, otherwise false.
     */
    boolean isSpecialCharacter(char c)
    {
        switch (c)
        {
            case'#':
            case'$':
            case'%':
            case'^':
            case'&':
            case'*':
            case'!':
            case'@':
            case'?':
                return true;
            default:
                return false;
        }
    }
}