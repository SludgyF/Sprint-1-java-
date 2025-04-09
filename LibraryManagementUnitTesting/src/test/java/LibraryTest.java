import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;  
  
public class LibraryTest {  
  
    @Test  
    public void testAddBook() {  
        Library library = new Library();  
        Book book = new Book("1984", "George Orwell");  
        library.addBook(book);  
        assertTrue(library.listAvailableBooks().contains(book));  
    }

}  
 