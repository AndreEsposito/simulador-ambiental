package br.com.aps.simulador.ambiental.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_transporte")
public class TipoTransporte {

    @Id
    @Column(name = "id_tipo_transporte")
    @JsonProperty("id_tipo_transporte")
    private Long idTipoTransporte;

    @Column(name = "descricao_tipo_transporte", length = 100)
    @JsonProperty("descricao_tipo_transporte")
    private String descricaoTipoTransporte;
}
