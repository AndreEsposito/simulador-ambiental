package br.com.aps.simulador.ambiental.controller;

import br.com.aps.simulador.ambiental.model.ImpactoAmbiental;
import br.com.aps.simulador.ambiental.model.InformacaoMeioTransporte;
import br.com.aps.simulador.ambiental.repository.InformacaoMeioTransporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class InformacaoMeioTransporteController {
    private final InformacaoMeioTransporteRepository repository;

    @PostMapping("/{cnh}/meio-transporte")
    public ResponseEntity<InformacaoMeioTransporte> salvarInformacoesMeioTransporte(@PathVariable("cnh") String cnh,
                                                                                    @RequestBody InformacaoMeioTransporte request) {
        request.setCnh(cnh);

        validaCNH(cnh);
        validaEntrada(request);

        if (repository.existsById(request.getCnh())) {
            repository.deleteById(request.getCnh());
        }

        repository.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/{cnh}/impactos-ambientais")
    public ResponseEntity<ImpactoAmbiental> calcularImpactoMeioTransporte(@PathVariable("cnh") String cnh) {
        validaCNH(cnh);

        if (!repository.existsById(cnh)) {
            throw new IllegalArgumentException("A CNH informada não existe.");
        }

        var gasolina = 0.75 * 3.7 * 0.82;
        var etanol = 0.782 * 2.0 * 0.95;
        var diesel = 0.85 * 12.0 * 1.22;

        Double gastoCO2 = null;

        Optional<InformacaoMeioTransporte> informacaoMeioTransporte = repository.findById(cnh);

        if (Objects.equals("gasolina", informacaoMeioTransporte.get().getTipoCombustivel())) {
            gastoCO2 = informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() * gasolina;
        }

        if (Objects.equals("etanol", informacaoMeioTransporte.get().getTipoCombustivel())) {
            gastoCO2 = informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() * etanol;
        }

        if (Objects.equals("diesel", informacaoMeioTransporte.get().getTipoCombustivel())) {
            gastoCO2 = informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() * diesel;
        }

        if (Objects.equals("eletricidade", informacaoMeioTransporte.get().getTipoCombustivel())) {
            var gastoCO2Gasolina = informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() * gasolina;

            gastoCO2 = gastoCO2Gasolina / 2.0;
        }

        var gastoMensal = gastoCO2 * 30;
        var gastoAnual = gastoMensal * 12;

        var response = ImpactoAmbiental.builder()
                .emissaoDiariaCO2(gastoCO2)
                .emissaoMensalCO2(gastoMensal)
                .emissaoAnualCO2(gastoAnual)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @GetMapping("/{cnh}/sugestoes")
//    public ResponseEntity<InformacaoMeioTransporte> consultarSugestoes(@PathVariable("cnh") String cnh) {
//        validaCNH(cnh);
//
//        if (!repository.existsById(cnh)) {
//            throw new IllegalArgumentException("A CNH informada não existe.");
//        }
//
//        if ()
//
//
//        // Implementar a lógica para sugerir como o usuário pode melhorar seu transporte para reduzir os impactos no meio ambiente e retorná-los.
//        return ResponseEntity.status(HttpStatus.OK).body(retornoSugestoes).build();
//    }

    private void validaCNH(String cnh) {
        if (cnh.length() != 11) {
            throw new DuplicateKeyException("A CNH informada está fora do padrão.");
        }
    }

    private void validaEntrada(InformacaoMeioTransporte request) {
        if (Objects.isNull(request.getIdTipoTransporte())) {
            throw new DuplicateKeyException("O campo 'id_tipo_transporte' é obrigatório.");
        }

        if (Objects.isNull(request.getTipoCombustivel())) {
            throw new DuplicateKeyException("O campo 'tipo_combustivel' é obrigatório.");
        }

        if (Objects.isNull(request.getDistanciaDiariaPercorrida())) {
            throw new DuplicateKeyException("O campo 'distancia_diaria_percorrida' é obrigatório.");
        }

        if (Objects.isNull(request.getConsumoLitro())) {
            throw new DuplicateKeyException("O campo 'consumo_litro' é obrigatório.");
        }
    }

}

