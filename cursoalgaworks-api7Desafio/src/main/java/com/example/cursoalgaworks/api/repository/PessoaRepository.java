package com.example.cursoalgaworks.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cursoalgaworks.api.model.Pessoa;
import com.example.cursoalgaworks.api.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery{
}
