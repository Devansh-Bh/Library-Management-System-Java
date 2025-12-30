import Pack1.Book;
import Pack2.FileService;
import Pack3.MyLinkedList;

import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

public class Demo {

    private static boolean isDuplicate(ArrayList<Book> books, int id) {
        for (Book b : books) {
            if (b.getId() == id) return true;
        }
        return false;
    }

    private static Book findBook(ArrayList<Book> books, int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //  load books
        ArrayList<Book> books = FileService.loadBooks();

        //  Queue for issue requests (FIFO)
        Queue<Integer> issueQueue = new LinkedList<>();

        // History using custom linked list
        MyLinkedList history = new MyLinkedList();
        history.add("System started. Loaded books: " + books.size());

        while (true) {

            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM (DAY 7) =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Request Issue (Queue)");
            System.out.println("4. Process Issue (FIFO)");
            System.out.println("5. View History");
            System.out.println("6. Save Books");
            System.out.println("7. Exit");
            System.out.println("============================================");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {
                    System.out.print("Enter Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    if (isDuplicate(books, id)) {
                        System.out.println("Error: Book ID already exists.");
                        history.add("Failed Add (Duplicate ID): " + id);
                        break;
                    }

                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();

                    books.add(new Book(id, title));
                    history.add("Added Book ID: " + id + " Title: " + title);
                    System.out.println("Book added successfully.");
                }

                case 2 -> {
                    if (books.isEmpty()) {
                        System.out.println("No books available.");
                        break;
                    }

                    System.out.println("\n--- BOOK LIST ---");
                    for (Book b : books) {
                        System.out.println(b.display());
                    }
                }

                //  Request issue -> enqueue book ID
                case 3 -> {
                    System.out.print("Enter Book ID to Issue: ");
                    int id = sc.nextInt();

                    Book b = findBook(books, id);

                    if (b == null) {
                        System.out.println("Error: Book not found.");
                        history.add("Issue Request Failed (Not Found) ID: " + id);
                        break;
                    }

                    if (b.isIssued()) {
                        System.out.println("Error: Book already issued.");
                        history.add("Issue Request Failed (Already Issued) ID: " + id);
                        break;
                    }

                    issueQueue.add(id); // enqueue
                    System.out.println("Issue request added to queue (FIFO).");
                    history.add("Issue Requested (Queued) Book ID: " + id);
                }

                //  Process issue -> dequeue and issue book
                case 4 -> {
                    if (issueQueue.isEmpty()) {
                        System.out.println("No issue requests in queue.");
                        history.add("Process Issue Failed (Queue Empty)");
                        break;
                    }

                    int id = issueQueue.poll(); // dequeue FIFO
                    Book b = findBook(books, id);

                    if (b == null) {
                        System.out.println("Error: Book not found while processing.");
                        history.add("Process Issue Failed (Not Found) ID: " + id);
                        break;
                    }

                    if (b.isIssued()) {
                        System.out.println("Book already issued. Skipping.");
                        history.add("Process Issue Skipped (Already Issued) ID: " + id);
                        break;
                    }

                    b.issueBook();
                    System.out.println("Book issued successfully ");
                    history.add("Issued Book ID: " + id + " (Processed FIFO)");
                }

                case 5 -> {
                    System.out.println("\n--- HISTORY ---");
                    history.display();
                }

                case 6 -> {
                    FileService.saveBooks(books);
                    history.add("Books saved to file.");
                }

                case 7 -> {
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
