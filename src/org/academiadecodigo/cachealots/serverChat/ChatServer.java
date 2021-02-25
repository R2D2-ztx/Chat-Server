package org.academiadecodigo.cachealots.serverChat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static ServerSocket serverSocket;
    public Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;
    private int port;
    private LinkedList<ClientDispatcher> clientList;

    public String[] name = { "DiguinCaçador", "OBrabo", "MrsNobody", "Cool", "MoreColl", "Mary",
            "OSafado", "Stripper", "SeuPai", "arrombado", "DuckNoris","bolaAmarela", "Achei", "PeteSelvas"};



    public ChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {

        ChatServer chatServer = new ChatServer(9000);
        chatServer.dispatch(serverSocket);
    }

    public void dispatch(ServerSocket serverSocket) throws IOException {

        System.out.println("Server is listening...on port " + port + "\n");
        System.out.println("---------------------------------------\n" +
                           "  To change your name use: 666newName  \n" +
                           "---------------------------------------\n");

        serverSocket = new ServerSocket(9000);
        clientList = new LinkedList<>();
        // A pool of 7 fixed threads
        ExecutorService fixedPool = Executors.newFixedThreadPool(7);
        Socket clientSocket;
        int i = 0;

        while (true) {

            clientSocket = serverSocket.accept();

            ClientDispatcher client = new ClientDispatcher(clientSocket,this,name[i]);
            clientList.add(client);
            fixedPool.submit(client);
            System.out.println(name[i] + " Has Logged inside the matrix");
            i++;

        }
    }
    public void broadcast(String message) throws IOException {
        for(ClientDispatcher client : clientList ){
            client.receiveMessage(message);
        }
    }


}
