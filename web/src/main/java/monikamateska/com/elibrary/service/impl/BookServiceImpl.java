package monikamateska.com.elibrary.service.impl;

import monikamateska.com.elibrary.model.Author;
import monikamateska.com.elibrary.model.Book;
import monikamateska.com.elibrary.model.Country;
import monikamateska.com.elibrary.model.dto.BookDto;
import monikamateska.com.elibrary.model.enumerations.Category;
import monikamateska.com.elibrary.model.exceptions.AuthorNotFoundException;
import monikamateska.com.elibrary.model.exceptions.BookNotFoundException;
import monikamateska.com.elibrary.repository.AuthorRepository;
import monikamateska.com.elibrary.repository.BookRepository;
import monikamateska.com.elibrary.repository.CountryRepository;
import monikamateska.com.elibrary.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, CountryRepository countryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }

    @Override
    public Page<Book> findAllWithPagination(Pageable pageable) {
        return this.bookRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return this.bookRepository.findById(id);
    }

    @Override
    public Optional<Book> save(BookDto bookDto) {
        Author author = this.authorRepository.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(bookDto.getAuthorId()));
        Book book = new Book(bookDto.getName(), author, bookDto.getCategory(), bookDto.getAvailableCopies());
        return Optional.of(this.bookRepository.save(book));
    }

    @Override
    public Optional<Book> edit(Long id, BookDto bookDto) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        Author author = this.authorRepository.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(bookDto.getAuthorId()));

        book.setName(bookDto.getName());
        book.setAuthor(author);
        book.setCategory(bookDto.getCategory());
        book.setAvailableCopies(bookDto.getAvailableCopies());

        return Optional.of(book);
    }

    @Override
    public void deleteById(Long id) {
        this.bookRepository.deleteById(id);
    }
}
