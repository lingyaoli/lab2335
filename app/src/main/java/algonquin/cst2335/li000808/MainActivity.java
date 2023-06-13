package algonquin.cst2335.li000808;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView tv = null;
    EditText et = null;
    Button btn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tv = findViewById(R.id.textView);
         et = findViewById(R.id.editText);
         btn = findViewById(R.id.button);

        btn.setOnClickListener( clk ->{
            String password = et.getText().toString();
            checkPasswordComplexity(password);
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
            Toast.makeText(MainActivity.this, "Your password meets all requirements", Toast.LENGTH_SHORT).show();
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

    /** This function should check is pw using special characters or not
     *
     * @param c  The character of one of the #$%^&*!@?
     * @return return true if c is one of the #$%^&*!@?, otherwise false
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