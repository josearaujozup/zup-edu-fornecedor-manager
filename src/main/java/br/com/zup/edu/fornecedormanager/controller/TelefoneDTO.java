package br.com.zup.edu.fornecedormanager.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;

public class TelefoneDTO {
	
	@NotBlank
	@Size(min=2,max=2)
    private String ddd;

	@NotBlank
	@Size(max=15)
    private String numero;

	public TelefoneDTO(@NotBlank @Size(min = 2, max = 2) String ddd, @NotBlank @Size(max = 15) String numero) {
		super();
		this.ddd = ddd;
		this.numero = numero;
	}
	
	public TelefoneDTO() {
		
	}
	
	public Telefone paraTelefone(Fornecedor fornecedor) {
		return new Telefone(ddd,numero,fornecedor);
	}
	
	public String getDdd() {
		return ddd;
	}

	public String getNumero() {
		return numero;
	}
}
