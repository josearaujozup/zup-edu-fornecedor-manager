package br.com.zup.edu.fornecedormanager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class GrupoDeFornecedores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String produto;


    @OneToMany(
    	      cascade=CascadeType.REMOVE, 
    	      mappedBy="grupo"
    	    )
    private List<Fornecedor> fornecedores= new ArrayList<>();

    public GrupoDeFornecedores(String produto) {
        this.produto = produto;
    }

    /**
     * @deprecated  construtor para uso exclusivo do Hibernate
     */
    @Deprecated
    public GrupoDeFornecedores() {
    }

	public void adicionar(Fornecedor f) {
		this.fornecedores.add(f);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
