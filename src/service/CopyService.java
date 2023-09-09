package service;

import java.util.List;
import domain.entities.BookCopy;
import domain.enums.Status;
import repository.*;

public class CopyService {
    public boolean hasAvailableBookCopy(String bookTitle, CopyRepository copyRepository) {

        BookCopy bookCopy = copyRepository.getBookCopies(bookTitle);

        if (bookCopy != null) {
            return true;
        }
        return false;
    }
}


