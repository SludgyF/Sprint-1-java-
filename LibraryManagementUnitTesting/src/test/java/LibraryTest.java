import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.time.LocalDate;
  
public class LibraryTest {  
  
    @Test  
    public void testAddBook() {  
        Library library = new Library();  
        Book book = new Book("1984", "George Orwell");  
        library.addBook(book);  
        assertTrue(library.listAvailableBooks().contains(book));  
    }

    @Test
    public void testCheckOutNonexistentBook() {
        Library library = new Library();
        Patron patron = new Patron("Juan Perez");
        Book nonExistentBook = new Book("El cuervo", "Edgar Allan Poe");
        boolean result = library.checkOutBook(patron, nonExistentBook, 7);

        assertFalse(result, "The system should not allow checkout of a non-existent book.");
    }

    @Test  
    public void testCalculateFineAfterReturn() {  
        // Setup  
        Library library = new Library();  
        Patron patron = new Patron("Alice Smith");  
        Book book = new Book("Design Patterns", "Erich Gamma");  
  
        library.addBook(book);  
        library.addPatron(patron);  
          
        // Check out for 2 days  
        library.checkOutBook(patron, book, 2);   
          
        // Simulate that 2 days have passed, and set the due date  
        book.setDueDate(LocalDate.now().minusDays(2));  
  
        // Return the book  
        library.returnBook(patron);   
  
        // Act: Calculate fine after returning  
        double fineAfterReturn = library.calculateFine(patron);   
  
        // Assert: No fine should be calculated after return  
        assertEquals(0, fineAfterReturn, "The fine should be zero after returning the book.");  
    }
    
    @Test
    public void testReturnBookSuccessfully() {
        Library library = new Library();
        Patron patron = new Patron("Carlos Mendoza");
        Book book = new Book("Refactoring", "Martin Fowler");

        library.addBook(book);
        library.addPatron(patron);

        // Prestar el libro
        boolean checkoutResult = library.checkOutBook(patron, book, 7);
        assertTrue(checkoutResult, "El libro debería poder prestarse");

        // Devolver el libro
        boolean returnResult = library.returnBook(patron);
        assertTrue(returnResult, "El libro debería devolverse correctamente");

        // Verificar que el libro esté disponible
        assertFalse(book.isCheckedOut(), "El libro debería estar disponible después de devolverlo");

        // Verificar que ya no esté en los préstamos activos
        assertFalse(library.listAvailableBooks().isEmpty(), "El libro debería estar en la lista de disponibles");
    }

    @Test
    public void testListAvailableBooks() {
        Library library = new Library();
        Patron patron = new Patron("Laura Sánchez");

        Book book1 = new Book("Effective Java", "Joshua Bloch");
        Book book2 = new Book("The Pragmatic Programmer", "Andrew Hunt");
        Book book3 = new Book("Domain-Driven Design", "Eric Evans");

        // Agregar todos los libros a la biblioteca
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        // Prestar solo book2
        library.addPatron(patron);
        library.checkOutBook(patron, book2, 7);

        // Obtener lista de libros disponibles
        List<Book> availableBooks = library.listAvailableBooks();

        // Verificaciones
        assertTrue(availableBooks.contains(book1), "book1 debería estar disponible");
        assertFalse(availableBooks.contains(book2), "book2 está prestado, no debería estar disponible");
        assertTrue(availableBooks.contains(book3), "book3 debería estar disponible");
        assertEquals(2, availableBooks.size(), "Solo 2 libros deberían estar disponibles");
    }

