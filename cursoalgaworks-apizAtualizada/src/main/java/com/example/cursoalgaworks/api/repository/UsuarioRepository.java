package com.example.cursoalgaworks.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cursoalgaworks.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Optional<Usuario> findByEmail(String email);
	
	public List<Usuario> findByPermissoesDescricao(String permissaoDescricao);
	
}
