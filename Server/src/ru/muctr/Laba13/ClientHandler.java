package ru.muctr.Laba13;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private List<String> lines;

    public ClientHandler(Server server, Socket socket, List<String> lines) {
        try {
            this.server = server;
            this.socket = socket;
            this.lines = lines;
            System.out.println(lines.get(0));
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());


            new Thread(() -> {
                try {
                    sendTasks();
                    while (true) {             // бесконечно ожидаем сообщения от клиента
                        String str = in.readUTF();  //в переменную str записываем сообщение от клиента
                        int index = 0;
                        index = Integer.parseInt(lines.get(lines.size() - 1).substring(0, 1));
                        if (str.equals("/end")) {
                            System.out.println("Client disconnected");
                            save();
                            break;
                        }
                        lines.add((index + 1) + ". " + str);
                        server.sendTasks();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendTasks(){
        try {
            out.writeUTF("clear");
            for (String task : lines) {
                out.writeUTF(task);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {             //сохранение списков задач в файлы
        File file1 = new File("Project1.txt");
        try (BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1))){
            Iterator<String> itr = lines.iterator();
            while (itr.hasNext()){
                writer1.write(itr.next());
                writer1.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
