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
@Table(name = "informacoes_meio_transporte")
public class InformacaoMeioTransporte {

    @Id
    @Column(name = "cnh")
    @JsonProperty("cnh")
    private String cnh;

    @JoinColumn(name = "id_tipo_transporte", referencedColumnName = "id_tipo_transporte")
    @JsonProperty("id_tipo_transporte")
    private Long idTipoTransporte;

    @Column(name = "tipo_combustivel")
    @JsonProperty("tipo_combustivel")
    private String tipoCombustivel;

    @Column(name = "distancia_diaria_percorrida")
    @JsonProperty("distancia_diaria_percorrida")
    private Double distanciaDiariaPercorrida;

    @Column(name = "consumo_litro")
    @JsonProperty("consumo_litro")
    private Double consumoLitro;
}
