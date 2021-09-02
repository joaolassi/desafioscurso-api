package com.example.cursoalgaworks.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.cursoalgaworks.api.model.Pessoa;
import com.example.cursoalgaworks.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa salvar(Pessoa pessoa) {
		// coloco a pessoa que está no parâmetro
		pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {

		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		
		// para atualizar sem que dê erro ou que não atualize é preciso limpar os contatos e adicionar quaisquer contatos inseridos
		pessoaSalva.getContatos().clear();
		pessoaSalva.getContatos().addAll(pessoa.getContatos());
		pessoaSalva.getContatos().forEach(c -> c.setPessoa(pessoaSalva));
		

		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");

		return pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	public Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);

		if (pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}

}
