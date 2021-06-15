package com.example.demo.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Version 1.0
 */
public class MyUtil {
    public static String getTimeStr(){
        LocalDateTime dateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss"));
    }

    public static void sleepMill(int t){
        try{
            Thread.sleep(t);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
