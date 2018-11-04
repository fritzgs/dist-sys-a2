package a2_fritz_20071968;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client {
	// Text field for receiving radius
	private JTextField textField = new JTextField();

	// Text area to display contents

	private JTextArea textDisp = new JTextArea();
	private Button send = new Button();
	private JPasswordField passTextField;
	private JTextField studentNoField;

	private JFrame frame;

	private String message;

	private JScrollPane display;

	private User user;

	private Button logoutBtn;
	private Button loginBtn;
	private SQLHandler sql = new SQLHandler();
	// IO streams

	private DataOutputStream toServer;
	private DataInputStream fromServer;

	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		frame = new JFrame();
		frame.setTitle("Client");
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel login = new JPanel();
		login.setLayout(new GridLayout(2, 3));

		JLabel studentNo = new JLabel("Student Number");
		studentNoField = new JTextField();

		JLabel pass = new JLabel("Password");
		passTextField = new JPasswordField();

		loginBtn = new Button("Login");
		loginBtn.addActionListener(new Listener());
		loginBtn.setActionCommand("login");
		loginBtn.setEnabled(true);

		logoutBtn = new Button("Logout");
		logoutBtn.addActionListener(new Listener());
		logoutBtn.setActionCommand("logout");
		logoutBtn.setEnabled(false);

		login.add(studentNo);
		login.add(studentNoField);
		login.add(logoutBtn);
		login.add(pass);
		login.add(passTextField);
		login.add(loginBtn);

		textField.setHorizontalAlignment(JTextField.RIGHT);
		textField.setEnabled(false);

		JPanel client = new JPanel();
		client.setLayout(new BorderLayout());
		client.add(new JLabel("Message:"), BorderLayout.WEST);
		client.add(textField, BorderLayout.CENTER);

		send.setLabel("Send");
		send.addActionListener(new Listener());
		send.setActionCommand("send");
		send.setEnabled(false);
		client.add(send, BorderLayout.EAST);

		textDisp.setEditable(false);

		display = new JScrollPane(textDisp);

		frame.setLayout(new BorderLayout());

		frame.add(login, BorderLayout.NORTH);
		frame.add(client, BorderLayout.SOUTH);
		frame.add(display, BorderLayout.CENTER);

		frame.setVisible(true); // It is necessary to show the frame here!

		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8000);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());

			while (true) {
				String serverMessage = fromServer.readUTF();
				textDisp.append(serverMessage + "\n");
			}
		} catch (IOException ex) {
			textDisp.append(ex.toString() + '\n');
		}
	}

	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("send")) {
				try {
					if (!textField.getText().equals("")) {
						message = user.getFullName() + " - " + user.getStudetNo() + ": " + textField.getText();

						textDisp.append(message + "\n");
						textField.setText("");

						// Send the radius to the server
						toServer.writeUTF(message);
						toServer.flush();
					}
				} catch (IOException ex) {
					System.err.println(ex);
				}
			} else if (command.equals("login")) {
				try {

					Connection con = sql.getConnection();
					if (sql.studentNoExist(con, studentNoField.getText(),
							String.valueOf(passTextField.getPassword())) == true) {
						logoutBtn.setEnabled(true);
						loginBtn.setEnabled(false);
						user = new User(sql.getFirstName(con, studentNoField.getText()),
								sql.getLastName(con, studentNoField.getText()), studentNoField.getText());
						textDisp.append("Logged as " + user.getFullName() + "\n");
						send.setEnabled(true);
						textField.setEnabled(true);
						passTextField.setText("");
						passTextField.setEnabled(false);
						studentNoField.setEnabled(false);

						try {
							toServer.writeUTF(
									"!! " + user.getFullName() + " - " + user.getStudetNo() + " has logged on.");

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else {
						JOptionPane failed = new JOptionPane();
						failed.showMessageDialog(frame, "Failed to Login", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (command.equals("logout")) {
				int confirm = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Confirm Logout",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					loginBtn.setEnabled(true);
					logoutBtn.setEnabled(false);
					textDisp.append("Logging out..." + "\n");

					user = null;
					textField.setEnabled(false);
					send.setEnabled(false);
					textDisp.setText("");
					studentNoField.setText("");
					passTextField.setText("");
					passTextField.setText("");
					passTextField.setEnabled(true);
					studentNoField.setEnabled(true);
				}
			}
		}
	}

}