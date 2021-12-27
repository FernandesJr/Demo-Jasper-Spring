package com.mballem.curso.jasper.spring.controller;

import com.mballem.curso.jasper.spring.entity.Funcionario;
import com.mballem.curso.jasper.spring.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;

@Controller
public class HomeController {

    @Autowired
    private Connection connection;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/conn")
    public String connection(Model model) {
        model.addAttribute("conn", this.connection != null ? "Conexão OK !":"Ops.. sem conexão");
        return "index";
    }

    @GetMapping("/validarCertificado")
    public String connection(@RequestParam("id_funcionario") Long id, Model model) {
        Funcionario funcionario = funcionarioRepository.findById(id).get();
        model.addAttribute("mensagem", "Certificamos a veracidade deste certificado, " +
                "pertencente a " + funcionario.getNome() + ", emitido em " + funcionario.getDataDemissao());
        return "index";
    }

}
