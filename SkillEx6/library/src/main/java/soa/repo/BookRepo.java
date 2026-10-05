package soa.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import soa.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
	
	boolean existsByIsbn(String isbn);
}
