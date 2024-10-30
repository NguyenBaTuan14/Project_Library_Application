package com.slygel.springbootlibrary.service;

import com.slygel.springbootlibrary.dto.BookPage;
import com.slygel.springbootlibrary.entity.Book;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String BASE_URI = "http://www.example.org/bookontology#";
    private static final String ONTOLOGY_FILE = "D:/converted_books.owl";

    private Model model;

    public BookService() {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(ONTOLOGY_FILE);
    }

    public BookPage getBookDetails(String title, int page, int size) {
        String queryString = "PREFIX ex: <http://www.example.org/reactlibrary#> " +
                "SELECT ?book ?id ?title ?author ?description ?copies ?copies_available ?category ?img " +
                "WHERE { " +
                "  ?book ex:title ?title . " +
                "  OPTIONAL { ?book ex:id ?id . } " +
                "  OPTIONAL { ?book ex:author ?author . } " +
                "  OPTIONAL { ?book ex:description ?description . } " +
                "  OPTIONAL { ?book ex:copies ?copies . } " +
                "  OPTIONAL { ?book ex:copies_available ?copies_available . } " +
                "  OPTIONAL { ?book ex:category ?category . } " +
                "  OPTIONAL { ?book ex:img ?img . } " +
                "  FILTER (regex(?title, \"" + title + "\", \"i\")) " +
                "} " +
                "LIMIT " + size + " " +
                "OFFSET " + (page * size);

        // First query to get paginated data
        QueryExecution queryExecution = QueryExecutionFactory.create(QueryFactory.create(queryString), model);
        ResultSet resultSet = queryExecution.execSelect();

        List<Book> books = new ArrayList<>();
        while (resultSet.hasNext()) {
            QuerySolution solution = resultSet.nextSolution();
            Resource bookResource = solution.getResource("book");

            Long id = null;
            if (solution.contains("id")) {
                id = solution.getLiteral("id").getLong();
            }

            String bookTitle = solution.getLiteral("title").getString();
            String author = solution.contains("author") ? solution.getLiteral("author").getString() : null;
            String description = solution.contains("description") ? solution.getLiteral("description").getString() : null;
            int copies = solution.contains("copies") ? solution.getLiteral("copies").getInt() : 0;
            int copiesAvailable = solution.contains("copies_available") ? solution.getLiteral("copies_available").getInt() : 0;
            String category = solution.contains("category") ? solution.getLiteral("category").getString() : null;
            String img = solution.contains("img") ? solution.getLiteral("img").getString() : null;

            books.add(new Book(id, bookTitle, author, description, copies, copiesAvailable, category, img));
        }
        queryExecution.close();

        // Get total count query
        String countQueryString = "PREFIX ex: <http://www.example.org/reactlibrary#> " +
                "SELECT (COUNT(?book) AS ?count) " +
                "WHERE { " +
                "  ?book ex:title ?title . " +
                "  FILTER (regex(?title, \"" + title + "\", \"i\")) " +
                "}";

        QueryExecution countQueryExecution = QueryExecutionFactory.create(QueryFactory.create(countQueryString), model);
        ResultSet countResultSet = countQueryExecution.execSelect();
        long totalElements = 0;
        if (countResultSet.hasNext()) {
            QuerySolution countSolution = countResultSet.nextSolution();
            totalElements = countSolution.getLiteral("count").getLong();
        }
        countQueryExecution.close();

        return new BookPage(books, page, size, totalElements);
    }

    public BookPage getBookDetailsByCategory(String category, int page, int size) {
        String queryString = "PREFIX ex: <http://www.example.org/reactlibrary#> " +
                "SELECT ?book ?id ?title ?author ?description ?copies ?copies_available ?category ?img " +
                "WHERE { " +
                "  ?book ex:title ?title . " +
                "  OPTIONAL { ?book ex:id ?id . } " +
                "  OPTIONAL { ?book ex:author ?author . } " +
                "  OPTIONAL { ?book ex:description ?description . } " +
                "  OPTIONAL { ?book ex:copies ?copies . } " +
                "  OPTIONAL { ?book ex:copies_available ?copies_available . } " +
                "  OPTIONAL { ?book ex:category ?category . } " +
                "  OPTIONAL { ?book ex:img ?img . } " +
                "  FILTER (regex(?category, \"" + category + "\", \"i\")) " +
                "} " +
                "LIMIT " + size + " " +
                "OFFSET " + (page * size);

        // First query to get paginated data
        QueryExecution queryExecution = QueryExecutionFactory.create(QueryFactory.create(queryString), model);
        ResultSet resultSet = queryExecution.execSelect();

        List<Book> books = new ArrayList<>();
        while (resultSet.hasNext()) {
            QuerySolution solution = resultSet.nextSolution();
            Resource bookResource = solution.getResource("book");

            Long id = null;
            if (solution.contains("id")) {
                id = solution.getLiteral("id").getLong();
            }

            String bookTitle = solution.getLiteral("title").getString();
            String author = solution.contains("author") ? solution.getLiteral("author").getString() : null;
            String description = solution.contains("description") ? solution.getLiteral("description").getString() : null;
            int copies = solution.contains("copies") ? solution.getLiteral("copies").getInt() : 0;
            int copiesAvailable = solution.contains("copies_available") ? solution.getLiteral("copies_available").getInt() : 0;
            String bookCategory = solution.contains("category") ? solution.getLiteral("category").getString() : null;
            String img = solution.contains("img") ? solution.getLiteral("img").getString() : null;

            books.add(new Book(id, bookTitle, author, description, copies, copiesAvailable, bookCategory, img));
        }
        queryExecution.close();

        // Get total count query
        String countQueryString = "PREFIX ex: <http://www.example.org/reactlibrary#> " +
                "SELECT (COUNT(?book) AS ?count) " +
                "WHERE { " +
                "  ?book ex:category ?category . " +
                "  FILTER (regex(?category, \"" + category + "\", \"i\")) " +
                "}";

        QueryExecution countQueryExecution = QueryExecutionFactory.create(QueryFactory.create(countQueryString), model);
        ResultSet countResultSet = countQueryExecution.execSelect();
        long totalElements = 0;
        if (countResultSet.hasNext()) {
            QuerySolution countSolution = countResultSet.nextSolution();
            totalElements = countSolution.getLiteral("count").getLong();
        }
        countQueryExecution.close();

        return new BookPage(books, page, size, totalElements);
    }


    public int countTotalBook(String param) {
        String countSql = "SELECT COUNT(*) FROM book WHERE lower(title) LIKE ?";
        String searchParam = param != null ? "%" + param.toLowerCase() + "%" : "%%";
        return jdbcTemplate.queryForObject(countSql, new Object[]{searchParam}, Integer.class);
    }

    public Page<Book> getAllBooks(String param, Pageable pageable) {
        String sql = "SELECT * FROM book WHERE lower(title) LIKE ? LIMIT ? OFFSET ?";
        String searchParam = param != null ? "%" + param.toLowerCase() + "%" : "%%";
        List<Book> books = jdbcTemplate.query(
                sql,
                new Object[]{
                        searchParam,
                        pageable.getPageSize(),
                        pageable.getPageNumber() * pageable.getPageSize()
                },
                (rs, rowNum) ->
                        new Book(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("description"),
                                rs.getInt("copies"),
                                rs.getInt("copies_available"),
                                rs.getString("category"),
                                rs.getString("img")
                        )
        );
        int totalRecords = countTotalBook(param);
        return new PageImpl<>(books, pageable, totalRecords);
    }
}
