package com.example.cursoalgaworks.api.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cursoalgaworks.api.model.Lancamento;
import com.example.cursoalgaworks.api.model.Pessoa;
import com.example.cursoalgaworks.api.repository.LancamentoRepository;
import com.example.cursoalgaworks.api.repository.PessoaRepository;
import com.example.cursoalgaworks.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	public Lancamento salvar(@Valid Lancamento lancamento) {
		
		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo()).orElse(null);
		
		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

}
