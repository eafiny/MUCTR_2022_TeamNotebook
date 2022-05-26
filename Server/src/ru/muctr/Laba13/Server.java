package ru.muctr.Laba13;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final int PORT = 8189;
    private ServerSocket server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private List<String> lines;

    private List<ClientHandler> clients;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        readTasks();
        try {
            server = new ServerSocket(PORT); //в конструктор серверного сокета передаем порт, который он будет слушать
            System.out.println("Server started");
            while (true) {
                socket = server.accept();  //ожидание подключения клиента
                System.out.println("Client connected" + socket.getRemoteSocketAddress());
                clients.add(new ClientHandler(this,socket, lines));
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readTasks() {   //считывание заданий из файлов
        lines  = new CopyOnWriteArrayList<>();
        try {
            File file = new File("Project1.txt");
            if (!file.exists()) {
                System.err.printf("File %s doesn't exist\n", file.getPath());
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = new String();
                    line = reader.readLine();
                    while (line != null) {
                        lines.add(line);
                        line = reader.readLine();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTasks(){
        for(ClientHandler client : clients){
            client.sendTasks();
        }
    }
}
