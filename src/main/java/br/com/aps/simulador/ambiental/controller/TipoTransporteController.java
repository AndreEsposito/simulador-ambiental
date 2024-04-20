package br.com.aps.simulador.ambiental.controller;

import br.com.aps.simulador.ambiental.model.TipoTransporte;
import br.com.aps.simulador.ambiental.repository.TipoTransporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tipos-transportes")
public class TipoTransporteController {
    private TipoTransporte tipoTransporte;
    private TipoTransporteRepository repository;

    @PostMapping
    public ResponseEntity<TipoTransporte> cadastrarVeiculos(@RequestBody TipoTransporte request) {
        validaEntrada(request);

        if (repository.existsById(request.getIdTipoTransporte())) {
            throw new DuplicateKeyException("O 'id_tipo_transporte' informado já existe no banco.");
        }

        repository.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

//    @GetMapping
//    public ResponseEntity<ListaTipoTransporte> consultarVeiculos() {
//        var transportes = repository.findAll();
//
//        // TODO: mapear os atributos retornados em lista para a classe ListaTipoTransporte
//        return ResponseEntity.status(HttpStatus.OK).body(transportes);
//    }

    private void validaEntrada(TipoTransporte request) {
        if (Objects.isNull(request.getIdTipoTransporte())) {
            throw new IllegalArgumentException("O campo 'id_tipo_transporte' é obrigatório.");
        }
        if (Objects.isNull(request.getDescricaoTipoTransporte())) {
            throw new IllegalArgumentException("O campo 'descricao_tipo_transporte' é obrigatório.");
        }
    }

}
