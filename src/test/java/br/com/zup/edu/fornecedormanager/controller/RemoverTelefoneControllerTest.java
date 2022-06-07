package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class RemoverTelefoneControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @BeforeEach
    void setUp() {
        telefoneRepository.deleteAll();
        fornecedorRepository.deleteAll();
    }

    @Test
    @DisplayName("não deve remover um telefone de um fornecedor não cadastrado")
    void naoDeveRemoverUmTelefoneDeFornecedorNaoCadastrado() throws Exception {

        Fornecedor fornecedor = new Fornecedor("Sadia", "costela", "Sadia Ltda");
        fornecedorRepository.save(fornecedor);
        Telefone telefone = new Telefone(fornecedor, "89", "999999999");
        telefoneRepository.save(telefone);

        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}",
                Integer.MAX_VALUE, telefone.getId()).contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("Fornecedor nao cadastrado", exception.getReason());
    }

    @Test
    @DisplayName("não deve remover um telefone não cadastrado de um fornecedor")
    void naoDeveRemoverUmTelefoneNaoCadastradoDeFornecedor() throws Exception {

        Fornecedor fornecedor = new Fornecedor("Sadia", "costela", "Sadia Ltda");
        fornecedorRepository.save(fornecedor);
        Telefone telefone = new Telefone(fornecedor, "89", "999999999");
        telefoneRepository.save(telefone);

        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}",
                fornecedor.getId(), Integer.MAX_VALUE).contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("Telefone nao cadastrado", exception.getReason());
    }


    @Test
    @DisplayName("não deve remover um telefone não pertencente a um fornecedor")
    void naoDeveRemoverUmTelefoneNaoPertencenteFornecedor() throws Exception {

        Fornecedor fornecedor1 = new Fornecedor("Sadia", "costela", "Sadia Ltda");
        Fornecedor fornecedor2 = new Fornecedor("Nestle", "chocolate", "Nestle Ltda");
        fornecedorRepository.saveAll(List.of(fornecedor1,fornecedor2));

        Telefone telefone = new Telefone(fornecedor2, "89", "999999999");
        telefoneRepository.save(telefone);

        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}",
                fornecedor1.getId(), telefone.getId()).contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("Este telefone nao pertence ao fornecedor", exception.getReason());
    }

    @Test
    @DisplayName("não deve remover um telefone pertencente a um fornecedor")
    void deveRemoverUmTelefonePertencenteFornecedor() throws Exception {

        Fornecedor fornecedor1 = new Fornecedor("Sadia", "costela", "Sadia Ltda");
        Fornecedor fornecedor2 = new Fornecedor("Nestle", "chocolate", "Nestle Ltda");
        fornecedorRepository.saveAll(List.of(fornecedor1,fornecedor2));

        Telefone telefone = new Telefone(fornecedor1, "89", "999999999");
        telefoneRepository.save(telefone);

        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}",
                fornecedor1.getId(), telefone.getId()).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(
                        status().isNoContent()
                );

        assertFalse(telefoneRepository.existsById(telefone.getId()),"não deve existir um registro para esse id");

    }


}