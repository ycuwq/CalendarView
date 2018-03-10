package com.example.calendarviewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ycuwq.calendarview.CalendarLayout;
import com.ycuwq.calendarview.CalendarView;
import com.ycuwq.calendarview.Date;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private final String TAG = getClass().getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarLayout calendarLayout = findViewById(R.id.calendarLayout);
        final CalendarView calendarView = findViewById(R.id.calendarView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ExRecyclerAdapter<String> adapter = new ExRecyclerAdapter<String>(this,
                R.layout.item_choose) {
            @Override
            public void bindData(ExRecyclerViewHolder holder, String s, int position) {
                TextView textView = holder.getView(R.id.tv_item_choose);
                textView.setText(s);
                holder.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (calendarView.getCalendarType() == CalendarView.TYPE_MONTH) {
                            calendarView.setTypeToWeek();
                        } else {
                            calendarView.setTypeToMonth();
                        }
                    }
                });
            }
        };
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            strings.add("Item "+ i);
        }
        adapter.setList(strings);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        calendarView.setSchemes(getSchemes());
    }

    private List<Date> getSchemes() {
        List<Date> dates = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            dates.add(new Date(2018, 3, i));
            dates.add(new Date(2018, 4, i));
            dates.add(new Date(2018, 5, i));
            dates.add(new Date(2018, 6, i));
            dates.add(new Date(2018, 7, i));
        }
        return dates;
    }
}
