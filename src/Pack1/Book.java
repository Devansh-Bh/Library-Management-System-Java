package Pack1;

import exceptions.BookAlreadyIssuedException;
import exceptions.BookNotIssuedException;

public class Book {

    private int id;
    private String title;
    private boolean issued;

    public Book(int id, String title) {
        this.id = id;
        this.title = title;
        this.issued = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isIssued() {
        return issued;
    }

    // Issue book with exception
    public void issueBook() throws BookAlreadyIssuedException {
        if (issued) {
            throw new BookAlreadyIssuedException("Book ID " + id + " is already issued.");
        }
        issued = true;
    }

    // Return book with exception
    public void returnBook() throws BookNotIssuedException {
        if (!issued) {
            throw new BookNotIssuedException("Book ID " + id + " is not issued, cannot return.");
        }
        issued = false;
    }

    // Used when loading from file
    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    public String display() {
        String status = issued ? "Issued" : "Available";
        return "Book ID: " + id + " | Title: " + title + " | Status: " + status;
    }
}
