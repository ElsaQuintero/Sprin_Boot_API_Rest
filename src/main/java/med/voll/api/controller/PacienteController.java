package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.DatosRespuestaMedico;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaPaciente> registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente,
                                                                    UriComponentsBuilder uriBuilder) {
        Paciente paciente = repository.save(new Paciente(datosRegistroPaciente));
        DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getDocumentoIdentidad(),
                new DatosDireccion(
                        paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getComplemento()));
        repository.save(paciente);

        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(datosRespuestaPaciente);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listadoPacientes(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion){
//        var page = repository.findAllByActivoTrue(paginacion).map(DatosListadoPaciente::new);
        return ResponseEntity.ok(repository.findAllByActivoTrue(paginacion).map(DatosListadoPaciente::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente) {
        Paciente paciente = repository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarInformacion(datosActualizarPaciente);
        return ResponseEntity.ok(new DatosRespuestaMedico(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getDocumentoIdentidad(),
                new DatosDireccion(
                        paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getComplemento())
        ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id) {
        Paciente paciente = repository.getReferenceById(id);
        paciente.inactivar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retorneDatosPaciente(@PathVariable Long id) {
        Paciente paciente = repository.getReferenceById(id);
        var datosPaciente = new DatosRespuestaMedico(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getDocumentoIdentidad(),
                new DatosDireccion(
                        paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getComplemento())
        );
        return ResponseEntity.ok(datosPaciente);
    }
}
