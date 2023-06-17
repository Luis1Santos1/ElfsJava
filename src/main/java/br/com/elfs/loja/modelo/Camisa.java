package br.com.elfs.loja.modelo;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "camisas")
public class Camisa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "tamanho")
    private String tamanho;
    @Column(name = "preco")
    private BigDecimal preco;
    @Column(name = "time")
    private String time;
    @ManyToOne
    private Tipo tipo;

    public Camisa(String nome, String tamanho, BigDecimal preco, Tipo tipo, String time) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.preco = preco;
        this.time = time;
        this.tipo = tipo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTamanho() {
        return this.tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public BigDecimal getPreco() {
        return this.preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }



    public Camisa id(Long id) {
        setId(id);
        return this;
    }

    public Camisa nome(String nome) {
        setNome(nome);
        return this;
    }

    public Camisa tamanho(String tamanho) {
        setTamanho(tamanho);
        return this;
    }

    public Camisa preco(BigDecimal preco) {
        setPreco(preco);
        return this;
    }

    public Camisa time(String time) {
        setTime(time);
        return this;
    }


}
