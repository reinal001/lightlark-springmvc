package com.example.demo.model;

import lombok.Data;

/**
 * @Version 1.0
 */

@Data
public class PersonRecord {
    private String timeStr;
    private String door_id;
    private String person_name;
    private String direction;
    private String picture;
    private boolean is_correct;
    private int[] is_correct_array;
    private String wrong_result;
    private int pageNo;


    @Override
    public String toString(){
        return timeStr+", " + door_id +", " + person_name +", " + direction +", " + is_correct +"," + wrong_result +", pageNo=" + pageNo;
    }

}
