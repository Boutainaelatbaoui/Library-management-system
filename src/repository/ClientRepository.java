package repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbconnection.DbConnection;
import domain.entities.*;

public class ClientRepository {
    private Connection connection;

    public ClientRepository(Connection connection) {
        this.connection = connection;
    }

    public Client getClientByCin(String clientCin) {
        String query = "SELECT * FROM clients WHERE cin = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, clientCin);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Client client = new Client();

                    client.setId(resultSet.getInt("client_id"));
                    client.setFullName(resultSet.getString("full_name"));
                    client.setEmail(resultSet.getString("email"));
                    client.setCin(clientCin);
                    client.setMemberNum(resultSet.getInt("member_num"));
                    client.setTelephone(resultSet.getString("telephone"));
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean doesClientExist(String cin) {
        String query = "SELECT * FROM clients WHERE cin = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int generateUniqueMemberNumber() {
        String query = "SELECT MAX(member_num) FROM clients";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int maxMemberNumber = resultSet.getInt(1);
                return maxMemberNumber + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    public void createClient(Client client) {
        int memberNum = this.generateUniqueMemberNumber();

        String query = "INSERT INTO clients (full_name, email, cin, member_num, telephone) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, client.getFullName());
            preparedStatement.setString(2, client.getEmail());
            preparedStatement.setString(3, client.getCin());
            preparedStatement.setInt(4, memberNum);
            preparedStatement.setString(5, client.getTelephone());

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Client created successfully.");
            } else {
                System.out.println("Failed to create the client.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
