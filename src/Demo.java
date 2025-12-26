import java.sql.SQLOutput;
import java.util.Scanner;
import Pack1.Book;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Demo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);


        Book b1 = new Book(1, "JAVA");
        Book b2 = new Book(2,"OOP");
        while(true){
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM=====");
            System.out.println("1. Show Sample Books");
            System.out.println("2. Issued Book 1");
            System.out.println("3. Return Book 1");
            System.out.println("4. Exit");
            System.out.println("======================================");
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            if(choice == 1){
                System.out.println(b1);
                System.out.println(b2);
            }else if (choice == 2){
                b1.issueBook();
                System.out.println("Book 1 issued");
            }else if (choice == 3){
                b1.returnBook();
                System.out.println("Book 1 returnd");
            }else if (choice == 4){
                System.out.println("Exiting........");
                break;
            }else{
                System.out.println("Invalid Choice! Please Try again..");
            }
        }
        sc.close();
    }
}