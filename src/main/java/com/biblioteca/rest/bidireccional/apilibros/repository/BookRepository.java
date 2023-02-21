package com.biblioteca.rest.bidireccional.apilibros.repository;

import com.biblioteca.rest.bidireccional.apilibros.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {
}
