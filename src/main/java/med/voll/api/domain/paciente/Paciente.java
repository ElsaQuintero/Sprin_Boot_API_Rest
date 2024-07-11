package med.voll.api.domain.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Entity(name = "Paciente")
@Table(name = "pacientes")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String documentoIdentidad;
    private String telefono;
    private Boolean activo;

    @Embedded
    private Direccion direccion;

    public Paciente(DatosRegistroPaciente datos) {
        this.activo = true;
        this.nombre = datos.nombre();
        this.email = datos.email();
        this.telefono = datos.telefono();
        this.documentoIdentidad = datos.documentoIdentidad();
        this.direccion = new Direccion(datos.direccion());
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public Long getId() {
        return id;
    }

    public String getTelefono() {
        return telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void actualizarInformacion(DatosActualizarPaciente datosActualizarPaciente) {
        if (datosActualizarPaciente.nombre() != null)
            this.nombre = datosActualizarPaciente.nombre();

        if (datosActualizarPaciente.telefono() != null)
            this.telefono = datosActualizarPaciente.telefono();

        if (datosActualizarPaciente.direccion() != null)
            direccion.actualizarDatos(datosActualizarPaciente.direccion());
    }

    public void inactivar() {
        this.activo = false;
    }
}
