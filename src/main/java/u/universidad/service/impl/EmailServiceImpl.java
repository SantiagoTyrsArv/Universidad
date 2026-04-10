package u.universidad.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import u.universidad.exception.NotificacionException;
import u.universidad.service.EmailService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementación concreta del servicio de envío de correos electrónicos.
 * Usa {@link JavaMailSender} de Spring Mail para envíos SMTP reales con formato HTML.
 *
 * El template HTML utiliza el branding institucional de la
 * Universidad Cooperativa de Colombia (UCC):
 * - Color corporativo: #00acc9 (Pantone 3125 C — Azul Diversidad / Aguamarina)
 * - Tipografía segura para email: Arial, sans-serif
 *
 * Principio S: su única responsabilidad es construir y enviar correos HTML.
 * Principio D: implementa la interfaz {@link EmailService}.
 * Principio O: el template HTML puede extenderse sin modificar la lógica de negocio.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    // ─── Colores corporativos UCC (Pantone 3125 C) ────────────────────────────
    private static final String COLOR_PRIMARIO   = "#00acc9";
    private static final String COLOR_PRIMARIO_D = "#007a90";
    private static final String COLOR_TEXTO      = "#4a4a4a";
    private static final String COLOR_FONDO      = "#f4f7f9";
    private static final String COLOR_BLANCO     = "#ffffff";
    private static final String COLOR_EXITO      = "#2e7d32";
    private static final String COLOR_GRIS_BORDE = "#e0e0e0";
    private static final String COLOR_FOOTER_TXT = "#a0a0a0";

    /**
     * {@inheritDoc}
     *
     * Construye un MimeMessage con contenido HTML y lo envía mediante SMTP.
     *
     * @throws NotificacionException si el correo es rechazado por el servidor SMTP
     *         o si la dirección del destinatario es inválida.
     */
    @Override
    public void enviarCorreoHtml(String destinatario,
                                 String asunto,
                                 String titulo,
                                 String cuerpoHtml,
                                 String piePagina) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(remitente, "Sistema UCC — Universidad Cooperativa de Colombia");
            helper.setTo(destinatario);
            helper.setSubject("[UCC] " + asunto);
            helper.setText(construirHtml(titulo, cuerpoHtml, piePagina), true);

            mailSender.send(mensaje);
            log.info("[EMAIL ✓] Correo HTML enviado exitosamente a: {}", destinatario);

        } catch (MailException e) {
            log.error("[EMAIL ✗] Fallo al enviar correo a '{}': {}", destinatario, e.getMessage());
            throw new NotificacionException(
                    "No fue posible enviar el correo a: " + destinatario +
                    ". Verifica que la dirección sea válida. Detalle: " + e.getMessage(), e);
        } catch (MessagingException e) {
            log.error("[EMAIL ✗] Error al construir el mensaje para '{}': {}", destinatario, e.getMessage());
            throw new NotificacionException(
                    "Error al construir el correo para: " + destinatario +
                    ". Detalle: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("[EMAIL ✗] Error inesperado enviando correo a '{}': {}", destinatario, e.getMessage());
            throw new NotificacionException(
                    "Error inesperado al notificar a: " + destinatario, e);
        }
    }

    // ─── Constructor del template HTML institucional UCC ──────────────────────

    /**
     * Construye el HTML completo del correo con el branding de la UCC.
     * Usa estilos inline para máxima compatibilidad con clientes de correo.
     *
     * @param titulo     título destacado en el cuerpo
     * @param cuerpo     contenido principal del correo
     * @param piePagina  texto al pie del mensaje
     * @return String con el HTML completo listo para enviar
     */
    private String construirHtml(String titulo, String cuerpo, String piePagina) {
        String fechaHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%1$s</title>
                </head>
                <body style="margin:0; padding:0; background-color:%2$s; font-family: Arial, Helvetica, sans-serif;">

                    <!-- Contenedor principal -->
                    <table width="100%%" cellpadding="0" cellspacing="0" border="0"
                           style="background-color:%2$s; padding: 30px 0;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" border="0"
                                       style="max-width:600px; width:100%%;">

                                    <!-- ── HEADER UCC ── -->
                                    <tr>
                                        <td style="background-color:%3$s; border-radius:8px 8px 0 0;
                                                   padding: 28px 36px;">
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0">
                                                <tr>
                                                    <td>
                                                        <!-- Logo textual UCC -->
                                                        <div style="display:inline-block;
                                                                    background-color:%4$s;
                                                                    color:%5$s;
                                                                    font-size:22px;
                                                                    font-weight:700;
                                                                    padding:8px 14px;
                                                                    border-radius:5px;
                                                                    letter-spacing:2px;
                                                                    margin-bottom:10px;">
                                                            UCC
                                                        </div>
                                                        <br>
                                                        <span style="color:%5$s; font-size:15px;
                                                                     font-weight:400; letter-spacing:0.5px;">
                                                            Universidad Cooperativa de Colombia
                                                        </span>
                                                    </td>
                                                    <td align="right" style="vertical-align:middle;">
                                                        <span style="color:rgba(255,255,255,0.7);
                                                                     font-size:12px;">
                                                            %6$s
                                                        </span>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>

                                    <!-- ── LÍNEA DECORATIVA ── -->
                                    <tr>
                                        <td style="height:4px; background: linear-gradient(to right, %3$s, %4$s);"></td>
                                    </tr>

                                    <!-- ── CUERPO ── -->
                                    <tr>
                                        <td style="background-color:%5$s; padding:36px 36px 28px 36px;
                                                   border-left:1px solid %7$s; border-right:1px solid %7$s;">

                                            <!-- Título -->
                                            <h2 style="color:%3$s; font-size:20px; font-weight:700;
                                                       margin:0 0 20px 0; padding-bottom:14px;
                                                       border-bottom:2px solid %3$s;">
                                                %1$s
                                            </h2>

                                            <!-- Separador -->
                                            <div style="height:1px; background-color:%7$s; margin-bottom:20px;"></div>

                                            <!-- Contenido del mensaje -->
                                            <div style="color:%8$s; font-size:15px; line-height:1.7;">
                                                %9$s
                                            </div>

                                            <!-- Pie del mensaje -->
                                            <div style="margin-top:28px; padding:16px 20px;
                                                        background-color:%2$s; border-radius:6px;
                                                        border-left:4px solid %3$s;
                                                        color:%8$s; font-size:14px; line-height:1.6;">
                                                %10$s
                                            </div>

                                        </td>
                                    </tr>

                                    <!-- ── FOOTER ── -->
                                    <tr>
                                        <td style="background-color:%4$s; border-radius:0 0 8px 8px;
                                                   padding:20px 36px; text-align:center;">
                                            <p style="color:%5$s; font-size:13px; margin:0 0 6px 0;
                                                      font-weight:600; letter-spacing:0.5px;">
                                                Sistema de Gestión Universitaria · UCC
                                            </p>
                                            <p style="color:rgba(255,255,255,0.75); font-size:11px;
                                                      margin:0 0 8px 0;">
                                                Este es un correo automático generado por el sistema.
                                                Por favor, no respondas a este mensaje.
                                            </p>
                                            <p style="color:rgba(255,255,255,0.6); font-size:11px; margin:0;">
                                                © 2026 Universidad Cooperativa de Colombia ·
                                                <a href="https://www.ucc.edu.co"
                                                   style="color:rgba(255,255,255,0.8); text-decoration:none;">
                                                    www.ucc.edu.co
                                                </a>
                                            </p>
                                        </td>
                                    </tr>

                                </table>
                            </td>
                        </tr>
                    </table>

                </body>
                </html>
                """.formatted(
                        titulo,           // %1$ - título / asunto HTML
                        COLOR_FONDO,      // %2$ - fondo body
                        COLOR_PRIMARIO,   // %3$ - aguamarina (header, títulos)
                        COLOR_PRIMARIO_D, // %4$ - aguamarina oscuro (logo, footer)
                        COLOR_BLANCO,     // %5$ - blanco
                        fechaHora,        // %6$ - fecha y hora del envío
                        COLOR_GRIS_BORDE, // %7$ - bordes grises
                        COLOR_TEXTO,      // %8$ - texto gris corporativo
                        cuerpo,           // %9$ - cuerpo del mensaje
                        piePagina         // %10$ - pie del mensaje
        );
    }
}
