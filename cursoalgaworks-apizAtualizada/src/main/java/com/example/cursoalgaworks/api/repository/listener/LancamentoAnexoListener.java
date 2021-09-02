package com.example.cursoalgaworks.api.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.util.StringUtils;

import com.example.cursoalgaworks.api.CursoalgaworksApiApplication;
import com.example.cursoalgaworks.api.model.Lancamento;
import com.example.cursoalgaworks.api.storage.S3;

public class LancamentoAnexoListener {
	
	@PostLoad
	public void postLoad(Lancamento lancamento) {
		if (StringUtils.hasText(lancamento.getAnexo())) {
			S3 s3 = CursoalgaworksApiApplication.getBean(S3.class);
			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
		}
	}
}
