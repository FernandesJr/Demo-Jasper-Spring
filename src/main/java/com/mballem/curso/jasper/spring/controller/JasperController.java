package com.mballem.curso.jasper.spring.controller;

import com.mballem.curso.jasper.spring.repository.EnderecoRepository;
import com.mballem.curso.jasper.spring.repository.FuncionarioRepository;
import com.mballem.curso.jasper.spring.repository.NivelRepository;
import com.mballem.curso.jasper.spring.service.JasperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class JasperController {

    @Autowired
    private JasperService service;

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping("/abrir-relatorios")
    public String abrir(){
        return "report";
    }

    @GetMapping("/relatorio/pdf/jr01")
    public void abrirRelatorio01(@RequestParam("code") String code,
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

    @GetMapping("/relatorio/pdf/jr09/{code}")
    public void abrirRelatorio09(@PathVariable() String code,
                                 @RequestParam(name="nivel", required = false) String nivel,
                                 @RequestParam(name="uf", required = false) String uf,
                                 HttpServletResponse response){
        System.out.println("funcionario-"+code);

        this.service.addParam("NIVEL_DESC", nivel.isEmpty() ? null : nivel); //Precisa colocar null caso venha vazio por causa do SQL que espera alguma coisa ou null
        this.service.addParam("UF", uf.isEmpty() ? null : uf); //Precisa colocar null caso venha vazio por causa do SQL que espera alguma coisa ou null

        byte[] pdf = this.service.exportarPDF(code);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE); //Avisando tipo da média que será gerada para resposta

        response.setHeader("Content-disposition","inline; filename=relatorio-"+code+".pdf"); //Visualiza no próprio navegador
        try {
            response.getOutputStream().write(pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/relatorio/pdf/jr19/{code}")
    public void abrirRelatorio19(@PathVariable("code") String code,
                                 @RequestParam(name="idf", required = false) Long idFuncinario,
                                 HttpServletResponse response){
        System.out.println("funcionario-"+code);

        this.service.addParam("ID_FUNCIONARIO",idFuncinario);

        byte[] pdf = this.service.exportarPDF(code);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE); //Avisando tipo da média que será gerada para resposta

        response.setHeader("Content-disposition","inline; filename=relatorio-"+code+".pdf"); //Visualiza no próprio navegador
        try {
            response.getOutputStream().write(pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/buscar/funcionarios/treinamento")
    public ModelAndView buscarPorNome(@RequestParam(name="nome", required = false) String nome){
        return new ModelAndView("report","funcionarios", funcionarioRepository.findFuncionarioByNomeCertificado(nome));
    }

    @ModelAttribute("niveis")
    public List<String> buscarNiveis(){
        return this.nivelRepository.findAllNiveis();
    }

    @ModelAttribute("ufs")
    public List<String> buscarUfs(){
        return enderecoRepository.findAllUfs();
    }
}
