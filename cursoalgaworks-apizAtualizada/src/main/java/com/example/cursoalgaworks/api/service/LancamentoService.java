package com.example.cursoalgaworks.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.cursoalgaworks.api.dto.LancamentoEstatisticaPessoa;
import com.example.cursoalgaworks.api.mail.Mailer;
import com.example.cursoalgaworks.api.model.Lancamento;
import com.example.cursoalgaworks.api.model.Pessoa;
import com.example.cursoalgaworks.api.model.Usuario;
import com.example.cursoalgaworks.api.repository.LancamentoRepository;
import com.example.cursoalgaworks.api.repository.PessoaRepository;
import com.example.cursoalgaworks.api.repository.UsuarioRepository;
import com.example.cursoalgaworks.api.service.exception.PessoaInexistenteOuInativaException;
import com.example.cursoalgaworks.api.storage.S3;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {

	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	@Autowired
	private S3 s3;

	public Lancamento salvar(@Valid Lancamento lancamento) {

		validarPessoa(lancamento);

		if (StringUtils.hasText(lancamento.getAnexo())) {
			s3.salvar(lancamento.getAnexo());
		}

		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoPeloCodigo(codigo);
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}
		
		if (StringUtils.hasText(lancamento.getAnexo())
				&& StringUtils.hasText(lancamentoSalvo.getAnexo())) {
			s3.remover(lancamentoSalvo.getAnexo());
		} else if (StringUtils.hasText(lancamento.getAnexo())
				&& !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
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

	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

//		criando e colocando os parâmetros
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("PT", "BR"));

//	 	pegando a pasta onde está o relatirio
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	// agendamento de servico
	// Fixed Delay: logo quando inicia a aplicação uma chamada para o método é feita
	// depois de encerrado ele conta o tempo para a próxima execução
	// evita gargalos
	// @Scheduled(fixedDelay = 1000 * 5)
	// Cron:
	// Para que o serviço seja chamado em um hoirário especificado
	// segundos minutos hora * * *: são em ordem dia do mês, Mês e dias da semana

	@Scheduled(cron = "0 0 6 * * *")
	public void avisarSobreLancamentosVencidos() {
		// log de informação
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de e-mails de lancamentos vencidos");
		}

		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);

		if (vencidos.isEmpty()) {
			logger.info("Sem lançamentos vencidos para serem avisados.");

			return;
		}

		logger.info("Existem {} lançamentos vencidos", vencidos.size());

		if (destinatarios.isEmpty()) {
			logger.warn("Existem lançamentos vencidos porém o sistema não encontrou destinatários.");

			return;
		}

		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

		logger.info("Envio de e-mail de aviso concluído");
	}
}
