package u.universidad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import u.universidad.domain.Persona;
import u.universidad.interfaces.Autenticable;
import u.universidad.interfaces.Evaluador;
import u.universidad.interfaces.Notificable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entidad que representa a un Profesor del sistema universitario.
 * Hereda los datos personales de {@link Persona} e implementa las interfaces
 * {@link Notificable}, {@link Autenticable} y {@link Evaluador}, definidas en la Capa de Interfaces.
 *
 * Es la entidad con más contratos implementados, demostrando polimorfismo extenso.
 * Principio S: solo gestiona datos y comportamientos de un Profesor.
 */
@Entity
@Table(name = "profesores")
@Getter
@Setter
public class Profesor extends Persona implements Notificable, Autenticable, Evaluador {

    private static final Logger log = LoggerFactory.getLogger(Profesor.class);

    /** Especialidad académica del profesor (ej. "Ingeniería de Software", "Matemáticas"). */
    @Column(nullable = false)
    private String especialidad;

    /**
     * Constructor requerido por JPA.
     */
    public Profesor() {
        super();
    }

    /**
     * Constructor principal, tal como define el diagrama.
     *
     * @param nombre       nombre completo del profesor
     * @param correo       correo electrónico institucional
     * @param especialidad área académica de especialización
     */
    public Profesor(String nombre, String correo, String especialidad) {
        super(nombre, correo);
        this.especialidad = especialidad;
    }

    // =====================================================
    // Implementación de Notificable
    // =====================================================

    /**
     * Envía una notificación al profesor a través de su correo institucional.
     *
     * @param mensaje contenido de la notificación
     */
    @Override
    public void enviarNotificacion(String mensaje) {
        log.info("[NOTIFICACIÓN → PROFESOR] Para {} ({}) <{}> → {}",
                getNombre(), especialidad, getCorreo(), mensaje);
    }

    // =====================================================
    // Implementación de Autenticable
    // =====================================================

    /**
     * Verifica las credenciales del profesor.
     * Valida el usuario contra el correo y la contraseña contra la especialidad.
     * (En producción esta lógica estaría centralizada en un servicio de autenticación.)
     *
     * @param usuario  nombre de usuario (correo institucional)
     * @param password contraseña del profesor
     * @return true si las credenciales coinciden, false en caso contrario
     */
    @Override
    public boolean login(String usuario, String password) {
        boolean credencialesValidas = getCorreo().equals(usuario) && especialidad.equals(password);
        if (credencialesValidas) {
            log.info("[LOGIN] Profesor '{}' autenticado correctamente.", getNombre());
        } else {
            log.warn("[LOGIN] Intento de acceso fallido para el usuario '{}'.", usuario);
        }
        return credencialesValidas;
    }

    // =====================================================
    // Implementación de Evaluador
    // =====================================================

    /**
     * Evalúa a un estudiante asignándole una nota y enviando una notificación al estudiante.
     * Valida que la nota esté en el rango académico permitido (0.0 - 5.0).
     *
     * @param estudiante el estudiante a evaluar
     * @param nota       la nota obtenida
     * @throws IllegalArgumentException si la nota está fuera del rango 0.0 - 5.0
     */
    @Override
    public void evaluar(Estudiante estudiante, double nota) {
        if (nota < 0.0 || nota > 5.0) {
            throw new IllegalArgumentException(
                    "La nota debe estar entre 0.0 y 5.0. Se recibió: " + nota);
        }
        String resultado = nota >= 3.0 ? "APROBÓ" : "REPROBÓ";
        log.info("[EVALUACIÓN] El profesor '{}' evaluó a '{}' con nota {:.1f} → {}",
                getNombre(), estudiante.getNombre(), nota, resultado);
        estudiante.enviarNotificacion(
                "Has sido evaluado por el Prof. " + getNombre() +
                " en " + especialidad +
                ". Nota: " + nota + " → " + resultado);
    }
}
