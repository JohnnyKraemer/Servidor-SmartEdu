package br.com.smartedu.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class Detail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loadingPeriod;
    private Integer period;
    private Integer matrix;
    private Integer age_situation;
    private Double coefficient;
    private Integer year_situation;
    private Integer semester_situation;
    private Integer semesters;
    private Integer disciplines_approved;
    private Integer disciplines_consigned;
    private Integer disciplines_matriculate;
    private Integer disciplines_reprovated_frequency;
    private Integer disciplines_reprovated_note;
    private String likely_retirement;

/*    private String periodoCarga;
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
    private String retencao_total;*/

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "situation_id", referencedColumnName = "id")
    private Situation situation;
}
