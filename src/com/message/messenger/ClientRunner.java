package com.message.messenger;

import javax.swing.*;

public class ClientRunner {
    public static void main(String[] args) {
        Client client;
        client = new Client("xxx.xxx.x.x");
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.startRunning();

    }
}
