package br.com.aps.simulador.ambiental.controller;

import br.com.aps.simulador.ambiental.model.MeioTransporte;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class MeioAmbienteController {
    private MeioTransporte meioTransporte;

    @PostMapping("/{id}/meio-transporte")
    public ResponseEntity<MeioTransporte> salvarInformacoesMeioTransporte(@PathVariable("id") Long id,
                                                                          @RequestBody MeioTransporte request) {
        // Implementar a lógica para salvar as informações do meio de transporte no banco e retornar elas.
        return ResponseEntity.status(HttpStatus.CREATED).body(informacoesSalvas).build();
    }

    @GetMapping("/{id}/impactos-ambientais")
    public ResponseEntity<MeioTransporte> calcularImpactoMeioTransporte(@PathVariable("id") Long id) {
        // Implementar a lógica para calcular as informações dos cálculos do impacto de co2 e retorná-los.
        return ResponseEntity.status(HttpStatus.OK).body(calculoImpactos).build();
    }

    @GetMapping("/{id}/sugestoes")
    public ResponseEntity<MeioTransporte> consultarSugestoes(@PathVariable("id") Long id) {
        // Implementar a lógica para sugerir como o usuário pode melhorar seu transporte para reduzir os impactos no meio ambiente e retorná-los.
        return ResponseEntity.status(HttpStatus.OK).body(sugestoes).build();
    }
}
