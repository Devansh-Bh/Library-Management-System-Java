package Pack2;

import Pack1.Book;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileService {

    private static final String FILE_NAME = "books.txt";

    // Save books (Day 5)
    public static void saveBooks(ArrayList<Book> books) {

        try {
            FileWriter fw = new FileWriter(FILE_NAME);

            for (Book b : books) {
                fw.write("ID: " + b.getId() + "\n");
                fw.write("TITLE: " + b.getTitle() + "\n");
                fw.write("ISSUED: " + b.isIssued() + "\n");
                fw.write("---\n");
            }

            fw.close();
            System.out.println("Books saved to file successfully.");

        } catch (IOException e) {
            System.out.println("Error while saving books to file.");
        }
    }

    // Load books (Day 6) - FileReader only, char-by-char
    public static ArrayList<Book> loadBooks() {

        ArrayList<Book> books = new ArrayList<>();

        try {
            FileReader fr = new FileReader(FILE_NAME);

            String line = "";

            // Temporary values for one book
            int id = -1;
            String title = "";
            boolean issued = false;

            int ch;

            while ((ch = fr.read()) != -1) {

                if (ch == '\n') {

                    line = line.trim();

                    if (line.startsWith("ID:")) {
                        id = Integer.parseInt(line.substring(3).trim());
                    } else if (line.startsWith("TITLE:")) {
                        title = line.substring(6).trim();
                    } else if (line.startsWith("ISSUED:")) {
                        issued = Boolean.parseBoolean(line.substring(7).trim());
                    } else if (line.equals("---")) {
                        // finalize a book when separator comes
                        if (id != -1) {
                            Book b = new Book(id, title);
                            b.setIssued(issued);
                            books.add(b);
                        }

                        // reset values for next book
                        id = -1;
                        title = "";
                        issued = false;
                    }

                    line = "";

                } else {
                    line += (char) ch;
                }
            }

            //  Handle last book if file doesn't end with ---
            if (id != -1) {
                Book b = new Book(id, title);
                b.setIssued(issued);
                books.add(b);
            }

            fr.close();

        } catch (IOException e) {
            System.out.println("No previous file found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("File format error. Could not load data.");
        }

        return books;
    }
}
