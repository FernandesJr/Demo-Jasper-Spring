package com.mballem.curso.jasper.spring.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperService {

    @Autowired
    private Connection connection;

    @Autowired
    private ResourceLoader resourceLoader;

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
            Resource resource = resourceLoader.getResource(JASPER_DIRETORIO+JASPER_PREFIXO+code+JASPER_SUFIXO);
            InputStream stream = resource.getInputStream();
            JasperPrint print = JasperFillManager.fillReport(stream, this.params, this.connection);
            bytes = JasperExportManager.exportReportToPdf(print);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return bytes;
    }


    public HtmlExporter exportarHTML(String code, HttpServletRequest request, HttpServletResponse response) {
        //Para exportar em HTML é necessário direcionar o servlet específico de gerenciamento das imagens do relatório
        //Configurado na class config.ConfiguracaoGenerica
        HtmlExporter htmlExporter = null;
        try {
            Resource resource = resourceLoader.getResource(JASPER_DIRETORIO+JASPER_PREFIXO+code+JASPER_SUFIXO);
            InputStream stream = resource.getInputStream();
            JasperPrint print = JasperFillManager.fillReport(stream, this.params, this.connection);
            htmlExporter = new HtmlExporter();
            htmlExporter.setExporterInput(new SimpleExporterInput(print));

            SimpleHtmlExporterOutput htmlExporterOutput = new SimpleHtmlExporterOutput(response.getWriter());

            HtmlResourceHandler resourceHandler =
                    new WebHtmlResourceHandler(request.getContextPath()+"/image/servlet?image={0}");
            htmlExporterOutput.setImageHandler(resourceHandler);
            htmlExporter.setExporterOutput(htmlExporterOutput);

            request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, print);


        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlExporter;
    }
}
