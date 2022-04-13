package br.com.zup.edu.fornecedormanager.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.GrupoDeFornecedores;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;

public class GrupoDeFornecedoresDTO {
	
	@NotBlank
	private String produto;
	
	@NotEmpty
    private Set<Long> fornecedorIds;

	public GrupoDeFornecedoresDTO(@NotBlank String produto, @NotEmpty Set<Long> fornecedorIds) {
		this.produto = produto;
		this.fornecedorIds = fornecedorIds;
	}
	
	public GrupoDeFornecedoresDTO() {
		
	}

	public GrupoDeFornecedores toModel(FornecedorRepository fornecedorRepository) {
		GrupoDeFornecedores grupoDeFornecedores = new GrupoDeFornecedores(produto);
		
		List<Fornecedor> fornecedores = fornecedorIds.stream()
                .map(idFornecedor -> fornecedorRepository.findById(idFornecedor).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não cadastrado")))
                .collect(Collectors.toList());
		
		fornecedores.forEach(f -> grupoDeFornecedores.adicionar(f));
		
		return grupoDeFornecedores;
	}

	public String getProduto() {
		return produto;
	}

	public Set<Long> getFornecedorIds() {
		return fornecedorIds;
	}
	
}
