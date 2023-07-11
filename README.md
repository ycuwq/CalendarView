# CalendarView

Deprecated

A support contraction and expansion of the Android calendar view.

![screen](https://raw.githubusercontent.com/ycuwq/CalendarView/master/screenshots/calendarView.gif)

## Import
```
dependencies {
	implementation 'com.ycuwq.widgets:calendarview:latest-version'
}
```


## Usage

### In XML

```
<com.ycuwq.calendarview.CalendarLayout
    android:id="@+id/calendarLayout"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <com.ycuwq.calendarview.CalendarView
        android:id="@+id/calendarView"
        app:bottomTextSize="12sp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
        
    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:background="@color/colorText"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</com.ycuwq.calendarview.CalendarLayout>
```

if you want to add scheme:

```
calendarView.setOnPageSelectedListener(new CalendarView.OnPageSelectedListener() {
    @Override
    public List<Date> onPageSelected(@NonNull PagerInfo pagerInfo) {
        int year = pagerInfo.getYear(), month = pagerInfo.getMonth(), mondayDay = pagerInfo.getMondayDay();
        if (pagerInfo.getType() == PagerInfo.TYPE_MONTH) {
            List<Date> scheme = new ArrayList<>();
            scheme.add(new Date(year, month, 1));
            scheme.add(new Date(year, month, 11));
            return scheme;
        } else {
            //周模式
            List<Date> schemes = new ArrayList<>();
            schemes.add(new Date(year, month, mondayDay));
            return schemes;
        }
    }

});
```



## License

```
MIT License

Copyright (c) 2018 ycuwq

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

