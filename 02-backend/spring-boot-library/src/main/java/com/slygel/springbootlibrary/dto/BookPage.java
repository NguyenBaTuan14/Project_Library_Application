package com.slygel.springbootlibrary.dto;

import com.slygel.springbootlibrary.entity.Book;
import lombok.Data;
import java.util.List;

@Data
public class BookPage {
    private List<Book> books;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public BookPage(List<Book> books, int pageNumber, int pageSize, long totalElements) {
        this.books = books;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }

}
