package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class App {
    private static UserDatabase userDatabase = new UserDatabase();

    private static JFrame frame = new JFrame("Javanger");

    private static JComboBox<String> contactComboBox;
    private static JTextField messageField;
    private static JTextArea messageArea;

    private static BSTree users = BSTree.getInstance();
    private static User currUser = null;

    public static void main(String[] args) {
        userDatabase.loadUsers();
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        JPanel authPanel = createAuthPanel();
        frame.getContentPane().add(authPanel);

        frame.pack();
        frame.setVisible(true);
        frame.setSize(320, 180);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Auth Panel
    private static JPanel createAuthPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton signUpButton = new JButton("Sign Up");
        JButton signInButton = new JButton("Sign In");

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.length() < 3) {
                showMessageDialog(panel, "Username length must be greater than 3.", "Sign Up", WARNING_MESSAGE);
            } else if (password.length() < 8) {
                showMessageDialog(panel, "Password length must be greater than 8.", "Sign Up", WARNING_MESSAGE);
            } else if (userDatabase.registerUser(username, password)) {
                showMessageDialog(panel, "Sign up successful.", "Sign Up", INFORMATION_MESSAGE);
                clearFields(usernameField, passwordField);
            } else {
                showMessageDialog(panel, "Username already in use.", "Sign Up", ERROR_MESSAGE);
            }
        });

        signInButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (userDatabase.loginUser(username, password)) {
                currUser = users.findUserByUsername(username);
                JPanel messagingPanel = createMessagingPanel();
                panel.removeAll();
                panel.add(messagingPanel);
                panel.revalidate();

                frame.setSize(480, 270);
                frame.setTitle("Javanger - Signed In as " + currUser.getUsername());
            } else {
                showMessageDialog(panel, "Invalid username or password.", "Sign In", ERROR_MESSAGE);
            }
        });

        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Message Panel
    private static JPanel createChatPanel() {
        JPanel chatPanel = new JPanel(new BorderLayout());

        messageArea = new JTextArea(10, 30);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);

        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        chatPanel.add(messageScrollPane, BorderLayout.CENTER);

        JPanel messageInputPanel = new JPanel(new BorderLayout());

        messageField = new JTextField();
        messageInputPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = createSendButton();
        messageInputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(messageInputPanel, BorderLayout.SOUTH);

        return chatPanel;
    }

    private static JButton createSendButton() {
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String message = messageField.getText();

            if (!message.isEmpty()) {
                String receiverName = (String) contactComboBox.getSelectedItem();
                User u = users.findUserByUsername(receiverName);

                if (u != null) {
                    Message m = new Message(currUser, u, message);
                    currUser.sendMessage(u, m);
                    messageField.setText("");
                    messageArea.append(currUser.getUsername() + ": " + message + "\n");
                }
            }
        });

        return sendButton;
    }

    private static JPanel createMessagingPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel contactListPanel = createContactListPanel(panel);
        panel.add(contactListPanel, BorderLayout.WEST);

        JPanel chatPanel = createChatPanel();
        panel.add(chatPanel, BorderLayout.CENTER);

        updateContactList();

        return panel;
    }

    private static JPanel createButtonPanel(JPanel panel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(e -> {
            currUser = null;
            JPanel authPanel = createAuthPanel();
            panel.removeAll();
            panel.add(authPanel);
            panel.revalidate();

            frame.setSize(320, 180);
            frame.setTitle("Javanger");
        });

        buttonPanel.add(signOutButton);

        return buttonPanel;
    }

    private static JPanel createContactListPanel(JPanel panel) {
        JPanel contactListPanel = new JPanel(new BorderLayout());
        contactListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        DefaultComboBoxModel<String> contactComboBoxModel = new DefaultComboBoxModel<>();
        contactComboBoxModel.setSelectedItem("");
        contactComboBox = new JComboBox<>(contactComboBoxModel);
        contactComboBox.addActionListener(e -> {
            String receiverName = (String) contactComboBox.getSelectedItem();
            User u = users.findUserByUsername(receiverName);
            if (u != null) {
                Chat chat = Chat.getChat(currUser, u);
                var list = chat.getMessagesList();
                messageArea.setText(null);
                list.forEach(message -> messageArea.append(message.getSender().getUsername() + ": " + message.getContent() + "\n"));
            }
        });
        contactListPanel.add(contactComboBox, BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanel(panel);
        contactListPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contactListPanel;
    }

    // Helpers
    private static void clearFields(JTextField usernameField, JPasswordField passwordField) {
        usernameField.setText("");
        passwordField.setText("");
    }

    private static void updateContactList() {
    DefaultComboBoxModel<String> contactComboBoxModel = (DefaultComboBoxModel<String>) contactComboBox.getModel();
    LinkedList<User> userList = users.getUsers(users.getRoot(), new LinkedList<>());

    for (User user : userList) {
        String username = user.getUsername();
        if (!username.equals(currUser.getUsername())) {
            contactComboBoxModel.addElement(username);
        }
    }
}

    private static void showMessageDialog(Component parentComponent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }
}

