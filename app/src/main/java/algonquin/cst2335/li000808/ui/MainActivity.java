package algonquin.cst2335.li000808.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import algonquin.cst2335.li000808.R;
import algonquin.cst2335.li000808.data.MainViewModel;
//import?

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //calls parent onCreate()

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.myEditText.setText(model.editString);
        variableBinding.myButton.setOnClickListener(click ->
        {
            model.editString.observe(owner: this, s -> {
            variableBinding.textview.setText("Your edit text has: " +s);
        });
            //onCheckedChanged()
//            myCheckbox.setOnCheckedChangeListener((a,b) -> {
//                //b is the new value on/off
//                theText.setText("The checkbox is on?" +b);
//            });
//            mySwitch.setOnCheckedChangeListener((a,b) -> {
//                //when only 1 line of code between {}
//                theText.setText("The switch is on?" +b);
//            });
//
//            model.isSelected.observe( owner: this, selected -> {
//            variableBinding.checkBox.setChecked(selected);
//            variableBinding.radioButton.setChecked(selected);
//            variableBinding.switch1.setchecked(selected);
});}}


//        variableBinding.theButton.setOnClickListener(new View.OnClickListener(){

//        //loads an XML file on the page
//        setContentView(R.layout.activity_main);
    //look for sth with the id "theText", return that object
//    TextView theText = findViewById(R.id.theText); //same as getElementById in JS
//
//    Button b = findViewById(R.id.theButton);//will search XML for sth with id theButton
//
//    //anonymous class
//    b.setOnClickListener(new View.OnClickListener(){
        //Provide the missing function:
//
//        @Override
//        public void onClick(View v) {
//
//            String words = variableBinding.theEditText.getText().toString();
//
//            //change what is in the textView;
//            variableBinding.theText.setText(words);
//        }
//    });
//
//    EditText theEdit = findViewById( androidx.constraintlayout.widget.R.id.bestChoice);
//}