package com.example.cursoalgaworks.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cursoalgaworks.api.model.Lancamento;
import com.example.cursoalgaworks.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{

}
