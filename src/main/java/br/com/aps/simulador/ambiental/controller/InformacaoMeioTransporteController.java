package br.com.aps.simulador.ambiental.controller;

import br.com.aps.simulador.ambiental.model.InformacaoMeioTransporte;
import br.com.aps.simulador.ambiental.repository.InformacaoMeioTransporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.regex.Pattern;

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
    public ResponseEntity<InformacaoMeioTransporte> calcularImpactoMeioTransporte(@PathVariable("cnh") String cnh) {
        validaCNH(cnh);

        if (!repository.existsById(cnh)) {
            throw new IllegalArgumentException("A CNH informada não existe.");
        }

        // Implementar a lógica para calcular o impacto de co2 a partir das informações obtidas do endpoint acima e retorná-los.

        return ResponseEntity.status(HttpStatus.OK).body(retornoCalculoImpactos).build();
    }

    @GetMapping("/{cnh}/sugestoes")
    public ResponseEntity<InformacaoMeioTransporte> consultarSugestoes(@PathVariable("cnh") String cnh) {
        // Implementar a lógica para sugerir como o usuário pode melhorar seu transporte para reduzir os impactos no meio ambiente e retorná-los.
        return ResponseEntity.status(HttpStatus.OK).body(retornoSugestoes).build();
    }

    private void validaCNH(String cnh) {
        if (cnh.length() != 11) {
            throw new DuplicateKeyException("A CNH informada está fora do padrão.");
        }
    }

    private void validaEntrada(InformacaoMeioTransporte request) {
        if (Objects.isNull(request.getId_tipo_transporte())) {
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

