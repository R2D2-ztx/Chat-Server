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
    public boolean actions;

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

            chatserver.broadcast(">>> " + name + " Has Logged inside the matrix!\n");

            while (clientSocket.isBound()) {
                actions = false;
                // read line from socket input reader
                message = in.readLine();
                // if received /quit close break out of the reading loop
                changeName();
                // show the received line to the console
                printMessage();

                statusOn();

                quit();

            }

        } catch (IOException ex) { System.out.println("Receiving error: " + ex.getMessage()); }
    }

    void receiveMessage(String message) throws IOException {
        out.writeBytes(message);
        out.flush();
    }

    public void printMessage() throws IOException {

        if(!actions) {
            System.out.println(message);
            chatserver.broadcast(name + ": " + message + "\n");
        }
    }

    public void statusOn() throws IOException {
        if(message.contains("/status")){
            chatserver.broadcast(name);
        }
    }

    public void changeName() throws IOException {
        if (message.contains("/666")) {
            System.out.print(name + " changes to ");
            chatserver.broadcast(name + " changed his name to ");
            name = message.split("/666")[1];
            System.out.print(name);
            chatserver.broadcast(name);
            actions = true;
        }
    }

    public void quit() throws IOException {
        if (message.contains("/quit")) {
            chatserver.broadcast(name + " left");
            System.out.println("Closing client connection");
            clientSocket.close();
        }

    }

    public void closeClientSocket() {
        try {

            if (clientSocket != null) {

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