package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;

import javax.validation.constraints.NotBlank;

public class FornecedorDTO {
    @NotBlank
    private String nome;

    @NotBlank
    private String produto;

    @NotBlank
    private String empresa;

    public FornecedorDTO(String nome, String produto, String empresa) {
        this.nome = nome;
        this.produto = produto;
        this.empresa = empresa;
    }

    public FornecedorDTO() {
    }

    public Fornecedor paraFornecedor(){
        return new Fornecedor(nome,produto,empresa);
    }

    public String getNome() {
        return nome;
    }

    public String getProduto() {
        return produto;
    }

    public String getEmpresa() {
        return empresa;
    }
}
