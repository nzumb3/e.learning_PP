package de.tudarmstadt.informatik.tudas;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Test extends AppCompatActivity {

    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setupUIViews();
        setupListView();
    }

    private void setupUIViews() {
        listview = (ListView) findViewById(R.id.lvMain);
    }

    private void setupListView() {
        String[] title = getResources().getStringArray(R.array.Main);
        String[] description = getResources().getStringArray(R.array.Description);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, title, description);
        listview.setAdapter(simpleAdapter);
    }

    public class SimpleAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView title, desciption;
        private String[] titleArray;
        private String[] descriptionArray;
        private ImageView imageView;

        public SimpleAdapter(Context context, String[] title, String[] description){
            mContext = context;
            titleArray = title;
            descriptionArray = description;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return titleArray.length;
        }

        @Override
        public Object getItem(int position) {
            return titleArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.rect, null);
            title = (TextView) convertView.findViewById(R.id.tvMain);
            desciption = (TextView) convertView.findViewById(R.id.tvDescription);
            imageView = (ImageView) convertView.findViewById(R.id.ivMain);
            title.setText(titleArray[position]);
            desciption.setText(descriptionArray[position]);
            if (titleArray[position].equalsIgnoreCase("Timetable")){
                imageView.setImageResource(R.drawable.beers);
            } else if (titleArray[position].equalsIgnoreCase("Subjects")) {
                imageView.setImageResource(R.drawable.beers);
            } else if (titleArray[position].equalsIgnoreCase("Faculty")) {
                imageView.setImageResource(R.drawable.beers);
            } else {
                imageView.setImageResource(R.drawable.beers);
            }
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            Log.d("Blubb Bluee", Boolean.toString(layoutParams==null));
            if (position%2==0){
                //layoutParams.setMargins(10, 50, 10, 0);
                //convertView.setLayoutParams(layoutParams);
            }
            return convertView;
        }
    }

}