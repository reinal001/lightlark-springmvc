package com.example.demo.service;

import com.example.demo.model.PersonRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
 * @Version 1.0
 */
@Service
public class ExcelService {

    @Autowired
    PersonService labService;

    public void export(String[] titles, ServletOutputStream out) throws Exception{
        try{
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet xssfSheet = workbook.createSheet();
            xssfSheet.setColumnWidth(0, 5000);
            xssfSheet.setColumnWidth(1, 4000);
            xssfSheet.setColumnWidth(4, 12000);

            // 设置表头
            Row row = xssfSheet.createRow(0);
            CellStyle hssfCellStyle = workbook.createCellStyle();
            hssfCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            hssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Cell xssfCell = null;
            for (int i = 0; i < titles.length; i++) {
                xssfCell = row.createCell(i);
                xssfCell.setCellValue(titles[i]);
                xssfCell.setCellStyle(hssfCellStyle);
            }

            List<PersonRecord> list = labService.list();
            for (int i = 0; i < list.size(); i++) {
                row = xssfSheet.createRow(i+1);
                PersonRecord packet = list.get(i);
                String timeStr = packet.getTimeStr();
                String doorId = packet.getDoor_id();

                row.createCell(0).setCellValue(timeStr);
                row.createCell(1).setCellValue(doorId);
                if(packet.getPerson_name() != null){
                    row.createCell(2).setCellValue(packet.getPerson_name());
                }
                if(packet.getDirection() != null){
                    row.createCell(3).setCellValue(packet.getDirection());
                }
                if(packet.getPicture() != null){
                    row.createCell(4).setCellValue(packet.getPicture());
                }

                //后面都是标记的内容
                if(packet.is_correct()) {
                    row.createCell(5).setCellValue(packet.is_correct());
                }
                if(packet.getWrong_result() != null){
                    row.createCell(6).setCellValue(parseInOut(packet.getWrong_result()));
                }
            }

            //将文件输出到客户端浏览器
            try {
                workbook.write(out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("导出信息失败！");

        }
    }

    public String parseInOut(String ori){
        if(ori.endsWith("empty")){
            return "";
        }else if(ori.endsWith("in")){
            return "入";
        }else if(ori.endsWith("out")){
            return "出";
        }else{
            return ori;
        }
    }
}
