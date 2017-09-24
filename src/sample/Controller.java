package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;


import javax.sound.sampled.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    MicRunner runner;
    private FileChooser choser;

    @FXML
    public void stopMicMethod(MouseEvent click)
    {
        runner.runMic = false;
    }
    public void playFile(MouseEvent ev)
    {
        File f = choser.showOpenDialog(getStageWindow(ev));
        if (f != null) {

            try {
                Socket s = new Socket("127.0.0.1", 6666);
                FileInputStream in = new FileInputStream(f);
                if (s.isConnected()) {
                    OutputStream out = s.getOutputStream();

                    byte buffer[] = new byte[2048];
                    int count;
                    while ((count = in.read(buffer)) != -1)
                        out.write(buffer, 0, count);

                    out.close();
                    s.close();
                }

            } catch (Exception ex) {
            } finally {

            }


        }
    }

    @FXML
    public void runMic(MouseEvent e){
        runner = new MicRunner();
        runner.runMic = true;
        runner.start();


    }
    private void configureFileChooser(FileChooser fileChooser)
    {
        fileChooser.setTitle("Play audio");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        String[] formats = {"*.wav", "*.mid"};
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio media files (*.wav, *.mid)", formats);
        fileChooser.getExtensionFilters().add(extFilter);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        choser = new FileChooser();
        configureFileChooser(choser);


    }

    private Window getStageWindow(EventObject ae)
    {
        Node source = (Node) ae.getSource();
        return source.getScene().getWindow();
    }
}
