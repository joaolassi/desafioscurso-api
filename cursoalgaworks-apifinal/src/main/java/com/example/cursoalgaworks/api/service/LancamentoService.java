package com.example.cursoalgaworks.api.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}

		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {

		Lancamento lancamentoSalvo = buscarLancamentoPeloCodigo(codigo);

		// condição para validar a pessoa
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}

		// copia as propriedades
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamento);
	}

	private void validarPessoa(Lancamento lancamento) {
		// novidades do spring para buscar pelo id é preciso adicionar o Optional
		Optional<Pessoa> pessoa = null;

		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		}

		if (pessoa == null || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}

	}

	public Lancamento buscarLancamentoPeloCodigo(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo).orElse(null);

		if (lancamentoSalvo == null) {
			throw new IllegalArgumentException();
		}

		return lancamentoSalvo;
	}

}
