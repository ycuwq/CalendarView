package com.ycuwq.calendarview;

/**
 * Created by ycuwq on 2018/3/6.
 */

public class ViewNotFoundException extends RuntimeException {


    public ViewNotFoundException(String message) {
        throw new RuntimeException(message);
    }
}
