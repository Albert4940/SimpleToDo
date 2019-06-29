package com.albert.simpletodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //a numeric code to indentify the edit activity
    public final static Integer EDIT_REQUEST_CODE = 20;

    // keys used for passing data between activies
    public final static String ITEM_TEXT = "itemText";

    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    Button btnAdd;
    EditText etNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*items.add("First item");
        items.add("Second item");*/
        initialize();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItem();
            }
        });
        setupListViewListener();

    }

    public void initialize()
    {
        etNewItem = (EditText) findViewById(R.id.etnewItem);
        btnAdd = ((Button)findViewById(R.id.btnAddItem));
        lvItems = (ListView)findViewById(R.id.lvItems);

        readeItems();
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);

        checkEditText();
    }

    /**
     *
     */
    public void checkEditText()
    {
        btnAdd.setEnabled(false);
        etNewItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnAdd.setEnabled(s.toString().length() !=0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     *
     */

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {

                final int position = pos;

                AlertDialog confirmDialog = new AlertDialog.Builder(MainActivity.this).create();
                confirmDialog.setTitle(R.string.app_name);
                confirmDialog.setMessage("Are you sure to delete ?");

                confirmDialog.setButton( AlertDialog.BUTTON_POSITIVE,"yes", new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                    }
                });

                confirmDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"NO", new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                confirmDialog.show();
                return true;
            }
        });

        // set up item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create the new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                // pass the data being edited
                i.putExtra(ITEM_TEXT,items.get(position));
                i.putExtra(ITEM_POSITION,position);

                // display the activity
                startActivityForResult(i,EDIT_REQUEST_CODE);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the edit activity completed ok
        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE)
        {
            //extract updated item text from result intent extra
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extract original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model with the new item text at the edited position
            items.set(position,updatedItem);
            //notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();
            //persist the changed model
            writeItems();
            //notify the user the operation completed ok
            Toast.makeText(MainActivity.this,"Item updated succesfully",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    public void onAddItem()
    {
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item added to list",Toast.LENGTH_SHORT).show();
    }

    private File getDataFile(){
        return new File(getFilesDir(),"todo.txt");
    }

    private void readeItems()
    {
        try
        {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e)
        {
            Toast.makeText(getApplicationContext(),"Error reading file "+e,Toast.LENGTH_SHORT).show();
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try{
            FileUtils.writeLines(getDataFile(),items);
        }catch(IOException e){
            Toast.makeText(getApplicationContext(),"Error writing file "+e,Toast.LENGTH_SHORT).show();
        }
    }

}
