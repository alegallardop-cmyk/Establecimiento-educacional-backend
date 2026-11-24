package cl.duoc.establecimiento_educacional.exception;

// Se lanza cuando una operación no cumple una regla del negocio
// (por ejemplo, editar una bitácora cerrada). Se responde con 409 Conflict.
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
