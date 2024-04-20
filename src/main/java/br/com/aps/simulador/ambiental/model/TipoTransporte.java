package br.com.aps.simulador.ambiental.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_transporte")
public class TipoTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_tipo_transporte")
    @JsonProperty("id_tipo_transporte")
    private Integer idTipoTransporte;

    @Column(name = "descricao_tipo_transporte", length = 100)
    @JsonProperty("descricao_tipo_transporte")
    private String descricaoTipoTransporte;
}
