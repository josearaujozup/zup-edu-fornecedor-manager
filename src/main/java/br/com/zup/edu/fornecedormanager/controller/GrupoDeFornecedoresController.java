package br.com.zup.edu.fornecedormanager.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.edu.fornecedormanager.model.GrupoDeFornecedores;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.GrupoDeFornecedorRepository;

@RestController
@RequestMapping("/grupoDeFornecedores")
public class GrupoDeFornecedoresController {
	
	private final GrupoDeFornecedorRepository repository;
	private final FornecedorRepository fornecedorRepository;
	
	public GrupoDeFornecedoresController(GrupoDeFornecedorRepository repository, FornecedorRepository fornecedorRepository) {
		this.repository = repository;
		this.fornecedorRepository = fornecedorRepository;
	}
	
	@PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid GrupoDeFornecedoresDTO request, UriComponentsBuilder uriComponentsBuilder){

		GrupoDeFornecedores novoGrupoDeFornecedores = request.toModel(fornecedorRepository);

        repository.save(novoGrupoDeFornecedores);

        URI location = uriComponentsBuilder.path("/grupoDeFornecedores/{id}")
                .buildAndExpand(novoGrupoDeFornecedores.getId())
                .toUri();


        return ResponseEntity.created(location).build();
    }
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable("id") Long idGrupo){
		
		GrupoDeFornecedores grupo = repository.findById(idGrupo).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe cadastro de grupo para o id informado"));
		
		repository.delete(grupo);
		
		return ResponseEntity.noContent().build();
	}
	
	
}
