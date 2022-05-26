package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final int PORT = 8189;
    private Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private  final String SERVER_ADDR = "localhost";

    @FXML
    public VBox vbox;
    @FXML
    public TextField deleteTextField;

    @FXML
    public Button btnAdd;
    @FXML
    public Button btnDel;
    @FXML
    public Button btnTopic1;
    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField1;


    @FXML
    public void initialize(URL location, ResourceBundle resources){     //метод срабатывает при запуске приложения
        try{
            socket = new Socket(SERVER_ADDR, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(()->{
                try{
                    while (true){
                        String str = in.readUTF();
                        if(str.equals("clear")){
                            textArea.clear();
                        }else {
                            textArea.appendText(str + "\n\n");
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try{
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void clickBtnAdd(ActionEvent actionEvent) {   //обработка нажатия кнопки "Добавить задачу"
        textField1.setVisible(true);
        textField1.requestFocus();
    }

    @FXML
    public void clickBtnDel(ActionEvent actionEvent) {  //обработка нажатия кнопки "Удалить"
        deleteTextField.setVisible(true);
        deleteTextField.requestFocus();
    }

    @FXML
    public void textFieldClick(ActionEvent actionEvent) {  //обработка нажатия Enter на textField (id "textField") при добавлении новой задачи
        try {
            out.writeUTF(textField1.getText());
            textField1.clear();
            textField1.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteTask(ActionEvent actionEvent) {       //обработка нажатия Enter на textField (id "deleteTextField") при удалении задачи из списка
        String number = deleteTextField.getText();
        List<String> tasks = null;
            //tasks = topic1Tasks;

        for (String task:tasks) {
            if (task.substring(0,1).equals(number)) {
                tasks.remove(task);
            }
        }
        textArea.clear();
        for (String task:tasks) {
            textArea.appendText(task);
            textArea.appendText("\n\n");
        }
        deleteTextField.clear();
        deleteTextField.setVisible(false);
    }
}
