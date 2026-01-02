import Pack1.Book;
import Pack2.FileService;
import Pack3.MyLinkedList;

import exceptions.BookNotFoundException;
import exceptions.BookAlreadyIssuedException;
import exceptions.BookNotIssuedException;

import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;

public class Demo {

    private static boolean isDuplicateBookId(ArrayList<Book> books, int id) {
        for (Book b : books) {
            if (b.getId() == id) return true;
        }
        return false;
    }

    private static boolean isAlreadyInQueue(Queue<Integer> q, int id) {
        for (int x : q) {
            if (x == id) return true;
        }
        return false;
    }

    private static Book findBook(ArrayList<Book> books, int id) throws BookNotFoundException {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        throw new BookNotFoundException("Book ID " + id + " not found.");
    }

    private static void printBooks(ArrayList<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println("\n--- BOOK LIST ---");
        System.out.println("ID | Title | Status");
        System.out.println("---------------------------");

        for (Book b : books) {
            System.out.println(b.getId() + " | " + b.getTitle() + " | " + (b.isIssued() ? "Issued" : "Available"));
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Book> books = FileService.loadBooks();
        Queue<Integer> issueQueue = new LinkedList<>();
        Stack<Book> returnStack = new Stack<>();
        MyLinkedList history = new MyLinkedList();

        history.add("System started. Loaded books: " + books.size());

        while (true) {

            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM  =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Request Issue (Queue)");
            System.out.println("4. Process Issue (FIFO)");
            System.out.println("5. Return Book");
            System.out.println("6. Undo Return (Stack LIFO)");
            System.out.println("7. View History");
            System.out.println("8. Save Books");
            System.out.println("9. Exit (Ask Save)");
            System.out.println("============================================");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {
                    System.out.print("Enter Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    if (id <= 0) {
                        System.out.println("Invalid ID. Must be positive.");
                        history.add("Failed Add (Invalid ID): " + id);
                        break;
                    }

                    if (isDuplicateBookId(books, id)) {
                        System.out.println("Error: Book ID already exists.");
                        history.add("Failed Add (Duplicate ID): " + id);
                        break;
                    }

                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();

                    if (title.trim().isEmpty()) {
                        System.out.println("Error: Title cannot be empty.");
                        history.add("Failed Add (Empty Title) ID: " + id);
                        break;
                    }

                    books.add(new Book(id, title));
                    System.out.println("Book added successfully");
                    history.add("Added Book ID: " + id + " Title: " + title);
                }

                case 2 -> printBooks(books);

                case 3 -> {
                    System.out.print("Enter Book ID to Issue: ");
                    int id = sc.nextInt();

                    try {
                        Book b = findBook(books, id);

                        if (b.isIssued()) {
                            throw new BookAlreadyIssuedException("Book ID " + id + " is already issued.");
                        }

                        if (isAlreadyInQueue(issueQueue, id)) {
                            System.out.println("Error: This book is already in issue queue.");
                            history.add("Issue Request Failed (Already In Queue) ID: " + id);
                            break;
                        }

                        issueQueue.add(id);
                        System.out.println("Issue request added to queue  (FIFO)");
                        history.add("Issue Requested (Queued) Book ID: " + id);

                    } catch (BookNotFoundException | BookAlreadyIssuedException e) {
                        System.out.println("Error: " + e.getMessage());
                        history.add("Issue Request Failed: " + e.getMessage());
                    }
                }

                case 4 -> {
                    try {
                        if (issueQueue.isEmpty()) {
                            System.out.println("No issue requests in queue.");
                            history.add("Process Issue Failed (Queue Empty)");
                            break;
                        }

                        int id = issueQueue.poll();
                        Book b = findBook(books, id);

                        b.issueBook();

                        System.out.println("Book issued successfully ");
                        history.add("Issued Book ID: " + id + " (Processed FIFO)");

                    } catch (BookNotFoundException | BookAlreadyIssuedException e) {
                        System.out.println("Error: " + e.getMessage());
                        history.add("Process Issue Failed: " + e.getMessage());
                    }
                }

                case 5 -> {
                    System.out.print("Enter Book ID to Return: ");
                    int id = sc.nextInt();

                    try {
                        Book b = findBook(books, id);

                        b.returnBook();
                        returnStack.push(b);

                        System.out.println("Book returned successfully ");
                        history.add("Returned Book ID: " + id + " (Pushed to Stack)");

                    } catch (BookNotFoundException | BookNotIssuedException e) {
                        System.out.println("Error: " + e.getMessage());
                        history.add("Return Failed: " + e.getMessage());
                    }
                }

                case 6 -> {
                    if (returnStack.isEmpty()) {
                        System.out.println("Nothing to undo. Stack empty.");
                        history.add("Undo Return Failed (Stack Empty)");
                        break;
                    }

                    Book b = returnStack.pop();

                    try {
                        b.issueBook();
                        System.out.println("Undo successful  Book re-issued.");
                        history.add("Undo Return: Re-issued Book ID: " + b.getId() + " (LIFO)");
                    } catch (BookAlreadyIssuedException e) {
                        System.out.println("Error: " + e.getMessage());
                        history.add("Undo Return Failed: " + e.getMessage());
                    }
                }

                case 7 -> {
                    System.out.println("\n--- HISTORY ---");
                    history.display();
                }

                case 8 -> {
                    FileService.saveBooks(books);
                    history.add("Books saved to file.");
                }

                case 9 -> {
                    System.out.print("Do you want to save before exit? ( 1(Yes) / 2(No) ): ");
                    int ans = sc.nextInt();

                    if (ans == 1) {
                        FileService.saveBooks(books);
                        System.out.println("Saved ");
                        history.add("Saved on exit.");
                    } else {
                        System.out.println("Not saved.");
                        history.add("Exit without saving.");
                    }

                    System.out.println("Goodbye!");
                    sc.close();
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
