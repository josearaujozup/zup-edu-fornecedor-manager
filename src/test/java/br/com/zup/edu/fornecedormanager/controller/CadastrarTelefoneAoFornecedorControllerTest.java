package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;
import br.com.zup.edu.fornecedormanager.util.MensagemDeErro;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class CadastrarTelefoneAoFornecedorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    private Fornecedor fornecedor;

    @BeforeEach
    void setUp(){
        this.telefoneRepository.deleteAll();
        this.fornecedorRepository.deleteAll();

        this.fornecedor = new Fornecedor("Maizena ltda","bolacha","Balduco");
        fornecedorRepository.save(fornecedor);
    }

    @Test
    @DisplayName("Deve cadastrar uma telefone para um fornecedor")
    void test1() throws Exception{
        //cenarios
        TelefoneRequest telefoneRequest = new TelefoneRequest("89","988223344");
        String payload = mapper.writeValueAsString(telefoneRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/fornecedores/{id}/telefones",fornecedor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        //acao e corretude
        mockMvc.perform(request)
                .andExpect(
                        MockMvcResultMatchers.status().isCreated()
                )
                .andExpect(
                        MockMvcResultMatchers.redirectedUrlPattern("http://localhost/fornecedores/*/telefones/*")
                );

        List<Telefone> telefones = telefoneRepository.findAll();
        assertEquals(1,telefones.size());
    }

    @Test
    @DisplayName("Não deve cadastrar um telefone caso um fornecedor não exista")
    void test2() throws Exception{
        TelefoneRequest telefoneRequest = new TelefoneRequest("89","988223344");
        String payload = mapper.writeValueAsString(telefoneRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/fornecedores/{id}/telefones",100000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        //acao e corretude
        mockMvc.perform(request)
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    @DisplayName("Não deve cadastrar um telefone caso os dados sejam inválidos")
    void test3() throws Exception{
        //cenarios

        TelefoneRequest telefoneRequest = new TelefoneRequest(" "," ");
        String payload = mapper.writeValueAsString(telefoneRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/fornecedores/{id}/telefones",fornecedor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language","pt-br")
                .content(payload);

        //acao e corretude
        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        MockMvcResultMatchers.status().isBadRequest()
                ).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);
        assertEquals(2,mensagemDeErro.getMensagens().size());
        assertThat(mensagemDeErro.getMensagens(), containsInAnyOrder(
                "O campo numero não deve estar em branco",
                "O campo ddd não deve estar em branco"
        ));
    }

}