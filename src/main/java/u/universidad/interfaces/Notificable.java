package u.universidad.interfaces;

/**
 * Interface que define el contrato para entidades que pueden
 * enviar notificaciones dentro del sistema universitario.
 * Principio I (Interface Segregation): separada de otras responsabilidades,
 * la implementan tanto Administrativo, Estudiante como Profesor.
 */
public interface Notificable {

    /**
     * Envía una notificación con el mensaje indicado.
     * @param mensaje contenido del mensaje a enviar
     */
    void enviarNotificacion(String mensaje);
}
