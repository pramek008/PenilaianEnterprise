/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projectKtpUts.uts_pae;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class DataController {
    DataJpaController dataController = new DataJpaController();
    List<Data> data = new ArrayList<>();
    
    @RequestMapping("/")
    public String getData(Model model){
        try {
            data = dataController.findDataEntities();
        } catch (Exception e) {
        }
        model.addAttribute("data", data);
        return "index";
    }
    
    @RequestMapping(value= "/img", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception{
        Data data = dataController.findData(id);
        byte[] img = data.getFoto();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
    }
    
    @RequestMapping("/create")
    public String createData(){
        return "createData";
    }
    
    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)    
    public String newData(@RequestParam("foto") MultipartFile file, HttpServletRequest servletRequest) throws ParseException, Exception{
        Data data = new Data();
        
//        int id = Integer.parseInt(servletRequest.getParameter("id"));
        String nama = servletRequest.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(servletRequest.getParameter("tglLahir"));
        byte[] img = file.getBytes();
        
//        data.setId(id);
        data.setNama(nama);
        data.setTglLahir(date);
        data.setFoto(img);
        
        dataController.create(data);
        return "redirect:/"; 
//        return "dataCreated";
    }
    
    @GetMapping("/delete/{id}")    
    public String deleteData(@PathVariable("id") int id, Model model) throws Exception{
        dataController.destroy(id);
        return "redirect:/"; 
    }
    
    @RequestMapping("/edit/{id}")    
    public String getDataById(@PathVariable("id") int id, Model model) throws Exception {
        Data data = dataController.findData(id);
        model.addAttribute("data", data);
        return "updateData";
    }
    
    @PostMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateData(@RequestParam("foto") MultipartFile file, HttpServletRequest servletRequest) throws ParseException, Exception{
        Data data = new Data();
        
        int id = Integer.parseInt(servletRequest.getParameter("id"));
        String nama = servletRequest.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(servletRequest.getParameter("tglLahir"));
        byte[] img = file.getBytes();
        
        data.setId(id);
        data.setNama(nama);
        data.setTglLahir(date);
        data.setFoto(img);
        
        dataController.edit(data);
        return "redirect:/";
    }
}
        
        