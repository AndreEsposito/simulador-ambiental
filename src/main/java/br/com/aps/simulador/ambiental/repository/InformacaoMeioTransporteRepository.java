package br.com.aps.simulador.ambiental.repository;

import br.com.aps.simulador.ambiental.model.MeioTransporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeioAmbienteRepository extends JpaRepository<MeioTransporte, Long> {
}
