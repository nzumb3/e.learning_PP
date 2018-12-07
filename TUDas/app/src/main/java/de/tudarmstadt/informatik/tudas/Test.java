package de.tudarmstadt.informatik.tudas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        String[] array = new String[]{"Hallo", "Was", "oe"};
        ArrayAdapter<String> test = new ArrayAdapter<>(this, R.layout.rect, array);
        ListView tmp = (ListView) findViewById(R.id.listview);
        tmp.setAdapter(test);
    }
}