package u.universidad.interfaces;

import u.universidad.entity.Estudiante;

/**
 * Interface que define el contrato para entidades que pueden
 * evaluar el desempeño de un estudiante.
 * Principio I (Interface Segregation): separada de otras responsabilidades,
 * solo la implementa Profesor.
 */
public interface Evaluador {

    /**
     * Evalúa a un estudiante asignándole una nota.
     * @param estudiante el estudiante a evaluar
     * @param nota       la nota obtenida (valor entre 0.0 y 5.0)
     */
    void evaluar(Estudiante estudiante, double nota);
}
