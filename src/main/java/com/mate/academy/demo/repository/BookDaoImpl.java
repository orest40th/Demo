package com.mate.academy.demo.repository;

import com.mate.academy.demo.exception.DataProcessingException;
import com.mate.academy.demo.model.Book;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();

            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataProcessingException("Cannot insert entity into DB: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session.createQuery("from Book where id = :value",
                    Book.class).setParameter("value", id);

            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Cannot fetch book from DB by id " + id, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot fetch all books from DB", e);
        }
    }
}
