package com.mballem.curso.jasper.spring.repository;

import com.mballem.curso.jasper.spring.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query("SELECT DISTINCT e.uf FROM Endereco e")
    public List<String> findAllUfs();
}
