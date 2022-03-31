package br.com.zup.edu.fornecedormanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Telefone {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 2)
    private String ddd;

    @Column(nullable = false, length = 15)
    private String numero;
	
	@ManyToOne(optional = false)
	private Fornecedor fornecedor;

	public Telefone(String ddd, String numero, Fornecedor fornecedor) {
		this.ddd = ddd;
		this.numero = numero;
		this.fornecedor = fornecedor;
	}
	
	/**
	 * @deprecated construtor é de uso do hibernate
	 */
	@Deprecated
	public Telefone() {
		
	}
	
	public Long getId() {
		return id;
	}
	
}
