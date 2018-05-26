package com.example.bobbywang.flashdash;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.xeoh.android.texthighlighter.TextHighlighter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    //DatabaseTable dataBase;
    String currentSpinner = "set1.txt";
    Spinner spinner;
    EditText editText;





    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);




        //create database
        //dataBase = new DatabaseTable(this);

        //creating spinner
        spinner = (Spinner)findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("CS245");
        list.add("CS246");
        list.add("STAT230");
        list.add("PSYCH257");
        list.add("PSYCH211");
        list.add("PD1");
        list.add("CS240");
        list.add("MATH239");
        list.add("CS135");
        list.add("+");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentSpinner = spinner.getSelectedItem().toString() + ".txt";
                System.out.println(currentSpinner);
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }


    public void clear(View view) {
        editText.setText("");
    }


    public void loadData() {
        String ret = "";

        try {
            InputStream inputStream = this.openFileInput(currentSpinner);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append( receiveString + '\n');
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (Exception e) {
            ret = "";
        }

        editText.setText(ret);

        new TextHighlighter()
                .setBackgroundColor(Color.parseColor("#FFFF00"))
                .setForegroundColor(Color.RED)
                .setBold(true)
                .setItalic(true)
                .addTarget(editText)
                .highlight(":", TextHighlighter.BASE_MATCHER);






    }

    public void getInfo (View view) {

        Intent intent = new Intent(this, RecursiveDisplayQuiz.class);
        //getting info to message
        EditText mainText = (EditText) findViewById(R.id.editText);
        String message = mainText.getText().toString() + '\n';
        //spltting message to usable data
        String [] entries = message.split("\n");
        String [] term = new String [entries.length]; //not needed
        String [] def = new String [entries.length];  //not needed
        for (int i = 0; i < entries.length; i++){
            term[i] = entries[i].split(":")[0];
            def[i] = entries[i].split(":")[1];
        }
        //ignore
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void saveInfo (View view) {
        //add protocol to write to database here
        EditText mainText = (EditText) findViewById(R.id.editText);
        String message = mainText.getText().toString() + '\n';
        //spltting message to usable data
        String [] entries = message.split("\n");
        String [] term = new String [entries.length]; //not needed
        String [] def = new String [entries.length];  //not needed
        for (int i = 0; i < entries.length; i++){
            term[i] = entries[i].split(":")[0];
            def[i] = entries[i].split(":")[1];
        }



        File path = this.getFilesDir();
        File file = new File(path, currentSpinner);

        try {
            FileOutputStream stream = new FileOutputStream(file);
            for (int i = 0; i < entries.length; i++) {
                stream.write((entries[i]+'\n').getBytes() );
            }
                stream.close();
            } catch (Exception e) {
        }



/*

        try {
            BufferedWriter bw = new BufferedWriter(new InputStreamWriter(openFileInput("res/raw/definitions.txt")));
            for (int i = 0; i < entries.length; i++) {
                bw.write(term[i]);
                bw.write(":");
                bw.write(def[i]);
                bw.newLine();
            }

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
        */


    }



    /*public class DatabaseTable {

        private static final String TAG = "DictionaryDatabase";

        //The columns we'll include in the dictionary table
        public static final String COL_WORD = "WORD";
        public static final String COL_DEFINITION = "DEFINITION";

        private static final String DATABASE_NAME = "DICTIONARY";
        private static final String FTS_VIRTUAL_TABLE = "FTS";
        private static final int DATABASE_VERSION = 1;

        public final DatabaseOpenHelper mDatabaseOpenHelper;

        public DatabaseTable(Context context) {
            mDatabaseOpenHelper = new DatabaseOpenHelper(context);
        }

        private class DatabaseOpenHelper extends SQLiteOpenHelper {

            private final Context mHelperContext;
            private SQLiteDatabase mDatabase;

            private static final String FTS_TABLE_CREATE =
                    "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                            " USING fts3 (" +
                            COL_WORD + ", " +
                            COL_DEFINITION + ")";

            DatabaseOpenHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
                mHelperContext = context;
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                mDatabase = db;
                mDatabase.execSQL(FTS_TABLE_CREATE);
                loadDictionary();
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
                db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
                onCreate(db);
            }

            private void loadWords() throws IOException {
                final Resources resources = mHelperContext.getResources();
                InputStream inputStream = resources.openRawResource(R.raw.definitions);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] strings = TextUtils.split(line, "-");
                        if (strings.length < 2) continue;
                        long id = addWord(strings[0].trim(), strings[1].trim());
                        if (id < 0) {
                            Log.e(TAG, "unable to add word: " + strings[0].trim());
                        }
                    }
                } finally {
                    reader.close();
                }
            }

            public long addWord(String word, String definition) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(COL_WORD, word);
                initialValues.put(COL_DEFINITION, definition);

                return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
            }

            private void loadDictionary() {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            loadWords();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        }


    }
    */

}
