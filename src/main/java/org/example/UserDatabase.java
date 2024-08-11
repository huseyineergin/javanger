package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserDatabase {
    private static final String DATABASE_FILE = "users.txt";
    private static final BSTree users = BSTree.getInstance();

    public boolean registerUser(String username, String password) {
        if (usernameExists(username)) {
            return false;
        }

        try {
            FileWriter fileWriter = new FileWriter(DATABASE_FILE, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(username + ":" + password);
            users.insert(username, password);
            writer.newLine();
            writer.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        try {
            FileReader fileReader = new FileReader(DATABASE_FILE);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            while (line != null) {
                String[] userData = line.split(":");
                if (userData.length == 2 && userData[0].equals(username) && userData[1].equals(password)) {
                    return true;
                }
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadUsers() {
        try {
            FileReader fileReader = new FileReader(DATABASE_FILE);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            while (line != null) {
                String[] userData = line.split(":");
                if (userData.length == 2) {
                    String username = userData[0];
                    String password = userData[1];
                    users.insert(username, password);
                }
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean usernameExists(String username) {
        try {
            FileReader fileReader = new FileReader(DATABASE_FILE);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            while (line != null) {
                String[] userData = line.split(":");
                if (userData.length == 2 && userData[0].equals(username)) {
                    return true;
                }
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
