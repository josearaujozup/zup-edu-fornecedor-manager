package br.com.zup.edu.fornecedormanager.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;

@RestController
@RequestMapping("/fornecedores/{idFornecedor}/telefones")
public class CadastrarNovoTelefoneController {
	
	private final FornecedorRepository fornecedorRepository;
	private final TelefoneRepository repository;
	
	public CadastrarNovoTelefoneController(FornecedorRepository fornecedorRepository, TelefoneRepository repository) {
		this.fornecedorRepository = fornecedorRepository;
		this.repository = repository;
	}
	
	@PostMapping
	public ResponseEntity<Void> cadastrar(@PathVariable Long idFornecedor, @RequestBody @Valid TelefoneDTO request, UriComponentsBuilder uriComponentsBuilder){
		
		Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe cadastro de fornecedor para o id informado"));
		
		Telefone novotelefone = request.paraTelefone(fornecedor);
		repository.save(novotelefone);
		
		URI location = uriComponentsBuilder.path("/fornecedores/{idFornecedor}/telefones/{id}").buildAndExpand(fornecedor.getId(),novotelefone.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
}
