package com.algaworks.socialbooks.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@Entity
public class Comentario {//implements Serializable{

	//private static final long serialVersionUID = 1L;
	
	//Estratégia para gerar o ID no banco
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;
	
	@JsonInclude(Include.NON_NULL)
	private String usuario;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonInclude(Include.NON_NULL)
	private Date data;	
	
	
	//Varios livros podem ter 1 comentário
	@ManyToOne(fetch = FetchType.LAZY)
	//criando uma coluna livro id para fazer o join
	@JoinColumn(name = "LIVRO_ID")
	//Se não inserir essa anotação abaixo, quando o jackson for criar o json, pelo objeto livro ter um objeto comentário
	//e o objeto comentário ter um objeto livro, começa a fazer um loop até o estouro de memória.	
	
	@JsonIgnore
	private Livro livro;

	public Livro getLivro() {
		return livro;
	}
	
	//Optional para o livro por causa que o findbyid foi modificado na versão 4 do STS, fazendo com que seja necessário
	//usar o Optional dentro do objeto
	public void setLivro(Optional<Livro> livro) {
		this.livro = livro.get();
	}
	
	//public static long getSerialversionuid() {
	//	return serialVersionUID;
	//}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}




}
