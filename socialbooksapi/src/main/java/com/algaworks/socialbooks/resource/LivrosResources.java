package com.algaworks.socialbooks.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.socialbooks.domain.Comentario;
import com.algaworks.socialbooks.domain.Livro;
import com.algaworks.socialbooks.services.LivrosService;

//---------------------------- Classe responsável por ser o rest controller de livros conforme a anotação @RestController, mesma responsável
//por fazer o gerenciamento do domínio Livro
@RestController
@RequestMapping("/livros")
public class LivrosResources {
	
	//@Autowired
	//private LivrosRepository livrosRepository;
	
	@Autowired
	private LivrosService livrosService;
		
	public void ocultarComentariosDoMetodoListar() {
//Anotação @RequestMapping, serve para fazer o mapeamento do objeto no browse, onde pode ser escrito como localhost:8080/livros e trás as informações
//que estão abaixo deste request get no browse.
//	@RequestMapping(method = RequestMethod.GET)
	//public ResponseEntity<List<Optional<Livro>>> listar() {
	//	public ResponseEntity<List<Livro>> listar(){
		//Livro l1 = new Livro("Rest Aplicado");
		//Livro l2 = new Livro("Git passo a passo");
		//Livro[] livros = {l1,l2};
		//Retorna um array de livros no browse connforme o objeto livro com seus getters and setters
		//return Arrays.asList(livros);
		//Este comando faz com que vamos a camada de acesso a dados da camada Repository e mandei listar todos, quem executa isso é o
		//Spring Data
		
		//Retornando resposta pro 200 - OK
	//	return ResponseEntity.status(HttpStatus.OK).body(livrosService.listar());
	//}
	}
		
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Livro>> listar(){
		return ResponseEntity.status(HttpStatus.OK).body(livrosService.listar());	
	}
	
	//O Post é o método HTTP responsável pela criação de novos recursos, é ele de fato que garante que um recurso vai ser criado
	@RequestMapping(method = RequestMethod.POST)
	//Request body diz ao spring pra pegar o que ta vindo na requisição e colocar essas informações dentro deste objeto livro
	public ResponseEntity<Void> salvar(@RequestBody Livro livro) {
		
		//Vai fazer persistir a entidade que queremos, vai receber um livro e salvar um livro
		livrosService.salvar(livro);	
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(livro.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	//Mapeando o /id faz com que possamos fazer uma busca especifica
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	//O path variable vai pegar qualquer variavel ID que vier no request mapping e configurar no long id dentro do método buscar
	public ResponseEntity<?> buscar(@PathVariable("id") Long id) {
		Optional<Livro> livro = livrosService.buscar(id);
		//CacheControl cacheControl = CacheControl.maxAge(45, TimeUnit.SECONDS);
		return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.maxAge(60,TimeUnit.SECONDS)).body(livro);
	}
	
	public void comentariosBuscar() {
		//public ResponseEntity<?> buscar(@PathVariable("id") Long id) {
			//Optional<Livro> livro = livrosService.buscar(id);
			
	//------Refatorado depois do ResourceExceptionHandler----------------------------------------------		
			//Optional<Livro> livro = null;
			//try {			 
				//Livro livro = livrosRepository.findById(id);
				//livro = livrosService.findById(id);
			//	livro = livrosService.buscar(id);	
			//} catch (LivroNaoEncontradoException e) {
			//	return ResponseEntity.notFound().build();
			//}
	//--------------------------------------------------------------------------------------------------		
			
			//Refatorado no LivrosServices
			//if (livro == null || livro.isEmpty()) {
			//	return ResponseEntity.notFound().build();
			//}				
			
			//modificado findOne para FindById por conta da modificação da entrada do método na versão do springboot
			//Retornando resposta pro 200 - OK
			//return ResponseEntity.status(HttpStatus.OK).body(livro);
		
	}

	//Ao chamar o método http Delete ele vai deletar o que estiver passando pelo método deletar por id
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		livrosService.deletar(id);
	
//------Refatorado depois do ResourceExceptionHandler----------------------------------------------		
//		try {
//			livrosService.deletar(id);
//		 	} catch (LivroNaoEncontradoException e) {
//			return ResponseEntity.notFound().build();
//			}
//--------------------------------------------------------------------------------------------------		
		//tratamento mais adequado do método deletar
		return ResponseEntity.noContent().build();
	}
	
	//Ao chamar o método http Delete ele vai alterar o que estiver passando pelo método alterar por id
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizar(@RequestBody Livro livro, @PathVariable("id") Long id) {
		//para garantir que quem vai ser atualizado vai ser o recurso que estou passando na URI e não no corpo da mensagem., é necessário dar um setId. 
		livrosService.atualizar(livro);
		
//------Refatorado depois do ResourceExceptionHandler----------------------------------------------		
//		try {	
//		Este save dentro do nosso livrosRepository está fazendo um merge, se tiver algum id ele altera, se não ele insere um novo.
//		livrosService.atualizar(livro);
//		} catch (LivroNaoEncontradoException e) {
//			return ResponseEntity.notFound().build();
//		}
//--------------------------------------------------------------------------------------------------
		//tratamento mais adequado do método alterar
		return ResponseEntity.noContent().build();
		
	}
	
	//Criando o comentário dentro do livro.
	@RequestMapping(value = "/{id}/comentarios", method = RequestMethod.POST)
	public ResponseEntity<Void> adicionarComentario(@PathVariable("id") Long livroId, 
			@RequestBody Comentario comentario) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		comentario.setUsuario(auth.getName());
		
		livrosService.salvarComentario(livroId, comentario);	
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(uri).build();
		
	}
	
	
	//Criando o list dos comentários dentro do livro, capturando apenas os comentarios do id do livro especifico
	@RequestMapping(value = "/{id}/comentarios", method = RequestMethod.GET)
	public ResponseEntity<List<Comentario>> listarComentarios(@PathVariable("id")Long livroId){
		List<Comentario> comentarios = livrosService.listarComentarios(livroId);
		return ResponseEntity.status(HttpStatus.OK).body(comentarios);
	}
	
}
