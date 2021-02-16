package com.message.messenger;

import javax.swing.*;

public class ClientRunner {
    public static void main(String[] args) {
        Client client;
        client = new Client("192.168.0.9");
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.startRunning();

    }
}
