package com.example.cursoalgaworks.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cursoalgaworks.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
