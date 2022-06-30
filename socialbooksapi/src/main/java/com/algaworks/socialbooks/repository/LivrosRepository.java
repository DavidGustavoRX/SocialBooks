package com.algaworks.socialbooks.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.socialbooks.domain.Livro;

public interface LivrosRepository extends JpaRepository<Livro, Long>{
//Optional<Livro> findById(Long id);



	
	
}
