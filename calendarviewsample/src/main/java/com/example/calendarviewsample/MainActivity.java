package com.example.calendarviewsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ycuwq.calendarview.CalendarLayout;
import com.ycuwq.calendarview.CalendarView;
import com.ycuwq.calendarview.Date;
import com.ycuwq.calendarview.PagerInfo;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private final String TAG = getClass().getSimpleName();
    private HashMap<String, List<Date>> mScheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarLayout calendarLayout = findViewById(R.id.calendarLayout);
        final CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnPageSelectedListener(new CalendarView.OnPageSelectedListener() {
            @Override
            public List<Date> onPageSelected(@NonNull PagerInfo pagerInfo) {
                if (mScheme == null) {
                    return null;
                }
                int year = pagerInfo.getYear(), month = pagerInfo.getMonth(), mondayDay = pagerInfo.getMondayDay();
                if (pagerInfo.getType() == PagerInfo.TYPE_MONTH) {
                    return mScheme.get(year + "-" + month);
                } else {
                    //周模式
                    List<Date> schemes = new ArrayList<>();
                    LocalDate monday = new LocalDate(year, month, mondayDay);
                    Date tempDate = new Date(year, month, mondayDay);
                    for (int i = 1; i <= 7; i++) {
                        LocalDate localDate = monday.withDayOfWeek(i);
                        tempDate.setYear(localDate.getYear());
                        tempDate.setMonth(localDate.getMonthOfYear());
                        tempDate.setDay(localDate.getDayOfMonth());
                        List<Date> monthScheme = mScheme.get(localDate.getYear() + "-" + localDate.getMonthOfYear());
                        if (monthScheme == null) {
                            continue;
                        }
                        int index = monthScheme.indexOf(tempDate);
                        if (index >= 0) {
                            schemes.add(monthScheme.get(index));
                        }
                    }
                    return schemes;
                }
            }
        });

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        generateSchemes();
    }

    private void generateSchemes() {
        mScheme = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            List<Date> list = new ArrayList<>();
            for (int j = 1; j < 28; j++) {
                list.add(new Date(2018, i, j));
            }
            mScheme.put(2018 + "-" + i, list);
        }
    }
}
