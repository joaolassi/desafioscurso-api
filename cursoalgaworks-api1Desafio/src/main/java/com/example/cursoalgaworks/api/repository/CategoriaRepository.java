package com.example.cursoalgaworks.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cursoalgaworks.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
