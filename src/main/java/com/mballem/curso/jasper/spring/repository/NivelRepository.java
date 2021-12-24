package com.mballem.curso.jasper.spring.repository;

import com.mballem.curso.jasper.spring.entity.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {

    @Query("SELECT DISTINCT n.nivel FROM Nivel n")
    public List<String> findAllNiveis();
}
