package service;

import repository.ClientRepository;
import domain.entities.Client;

import java.util.Scanner;

public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    public static void checkClientExistence(String cin, ClientRepository clientRepository, Scanner scanner) {
        if (clientRepository.doesClientExist(cin)) {
            Client client = clientRepository.getClientByCin(cin);
            System.out.println("Client with CIN " + cin + " exists in the database.");
            displayClient(client);
        } else {
            System.out.println("Client with CIN " + cin + " does not exist in the database.");

            System.out.println("Do you want to create a new client with this CIN? (yes/no)");
            String createOption = scanner.nextLine().toLowerCase();

            if (createOption.equals("yes")) {
                createClientFromUserInput(cin, clientRepository, scanner);
            } else {
                System.out.println("No new client created.");
            }
        }
    }

    public static void createClientFromUserInput(String cin, ClientRepository clientRepository, Scanner scanner) {
        System.out.println("Enter Client Full Name:");
        String fullName = scanner.nextLine();

        System.out.println("Enter Client Email:");
        String email = scanner.nextLine();

        int memberNum = clientRepository.generateUniqueMemberNumber();

        System.out.println("Enter Client Telephone:");
        String telephone = scanner.nextLine();

        Client newClient = new Client(fullName, email, cin, memberNum, telephone);
        clientRepository.createClient(newClient);
    }

    public static void displayClient(Client client) {
        if (client != null) {
            System.out.println("Client Information:");
            System.out.println("Full Name: " + client.getFullName());
            System.out.println("Email: " + client.getEmail());
            System.out.println("CIN: " + client.getCin());
            System.out.println("Member Number: " + client.getMemberNum());
            System.out.println("Telephone: " + client.getTelephone());
        } else {
            System.out.println("Client not found.");
        }
    }
}