    @Test
    public void testListRegisteredPatrons() {
        Library library = new Library();

        Patron patron1 = new Patron("Ana Torres");
        Patron patron2 = new Patron("Luis Ríos");
        Patron patron3 = new Patron("Marta Díaz");

        library.addPatron(patron1);
        library.addPatron(patron2);
        library.addPatron(patron3);

        List<Patron> patrons = library.listPatrons();

        // Verificaciones
        assertEquals(3, patrons.size(), "Debería haber 3 usuarios registrados.");
        assertTrue(patrons.contains(patron1), "La lista debe contener a Ana Torres.");
        assertTrue(patrons.contains(patron2), "La lista debe contener a Luis Ríos.");
        assertTrue(patrons.contains(patron3), "La lista debe contener a Marta Díaz.");
    }

    @Test
    public void testRegisterDuplicatePatron() {
        Library library = new Library();

        Patron original = new Patron("Fernando Núñez");
        Patron duplicado = new Patron("Fernando Núñez");

        library.addPatron(original);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            library.addPatron(duplicado);
        });

        assertEquals("El patron ya está registrado.", exception.getMessage());
    }

    @Test
    public void testReturnBookThatWasNotCheckedOut() {
        Library library = new Library();
        Patron patron = new Patron("Gabriela López");

        library.addPatron(patron);

        // No se ha prestado ningún libro
        boolean result = library.returnBook(patron);

        assertFalse(result, "No se debería poder devolver un libro si el patron no tiene ninguno prestado.");
    }

    @Test
    public void testBookCreated() {
        Book book = new Book("Brave New World", "Aldous Huxley");

        assertEquals("Brave New World", book.getTitle(), "El título debería coincidir");
        assertEquals("Aldous Huxley", book.getAuthor(), "El autor debería coincidir");
        assertFalse(book.isCheckedOut(), "El libro no debería estar prestado al crearse");
        assertNull(book.getDueDate(), "La fecha de vencimiento debe ser nula inicialmente");
    }

    @Test
    public void testRegisterPatron() {
        Patron patron = new Patron("John Doe");

        assertEquals("John Doe", patron.getName(), "El nombre del patron debería ser correcto.");
        assertTrue(patron.getCheckedOutBooks().isEmpty(), "El patron no debería tener libros prestados al inicio.");
    }

    @Test
    public void testCalculateFineNoOverdue() {
        Library library = new Library();
        Patron patron = new Patron("Tomás Díaz");
        Book book = new Book("1984", "George Orwell");

        library.addBook(book);
        library.addPatron(patron);
        library.checkOutBook(patron, book, 7);
        book.setDueDate(LocalDate.now());

        double fine = library.calculateFine(patron);
        assertEquals(0.0, fine, "No debe haber multa si el libro no está atrasado.");
    }

    @Test
    public void testCheckOutAlreadyCheckedOutBook() {
        Library library = new Library();
        Patron patron1 = new Patron("Lucas");
        Patron patron2 = new Patron("Sara");
        Book book = new Book("Cien años de soledad", "Gabriel García Márquez");

        library.addBook(book);
        library.addPatron(patron1);
        library.addPatron(patron2);

        boolean firstAttempt = library.checkOutBook(patron1, book, 7);
        boolean secondAttempt = library.checkOutBook(patron2, book, 7);

        assertTrue(firstAttempt, "El primer préstamo debería ser exitoso.");
        assertFalse(secondAttempt, "El segundo préstamo no debería ser permitido.");
    }

    @Test
    public void testCheckOutAndReturnFlow() {
        Patron patron = new Patron("Lucía Soto");
        Book book = new Book("La sombra del viento", "Carlos Ruiz Zafón");

        patron.checkOutBook(book);
        assertTrue(patron.hasCheckedOutBook(book), "El libro debe estar en la lista del patron.");

        patron.returnBook(book);
        assertFalse(patron.hasCheckedOutBook(book), "El libro ya no debe estar en la lista del patron.");
    }

    @Test
    public void testSetDueDateOnlyIfCheckedOut() {
        Book book = new Book("El alquimista", "Paulo Coelho");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            book.setDueDate(LocalDate.now().plusDays(7));
        });

        assertEquals("Cannot set due date for a book that is not checked out.", exception.getMessage());
    }

}  
 