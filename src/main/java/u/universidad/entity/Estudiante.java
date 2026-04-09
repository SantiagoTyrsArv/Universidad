package u.universidad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import u.universidad.domain.Persona;
import u.universidad.interfaces.Autenticable;
import u.universidad.interfaces.Notificable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entidad que representa a un Estudiante del sistema universitario.
 * Hereda los datos personales de {@link Persona} e implementa las interfaces
 * {@link Notificable} y {@link Autenticable}, definidas en la Capa de Interfaces.
 *
 * Principio S: solo gestiona datos y comportamientos de un Estudiante.
 * Principio L: puede sustituir a Persona en cualquier contrato que lo requiera.
 */
@Entity
@Table(name = "estudiantes")
@Getter
@Setter
public class Estudiante extends Persona implements Notificable, Autenticable {

    private static final Logger log = LoggerFactory.getLogger(Estudiante.class);

    /**
     * Código único del estudiante en el sistema (ej. "2024-001").
     * Sirve como identificador académico.
     */
    @Column(nullable = false, unique = true)
    private String codigo;

    /**
     * Constructor requerido por JPA.
     */
    public Estudiante() {
        super();
    }

    /**
     * Constructor principal, tal como define el diagrama.
     *
     * @param nombre nombre completo del estudiante
     * @param correo correo electrónico institucional
     * @param codigo código único del estudiante
     */
    public Estudiante(String nombre, String correo, String codigo) {
        super(nombre, correo);
        this.codigo = codigo;
    }

    // =====================================================
    // Implementación de Notificable
    // =====================================================

    /**
     * Envía una notificación al estudiante a través de su correo institucional.
     *
     * @param mensaje contenido de la notificación
     */
    @Override
    public void enviarNotificacion(String mensaje) {
        log.info("[NOTIFICACIÓN → ESTUDIANTE] Para {} ({}) <{}> → {}",
                getNombre(), codigo, getCorreo(), mensaje);
    }

    // =====================================================
    // Implementación de Autenticable
    // =====================================================

    /**
     * Verifica las credenciales del estudiante.
     * La lógica compara el usuario con el correo institucional y la contraseña con el código del estudiante.
     * (En producción esta lógica estaría centralizada en un servicio de autenticación.)
     *
     * @param usuario  nombre de usuario (correo institucional)
     * @param password contraseña del estudiante (se valida contra el código)
     * @return true si las credenciales coinciden, false en caso contrario
     */
    @Override
    public boolean login(String usuario, String password) {
        boolean credencialesValidas = getCorreo().equals(usuario) && codigo.equals(password);
        if (credencialesValidas) {
            log.info("[LOGIN] Estudiante '{}' autenticado correctamente.", getNombre());
        } else {
            log.warn("[LOGIN] Intento de acceso fallido para el usuario '{}'.", usuario);
        }
        return credencialesValidas;
    }
}
