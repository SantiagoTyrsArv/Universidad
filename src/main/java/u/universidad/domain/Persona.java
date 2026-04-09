package u.universidad.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase abstracta que representa a una Persona dentro del sistema universitario.
 * Actúa como clase base (Capa de Dominio) para Administrativo, Estudiante y Profesor.
 *
 * Uso de @MappedSuperclass: los campos nombre y correo se heredan directamente
 * a las tablas de cada subclase, sin crear una tabla intermedia "persona".
 *
 * Principio L (Liskov): cualquier subclase puede sustituir a Persona sin romper el sistema.
 * Principio S (Single Responsibility): solo contiene los datos e identidad de una persona.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Persona {

    /** Identificador único auto-generado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo de la persona. */
    @Column(nullable = false)
    private String nombre;

    /** Correo electrónico institucional de la persona. */
    @Column(nullable = false, unique = true)
    private String correo;

    /**
     * Constructor requerido por JPA (sin argumentos).
     */
    protected Persona() {}

    /**
     * Constructor principal de Persona, tal como define el diagrama.
     *
     * @param nombre nombre completo de la persona
     * @param correo correo electrónico institucional
     */
    public Persona(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }
}
