package u.universidad.interfaces;

/**
 * Interface que define el contrato para entidades que pueden
 * aprobar solicitudes dentro del sistema universitario.
 * Principio I (Interface Segregation): es una interfaz específica
 * y delgada, solo para quien tenga esta responsabilidad.
 */
public interface Aprobador {

    /**
     * Aprueba una solicitud identificada por su código.
     * @param codigoSolicitud código único de la solicitud a aprobar
     */
    void aprobarSolicitud(String codigoSolicitud);
}
