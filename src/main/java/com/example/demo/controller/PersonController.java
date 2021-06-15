package com.example.demo.controller;

import com.example.demo.model.PersonRecord;
import com.example.demo.model.MyUtil;
import com.example.demo.service.PersonService;
import com.example.demo.service.ExcelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping("/person")
public class PersonController {
    static String pictures = "src/main/resources/temp/pictures";
    static String dataSource = "src/main/resources/temp/result.xlsx";
    @ResponseBody
    @RequestMapping(value = "/")
    public String hello() {
        return "Hello World";

    }

    @Autowired
    PersonService personService;

    @Autowired
    ExcelService excleImpl;//如果这个类要用到其他类的bean注入，需要她本身也是bean注入的。某种原因脱离了sring管理。比如反射机制就会脱离

    @ResponseBody
    @RequestMapping(value = "/init")
    public String initDB(){
        personService.initDB(dataSource);
        return "done";
    }

    @RequestMapping(value = "/list")
    public ModelAndView index(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "5") int pageSize){
        //只有紧跟在PageHelper.startPage（）方法后的第一个Mybatis的查询（Select）方法会被分页
        System.out.println("page:" + pageNo + "," + pageSize);
        PageHelper.startPage(pageNo, pageSize);
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<PersonRecord> l = personService.list();
            PageInfo<PersonRecord> pageInfo = new PageInfo<>(l);
            modelAndView.addObject("dataList", pageInfo);
            modelAndView.setViewName("index");
        }finally {
            PageHelper.clearPage();
        }
        modelAndView.addObject("dataOneLine", new PersonRecord());
        return modelAndView;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ResponseBody
    public String saveToDb(@ModelAttribute(value = "dataOneLine") PersonRecord dataOneLine){
        personService.parseAndWriteToDB(dataOneLine);
        return "redirect:/person/list?pageNo=" + dataOneLine.getPageNo();
//        return dataOneLine.toString();
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public String deleteAll(){
        personService.deleteAll();
        return "delete done";
    }

    @RequestMapping(value = "/seekPicture")
    @ResponseBody
    public String seekPicture(HttpServletRequest request,
                              HttpServletResponse response, String path) {
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(pictures + "/" + path);
            os = response.getOutputStream();
            int count = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((count = fis.read(buffer)) != -1) {
                os.write(buffer, 0, count);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fis.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @RequestMapping(value = "/download")
    @ResponseBody
    public String downloadExcel(HttpServletResponse response){
        response.setContentType("application/binary;charset=UTF-8");
        try{
            ServletOutputStream out = response.getOutputStream();
            try {
                String name = "webResult" + MyUtil.getTimeStr() + ".xlsx";
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(name, "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String[] titles = { "时间", "出入口", "姓名", "方向", "图片",
                    "正确", "错误",
            };
            excleImpl.export(titles, out);
            return "success";
        } catch(Exception e){
            e.printStackTrace();
            return "导出信息失败";
        }
    }

}
