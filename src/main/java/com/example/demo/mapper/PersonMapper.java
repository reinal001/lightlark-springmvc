package com.example.demo.mapper;

import com.example.demo.model.PersonRecord;

import java.util.List;


public interface PersonMapper {
    List<PersonRecord> selectAll();
    void updateAnnotation(PersonRecord packet);
    void insertOneRecord(PersonRecord packet);
    void deleteAll();
}
