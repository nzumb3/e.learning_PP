package de.tudarmstadt.informatik.tudas.customWidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class LabelListView extends ListView {

    public LabelListView(Context context) {
        super(context);
    }

    public LabelListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heigthMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
