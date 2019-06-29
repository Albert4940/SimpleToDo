package com.albert.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.albert.simpletodo.MainActivity.ITEM_POSITION;
import static com.albert.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // track Edit Text
    EditText etItemText;

    //m position of the item in list
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        // Resolve edit text from layout
        etItemText = (EditText)findViewById(R.id.editItemText);

        //set edit text value form intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        //update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION,0);

        //Update the title bar of the activity
        getSupportActionBar().setTitle("Edit Item");
    }

    //handler for save button
        public void onSaveItem(View v){
        //prepare new intent for result
            Intent i = new Intent();
            //Pass update item text as extra
            i.putExtra(ITEM_TEXT,etItemText.getText().toString());
            //pas origin position as extra
            i.putExtra(ITEM_POSITION,position);
            //set the intent as the result of the activity
            setResult(RESULT_OK,i);
            //close the activity and redirect to main
            finish();

        }
}
