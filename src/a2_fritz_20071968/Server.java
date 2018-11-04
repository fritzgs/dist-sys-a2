package a2_fritz_20071968;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {

	private JTextField textField;
	private JTextArea textDisp;
	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;

	public static void main(String[] args) {
		new Server();
	}

	public Server() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		textField = new JTextField();
		textDisp = new JTextArea();
		Button sendBtn = new Button("Send");

		panel.add(new JLabel("Message:"), BorderLayout.WEST);
		panel.add(textField, BorderLayout.CENTER);
		panel.add(sendBtn, BorderLayout.EAST);

		textField.setHorizontalAlignment(JTextField.RIGHT);
		sendBtn.addActionListener(new Listener());
		sendBtn.setActionCommand("send");

		setLayout(new BorderLayout());
		add(panel, BorderLayout.NORTH);
		add(new JScrollPane(textDisp), BorderLayout.CENTER);

		setTitle("Server");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		try {
			ServerSocket serverSocket = new ServerSocket(8000);
			textDisp.append("Server started at " + new Date() + "\n");

			// Listen for connection
			Socket socket = serverSocket.accept();

			// Create data input/output stream
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());

			while (true) {
				String clientMessage = inputFromClient.readUTF();

				textDisp.append(clientMessage + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("send")) {
				if (!textField.getText().equals("")) {
					String message = "Server: " + textField.getText();
					textField.setText("");

					try {
						outputToClient.writeUTF(message);
						outputToClient.flush();
						textDisp.append(message + "\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
