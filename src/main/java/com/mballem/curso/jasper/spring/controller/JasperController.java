package com.mballem.curso.jasper.spring.controller;

import com.mballem.curso.jasper.spring.service.JasperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class JasperController {

    @Autowired
    JasperService service;

    @GetMapping("/abrir-relatorios")
    public String abrir(){
        return "report";
    }

    @GetMapping("/relatorio/pdf")
    public void abrirRelatorio(@RequestParam("code") String code,
                               @RequestParam("action") String action,
                               HttpServletResponse response){
        System.out.println("funcionario-"+code);
        byte[] pdf = this.service.exportarPDF(code);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        if(action.equals("v")){
            //Visualiza no próprio navegador
            response.setHeader("Content-disposition","inline; filename=relatorio-"+code+".pdf");
        }else{
            //Download do pdf
            response.setHeader("Content-disposition","attachment; filename=relatorio-"+code+".pdf");
        }
        try {
            response.getOutputStream().write(pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
