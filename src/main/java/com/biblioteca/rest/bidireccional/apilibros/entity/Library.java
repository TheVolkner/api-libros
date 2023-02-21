package com.biblioteca.rest.bidireccional.apilibros.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLibrary;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "library",cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Library library = (Library) o;
        return idLibrary != null && Objects.equals(idLibrary, library.idLibrary);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setBooks(Set<Book> books){

        this.books = books;

        for(Book book: books){

            book.setLibrary(this);
        }
    }
}
