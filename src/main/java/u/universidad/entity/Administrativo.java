package u.universidad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import u.universidad.domain.Persona;
import u.universidad.interfaces.Aprobador;
import u.universidad.interfaces.Notificable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entidad que representa a un Administrativo del sistema universitario.
 * Hereda los datos personales de {@link Persona} e implementa las interfaces
 * {@link Aprobador} y {@link Notificable}, definidas en la Capa de Interfaces.
 *
 * Principio S: solo gestiona datos y comportamientos de un Administrativo.
 * Principio L: puede sustituir a Persona en cualquier contrato que lo requiera.
 */
@Entity
@Table(name = "administrativos")
@Getter
@Setter
public class Administrativo extends Persona implements Aprobador, Notificable {

    private static final Logger log = LoggerFactory.getLogger(Administrativo.class);

    /** Área institucional a la que pertenece el administrativo (ej. "Registro", "Bienestar"). */
    @Column(nullable = false)
    private String area;

    /**
     * Constructor requerido por JPA.
     */
    public Administrativo() {
        super();
    }

    /**
     * Constructor principal, tal como define el diagrama.
     *
     * @param nombre nombre completo del administrativo
     * @param correo correo electrónico institucional
     * @param area   área a la que pertenece
     */
    public Administrativo(String nombre, String correo, String area) {
        super(nombre, correo);
        this.area = area;
    }

    // =====================================================
    // Implementación de Aprobador
    // =====================================================

    /**
     * Aprueba una solicitud identificada por su código.
     * Registra en el log la aprobación y notifica al sistema.
     *
     * @param codigoSolicitud código de la solicitud a aprobar
     */
    @Override
    public void aprobarSolicitud(String codigoSolicitud) {
        log.info("[APROBACIÓN] El administrativo '{}' del área '{}' aprobó la solicitud con código: {}",
                getNombre(), area, codigoSolicitud);
        enviarNotificacion("Tu solicitud [" + codigoSolicitud + "] ha sido APROBADA por " + getNombre());
    }

    // =====================================================
    // Implementación de Notificable
    // =====================================================

    /**
     * Envía una notificación por correo institucional del administrativo.
     *
     * @param mensaje contenido de la notificación
     */
    @Override
    public void enviarNotificacion(String mensaje) {
        log.info("[NOTIFICACIÓN] Enviada desde {} <{}> → {}",
                getNombre(), getCorreo(), mensaje);
    }
}
