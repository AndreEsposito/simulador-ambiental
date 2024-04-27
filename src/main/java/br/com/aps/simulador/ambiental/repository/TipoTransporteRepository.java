package br.com.aps.simulador.ambiental.repository;

import br.com.aps.simulador.ambiental.model.TipoTransporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoTransporteRepository extends JpaRepository<TipoTransporte, Long> {
}
