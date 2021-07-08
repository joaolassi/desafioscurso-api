package com.example.cursoalgaworks.api.repository.lancamento;

import java.util.List;

import com.example.cursoalgaworks.api.model.Lancamento;
import com.example.cursoalgaworks.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
	
}
