package org.academiadecodigo.cachealots.serverChat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


class ClientDispatcher implements Runnable{

    private final Socket clientSocket;
    private Socket serverSocket;
    private DataOutputStream out;
    private BufferedReader in;
    private String message = "";
    private ChatServer chatserver;
    public String name = "";

    public ClientDispatcher(Socket clientSocket, ChatServer chatServer, String name){
        this.clientSocket = clientSocket;
        this.chatserver = chatServer;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.writeBytes("Username: ");
            out.flush();
            chatserver.broadcast(name + " has joined the chat!\n");

            while (clientSocket.isBound()) {
                out.writeBytes("\n: ");
                out.flush();
                // read line from socket input reader
                message = in.readLine();
                // if received /quit close break out of the reading loop
                if (message.contains("666")) {
                }
                // show the received line to the console
                System.out.println(message);
                chatserver.broadcast(name + ": " + message + "\n");
            }

        } catch (IOException ex) { System.out.println("Receiving error: " + ex.getMessage()); }
    }

    void receiveMessage(String message) throws IOException {
        out.writeBytes(message);
        out.flush();
    }

    private void closeClientSocket() {
        try {

            if (clientSocket != null) {
                System.out.println("Closing client connection");
                clientSocket.close();
            }

        } catch (IOException ex) {

            System.out.println("Error closing connection: " + ex.getMessage());

        }
    }

    public void closeServerSocket() {

        try {

            if (serverSocket != null) {
                System.out.println("Closing server socket");
                serverSocket.close();
            }

        } catch (IOException ex) {

            System.out.println("Error closing connection: " + ex.getMessage());

        }

    }
}