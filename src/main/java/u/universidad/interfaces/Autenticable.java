package u.universidad.interfaces;

/**
 * Interface que define el contrato para entidades que pueden
 * autenticarse en el sistema universitario.
 * Principio I (Interface Segregation): separada de otras responsabilidades,
 * la implementan Estudiante y Profesor (no Administrativo).
 */
public interface Autenticable {

    /**
     * Verifica las credenciales de acceso al sistema.
     * @param usuario nombre de usuario (normalmente el correo institucional)
     * @param password contraseña del usuario
     * @return true si las credenciales son correctas, false en caso contrario
     */
    boolean login(String usuario, String password);
}
