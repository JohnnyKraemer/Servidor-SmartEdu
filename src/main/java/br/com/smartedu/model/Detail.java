package br.com.smartedu.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString
public class Detail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String periodoCarga;
    private Integer periodo;
    private Integer matriz;
    private Integer idade_situacao;
    private Double cr;
    private Integer ano_situacao;
    private Integer semestre_situacao;
    private Integer quant_semestre_cursados;
    private Integer disciplinas_aprovadas;
    private Integer disciplinas_consignadas;
    private Integer disciplinas_matriculadas;
    private Integer disciplinas_reprovadas_frequencia;
    private Integer disciplinas_reprovadas_nota;
    private String provavel_jubilamento;
    private String retencao_parcial;
    private String retencao_total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "situation_id", referencedColumnName = "id")
    private Situation situation;
}
