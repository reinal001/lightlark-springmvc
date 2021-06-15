package com.example.demo.service;

import com.example.demo.mapper.PersonMapper;
import com.example.demo.model.PersonRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @Version 1.0
 */
@Service
public class PersonService {

    @Autowired
    private PersonMapper personMapper;

    public List<PersonRecord> list(){
        return personMapper.selectAll();
    }

    public void insertRecord(PersonRecord packet) {
        personMapper.insertOneRecord(packet);

    }

    public void deleteAll(){
        personMapper.deleteAll();
    }

    public void initDB(String dataFile){
        try{
            File file = new File(dataFile);
            System.out.println("开始处理报文" + file.getAbsolutePath());
            FileInputStream in = new FileInputStream(file);
            Workbook haikang = WorkbookFactory.create(in);
            Sheet sheet = haikang.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowCount; i++){
                Row row = sheet.getRow(i);
                String timeStr = row.getCell(0).getStringCellValue();
                String doorId = row.getCell(1).getStringCellValue();
                String personName = null;
                String direction = null;
                String pictures = null;

                if(row.getCell(2) != null){
                    personName = row.getCell(2).getStringCellValue();
                }
                if(row.getCell(3) != null) {
                    direction = row.getCell(3).getStringCellValue();
                }
                if(row.getCell(4) != null) {
                    pictures = row.getCell(4).getStringCellValue();
                }

                PersonRecord packet = new PersonRecord();
                packet.setTimeStr(timeStr);
                packet.setDoor_id(doorId);
                packet.setPerson_name(personName);
                packet.setPicture(pictures);
                packet.setDirection(direction);
                insertRecord(packet);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //将提交的数据，保存到数据库中
    public void parseAndWriteToDB(PersonRecord line){
        String[] doorId = line.getDoor_id().split(",");
        int size = doorId.length;
        String[] timeStr = line.getTimeStr().split(",");
        boolean[] isCorrect = intToBoolean(line.getIs_correct_array(), size);
        String[] wrongResult = line.getWrong_result().split(",");

        for(int i = 0; i < size; i++){
            PersonRecord packet = new PersonRecord();
            packet.setDoor_id(doorId[i]);
            packet.setTimeStr(timeStr[i]);
            packet.set_correct(isCorrect[i]);
            packet.setWrong_result(wrongResult[i]);
            personMapper.updateAnnotation(packet);
        }
    }

    public boolean[] intToBoolean(int[] index, int size){

        boolean[] ret = new boolean[size];
        if(index == null){
            return ret;
        }

        for(int i : index){
            ret[i] = true;
        }
        return ret;
    }
}
