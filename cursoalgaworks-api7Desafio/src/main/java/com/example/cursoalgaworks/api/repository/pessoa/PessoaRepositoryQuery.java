package com.example.cursoalgaworks.api.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cursoalgaworks.api.model.Pessoa;
import com.example.cursoalgaworks.api.repository.filter.PessoaFilter;

public interface PessoaRepositoryQuery {
	
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);
	
}
