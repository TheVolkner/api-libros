package com.biblioteca.rest.bidireccional.apilibros.controllers;

import com.biblioteca.rest.bidireccional.apilibros.entity.Book;
import com.biblioteca.rest.bidireccional.apilibros.entity.Library;
import com.biblioteca.rest.bidireccional.apilibros.repository.BookRepository;
import com.biblioteca.rest.bidireccional.apilibros.repository.LibraryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("api/book")
public class BookController {

    //SE INJECTAN LAS DOS DEPENDENCIAS YA QUE DESDE BOOK AL SER EL PORTADOR DE LA LLAVE PRIMARIA
    //SE EDITARÁ TAMBIEN A LIBRARY.
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(Pageable p){

        return ResponseEntity.ok(bookRepository.findAll(p));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id){

        Optional<Book> bookOptional = bookRepository.findById(id);

        if(bookOptional.isPresent()){

            return ResponseEntity.ok(bookOptional.get());
        }

        return ResponseEntity.unprocessableEntity().build();
    }



    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book){

        //ANTES DE GUARDAR EL BOOK, VERIFICAMOS QUE LIBRARY EXISTA.
        Optional<Library> libraryOptional = libraryRepository.findById(book.getLibrary().getIdLibrary());

        if(!libraryOptional.isPresent()){

            return ResponseEntity.unprocessableEntity().build();
        }

        book.setLibrary(libraryOptional.get());
        Book bookSaved =bookRepository.save(book);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(bookSaved.getIdBook()).toUri();

        return ResponseEntity.created(uri).body(bookSaved);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> addBook(@PathVariable Integer id,@Valid @RequestBody Book book){

        //ANTES DE GUARDAR EL BOOK, VERIFICAMOS QUE LIBRARY EXISTA.
        Optional<Library> libraryOptional = libraryRepository.findById(book.getLibrary().getIdLibrary());

        //SI LA LIBRERIA EXISTE, PROCEDEMOS AHORA A VALIDAR EL LIBRO
        if(!libraryOptional.isPresent()){

            return ResponseEntity.unprocessableEntity().build();
        }


        Optional<Book> bookOptional = bookRepository.findById(id);

        //SI EL LIBRO EXISTE,PROCEDEMOS A ASIGNARLE LA LIBRERIA RESPECTIVA Y A GUARDARLO EN LA BBDD.
        if(!bookOptional.isPresent()){

            return ResponseEntity.unprocessableEntity().build();
        }

        book.setLibrary(libraryOptional.get());
        book.setIdBook(bookOptional.get().getIdBook());
        Book bookSaved = bookRepository.save(book);

        return ResponseEntity.noContent().build();

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Library> deleteLibrary(@PathVariable Integer id){

        Optional<Book> bookOptional = bookRepository.findById(id);

        if(bookOptional.isPresent()){

            bookRepository.delete(bookOptional.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.unprocessableEntity().build();
    }


}
