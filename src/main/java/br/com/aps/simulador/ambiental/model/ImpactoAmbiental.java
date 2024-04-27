package br.com.aps.simulador.ambiental.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ImpactoAmbiental {

    @JsonProperty("emissão_diaria_co2")
    private Double emissaoDiariaCO2;

    @JsonProperty("emissão_media_mensal_co2")
    private Double emissaoMensalCO2;

    @JsonProperty("emissão_media_anual_co2")
    private Double emissaoAnualCO2;
}
