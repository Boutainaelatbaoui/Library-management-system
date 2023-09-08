package controller;

import dbconnection.DbConnection;
import domain.entities.Client;
import repository.BookRepository;
import repository.ClientRepository;
import service.BookService;
import service.ClientService;

import java.util.Scanner;
import java.sql.Connection;

public class ClientController {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DbConnection.getConnection();
        ClientRepository clientRepository = new ClientRepository(connection);
        ClientService clientService = new ClientService(new ClientRepository(connection));

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Check if Client Exists by CIN");
            System.out.println("2. Return to The Menu");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Enter CIN to check if client exists:");
                    String cin = scanner.nextLine();
                    Client client = clientService.checkClientExistence(cin, scanner);

                    if (client != null) {
                        BookController.searchByTitle(new BookRepository(), scanner, new BookService());
                    }
                    break;
                case 2:
                    BookController.main(args);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
