package com.slygel.springbootlibrary.controller;
import com.slygel.springbootlibrary.dto.BookPage;
import com.slygel.springbootlibrary.entity.Book;
import com.slygel.springbootlibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books/search")
    public ResponseEntity<BookPage> getBookDetails(@RequestParam(value = "title", required = false) String title,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size ) {
        BookPage book = bookService.getBookDetails(title, page, size);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/books/search/byCategory")
    public ResponseEntity<BookPage> getBookDetailsByCategory(@RequestParam(value = "category", required = false) String category,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size ) {
        BookPage book = bookService.getBookDetailsByCategory(category, page, size);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getAllBooks(@RequestParam(value = "title", required = false) String param, Pageable pageable){
        Page<Book> books = bookService.getAllBooks(param,pageable);
        return ResponseEntity.ok().body(books);
    }
}
