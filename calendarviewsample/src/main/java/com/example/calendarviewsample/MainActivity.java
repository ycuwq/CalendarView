package com.example.calendarviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ExRecyclerAdapter<String> adapter = new ExRecyclerAdapter<String>(this,
                R.layout.item_choose) {
            @Override
            public void bindData(ExRecyclerViewHolder holder, String s, int position) {
                TextView textView = holder.getView(R.id.tv_item_choose);
                textView.setText(s);
            }
        };
        recyclerView.setAdapter(adapter);

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            strings.add(" 列表项"+ i);
        }
        adapter.setList(strings);
    }
}
