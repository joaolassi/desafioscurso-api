package com.example.cursoalgaworks.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cursoalgaworks.api.model.Lancamento;
import com.example.cursoalgaworks.api.repository.filter.LancamentoFilter;
import com.example.cursoalgaworks.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	
}
