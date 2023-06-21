package br.com.elfs.loja.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "camisas_personalizadas")
@PrimaryKeyJoinColumn(name = "id")
public class CamisaPersonalizada extends Camisa {
    @Column(name = "mensagem_personalizada")
    private String mensagemPersonalizada;

    public CamisaPersonalizada(String nome, String tamanho, BigDecimal preco, Tipo tipo, String time, String mensagemPersonalizada) {
        super(nome, tamanho, preco, tipo, time);
        this.mensagemPersonalizada = mensagemPersonalizada;
    }

    public CamisaPersonalizada() {
    }

    public String getMensagemPersonalizada() {
        return mensagemPersonalizada;
    }

    public void setMensagemPersonalizada(String mensagemPersonalizada) {
        this.mensagemPersonalizada = mensagemPersonalizada;
    }
}
