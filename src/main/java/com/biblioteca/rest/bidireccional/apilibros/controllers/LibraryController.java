package com.biblioteca.rest.bidireccional.apilibros.controllers;

import com.biblioteca.rest.bidireccional.apilibros.entity.Library;
import com.biblioteca.rest.bidireccional.apilibros.repository.LibraryRepository;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
//INDICAMOS LA URL DE LA API DESDE ACÁ
@RequestMapping("api/library")
public class LibraryController {

    //INJECTAMOS LA DEPENDENCIA DE LA CAPA DE DATOS.
    @Autowired
    private LibraryRepository libraryRepository;

    @GetMapping
    //SE BUSCAR RETORNAR EL LISTADO DE LIBRERIAS HACIENDO USO DE PAGINACION DE SER NECESARIO
    public ResponseEntity<Page<Library>> getAllLibraries(Pageable p){

        return ResponseEntity.ok(libraryRepository.findAll(p));
    }


    //METODO GET, SE AÑADE ID A LA URL
    @GetMapping("/{id}")
    public ResponseEntity<Library> getLibraryById(@PathVariable Integer id){

        //CON UN OPTIONAL PODEMOS OBTENER EL RESULTADO DE LA PETICION, Y DEFINIR SI SE EJECUTO O NO.
        Optional<Library> libraryFound = libraryRepository.findById(id);

        //METODO QUE COMPRUEBA SI LA PETICION TUVO UN RESULTADO EXISTENTE
        if(libraryFound.isPresent()){

            //EN CASO DE POSITIVO, RETORNAR UN ESTADO OK + EL OBJETO LIBRARY
            return ResponseEntity.ok(libraryFound.get());
        }

        //EN CASO NEGATIVO, REGRESAR QUE LA ENTIDAD NO SE PUEDE PROCESAR.
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping
    public ResponseEntity<Library> addLibrary(@Valid @RequestBody Library library){

        Library librarySaved = libraryRepository.save(library);

        //CONSTRUIMOS UNA URI (UN ENLACE NUEVO) A PARTIR DEL YA EXISTENTE AGREGANDOLE EL ID DE LA LIBRERIA NUEVA
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(librarySaved.getIdLibrary()).toUri();

        return ResponseEntity.created(uri).body(librarySaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> updateLibrary(@PathVariable Integer id,@Valid @RequestBody Library library){

        Optional<Library> libraryFound = libraryRepository.findById(id);

        if(!libraryFound.isPresent()){

            return ResponseEntity.unprocessableEntity().build();
        }

        library.setIdLibrary(libraryFound.get().getIdLibrary());
        libraryRepository.save(library);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Library> deleteLibrary(@PathVariable Integer id){

        Optional<Library> libraryFound = libraryRepository.findById(id);

        if(libraryFound.isPresent()){

            libraryRepository.delete(libraryFound.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.unprocessableEntity().build();
    }


}
