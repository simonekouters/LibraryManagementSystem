package com.simonekouters.librarymanagementsystem.author;

import com.simonekouters.librarymanagementsystem.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
