package com.message.messenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String message = "";
    private String serverIP;
    private Socket socketConnection;

    //Constructor
    public Client(String host){
        super("Instant Messenger Client");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(e.getActionCommand());
                userText.setText("");
            }
        });
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(350,150);
        setVisible(true);
    }

    //Connect Server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException eofException){
            showMessage("\n Client Terminated Connection");
        } catch(IOException ioException){
            ioException.printStackTrace();
        }finally {
            closeConnection();
        }
    }

    //Connect Server
    private void connectToServer() throws IOException{
        showMessage("Attempting Connection... \n");
        socketConnection = new Socket(InetAddress.getByName(serverIP), 6789); // IMPORTANT: Use the same port number from the other connection project
        showMessage("Connected to: "+socketConnection.getInetAddress().getHostName());
    }

    //Setup Stream To Send Messages & Receive Messages
    private void setupStreams() throws IOException{
        outputStream = new ObjectOutputStream(socketConnection.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socketConnection.getInputStream());
        showMessage("\n Streams Are Connected!\n");
    }

    //While Chatting With Server
    private void whileChatting() throws IOException{
        typeMessage(true);
        do{
            try{
                message = (String) inputStream.readObject();
                showMessage("\n"+message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("\n Cannot Recognize Message Sent!");
            }
        }while(!message.equals("SERVER - END"));
    }

    //Close the Stream & Socket
    private void closeConnection(){
        showMessage("\n Closing Connection...");
        typeMessage(false);
        try{
            outputStream.close();
            inputStream.close();
            socketConnection.close();

        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    //Send Messages To Server
    private void sendMessage(String message){
        try{
            outputStream.writeObject("CLIENT - "+ message);
            outputStream.flush();
            showMessage("\n CLIENT - "+ message);
        }catch (IOException ioException){
            chatWindow.append("\n Something Went Wrong!");
        }
    }

    //Change/Update ChatWindow
    private void showMessage(final String m){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatWindow.append(m);
            }
        });
    }

    //Allow User Permission To Type
    private void typeMessage(final boolean tof){ //NOTE: tof --> True or False
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userText.setEditable(tof);
            }
        });
    }
}
