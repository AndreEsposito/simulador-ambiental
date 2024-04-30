package br.com.aps.simulador.ambiental.controller;

import br.com.aps.simulador.ambiental.model.ImpactoAmbiental;
import br.com.aps.simulador.ambiental.model.InformacaoMeioTransporte;
import br.com.aps.simulador.ambiental.model.Sugestao;
import br.com.aps.simulador.ambiental.repository.InformacaoMeioTransporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
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
            throw new DuplicateKeyException("A CNH informada não existe.");
        }

        var gasolina = 0.75 * 3.7 * 0.82;
        var etanol = 0.782 * 2.0 * 0.95;
        var diesel = 0.85 * 12.0 * 1.22;

        Double gastoDiarioCO2 = null;

        Optional<InformacaoMeioTransporte> informacaoMeioTransporte = repository.findById(cnh);

        if (Objects.equals("gasolina", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
            gastoDiarioCO2 = (informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() /
                    informacaoMeioTransporte.get().getConsumoLitro()) * gasolina;
        }

        if (Objects.equals("etanol", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
            gastoDiarioCO2 = (informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() /
                    informacaoMeioTransporte.get().getConsumoLitro()) * etanol;
        }

        if (Objects.equals("diesel", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
            gastoDiarioCO2 = (informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() /
                        informacaoMeioTransporte.get().getConsumoLitro()) * diesel;
        }

        if (Objects.equals("eletricidade", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
            var gastoCO2Gasolina = (informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() /
                    informacaoMeioTransporte.get().getConsumoLitro()) * gasolina;

            gastoDiarioCO2 = gastoCO2Gasolina / 2.0;
        }

        var gastoMensalCO2 = gastoDiarioCO2 * 30;
        var gastoAnualCO2 = gastoMensalCO2 * 12;

        DecimalFormat padraoFormatado = new DecimalFormat("#.##");

        var gastoDiarioCO2Formatado = Double.parseDouble(padraoFormatado.format(gastoDiarioCO2));
        var gastoMensalCO2Formatado = Double.parseDouble(padraoFormatado.format(gastoMensalCO2));
        var gastoAnualCO2Formatado = Double.parseDouble(padraoFormatado.format(gastoAnualCO2));

        var response = ImpactoAmbiental.builder()
                .emissaoDiariaCO2(gastoDiarioCO2Formatado)
                .emissaoMensalCO2(gastoMensalCO2Formatado)
                .emissaoAnualCO2(gastoAnualCO2Formatado)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{cnh}/sugestoes")
    public ResponseEntity<Sugestao> consultarSugestoes(@PathVariable("cnh") String cnh) {
        validaCNH(cnh);

        if (!repository.existsById(cnh)) {
            throw new DuplicateKeyException("A CNH informada não existe.");
        }

        Optional<InformacaoMeioTransporte> informacaoMeioTransporte = repository.findById(cnh);

        Sugestao sugestao = new Sugestao();

        if (informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() <= 10.00) {
            if (Objects.equals("gasolina", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta, pois além de ser sustentável para o meio ambiente, também faz bem para a saúde.",
                            "Utilize moto, caso tenha, pois é um transporte rápido, Com um custo menor e que prejudica menos o meio ambiente."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em fazer uma caminhada, já que o trajeto é pequeno, pois faz bem para a saúde e para o planeta.",
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 5) {
                    sugestao.setDescricao(Arrays.asList(
                            "Veja se não faz sentido utilizar onibus/metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 6) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em fazer uma caminhada, já que o trajeto é pequeno, pois faz bem para a saúde e para o planeta.",
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("etanol", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta, pois além de ser sustentável para o meio ambiente, também faz bem para a saúde.",
                            "Utilize moto, caso tenha, pois é um transporte rápido, Com um custo menor e que prejudica menos o meio ambiente."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em fazer uma caminhada, já que o trajeto é pequeno, pois faz bem para a saúde e para o planeta.",
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 5) {
                    sugestao.setDescricao(Arrays.asList(
                            "Veja se não faz sentido utilizar onibus/metro, é um meio de transporte público mais barato e mais sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 6) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em fazer uma caminhada, já que o trajeto é pequeno, pois faz bem para a saúde e para o planeta.",
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("diesel", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 1) {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas sustentáveis."
                    ));
                } else if (idTipoTransporte == 4) {
                    sugestao.setDescricao(Arrays.asList(
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("eletricidade", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta, pois além de ser sustentável para o meio ambiente, também faz bem para a saúde.",
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta, pois além de ser sustentável para o meio ambiente, também faz bem para a saúde.",
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
        } else if (informacaoMeioTransporte.get().getDistanciaDiariaPercorrida() <= 50.00) {
            if (Objects.equals("gasolina", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 5) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 6) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("etanol", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 5) {
                    sugestao.setDescricao(Arrays.asList(
                            "Pense em utilizar uma bicicleta elétrica caso esteja com pressa, pois além de ser rápido é sustentável para o meio ambiente.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 6) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("diesel", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 1) {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                } else if (idTipoTransporte == 4) {
                    sugestao.setDescricao(Arrays.asList(
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("eletricidade", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Recarga com energia renovável como um painel solar ou turbina eólica, Pode encontrar em parques com estacionamento público.",
                            "Carregamento inteligente, que otimiza carga da bateria."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Recarga com energia renovável como um painel solar ou turbina eólica. Pode encontrar em parques com estacionamento público.",
                            "Carregamento inteligente, que otimiza carga da bateria."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
        } else {
            if (Objects.equals("gasolina", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 5) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 6) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("etanol", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 5) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else if (idTipoTransporte == 6) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar metro, é um meio de transporte público barato e sustentável para o meio ambiente."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("diesel", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 1) {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                } else if (idTipoTransporte == 4) {
                    sugestao.setDescricao(Arrays.asList(
                            "Considere optar por um transporte público como um onibus, é uma opção com custo baixo.",
                            "Veja se não faz sentido utilizar trem, é um meio de transporte público barato."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
            if (Objects.equals("eletricidade", informacaoMeioTransporte.get().getTipoCombustivel().toLowerCase())) {
                Long idTipoTransporte = informacaoMeioTransporte.get().getIdTipoTransporte();
                if (idTipoTransporte == 2) {
                    sugestao.setDescricao(Arrays.asList(
                            "Recarga com energia renovável como um painel solar ou turbina eólica. Pode encontrar em parques com estacionamento público.",
                            "Carregamento inteligente, que otimiza carga da bateria."
                    ));
                } else if (idTipoTransporte == 3) {
                    sugestao.setDescricao(Arrays.asList(
                            "Recarga com energia renovável como um painel solar ou turbina eólica. Pode encontrar em parques com estacionamento público.",
                            "Carregamento inteligente, que otimiza carga da bateria."
                    ));
                } else {
                    sugestao.setDescricao(Arrays.asList(
                            "Até o momento não existem alternativas mais sustentáveis."
                    ));
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(sugestao);
    }

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
        else {
            if (!Arrays.asList("gasolina", "etanol", "diesel", "eletricidade").contains(request.getTipoCombustivel().toLowerCase())) {
                throw new IllegalArgumentException("Combustível incorreto. Os combustíveis aceitos são: gasolina, etanol, diesel e eletricidade");
            }

        }

        if (Objects.isNull(request.getDistanciaDiariaPercorrida())) {
            throw new DuplicateKeyException("O campo 'distancia_diaria_percorrida' é obrigatório.");
        }

        if (Objects.isNull(request.getConsumoLitro())) {
            throw new DuplicateKeyException("O campo 'consumo_litro' é obrigatório.");
        }
    }

}

