package com.mballem.curso.jasper.spring.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperService {

    @Autowired
    private Connection connection;

    public static String JASPER_DIRETORIO = "classpath:jasper/"; //classpath é a raiz do projeto, ou seja, diretório resources
    public static String JASPER_PREFIXO = "funcionario-";
    public static String JASPER_SUFIXO = ".jasper";

    public Map<String, Object> params = new HashMap<>();

    public JasperService() {
        this.params.put("DIRETORIO_IMAGE", JASPER_DIRETORIO); //Parametro das imagens de title do relatorio
        this.params.put("SUB_REPORT_DIR", JASPER_DIRETORIO); //Parametro para encontrar o sub-relatorio do funcionarios-10
    }

    public void addParam(String key, Object value){
        this.params.put(key, value);
    }

    public byte[] exportarPDF(String code){
        byte[] bytes = null;
        try {
            File file = ResourceUtils.getFile(JASPER_DIRETORIO+JASPER_PREFIXO+code+JASPER_SUFIXO);
            JasperPrint print = JasperFillManager.fillReport(file.getAbsolutePath(), this.params, this.connection);
            bytes = JasperExportManager.exportReportToPdf(print);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
        }

        return bytes;
    }


}
