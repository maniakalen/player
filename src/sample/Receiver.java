package sample;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends Thread
{
    InputStream input;
    public static void receive()
    {
        Receiver r = new Receiver();
        r.start();
    }
    @Override
    public void run()
    {
        System.out.println("Listener running");
        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            Socket clientSocket = serverSocket.accept();

            input = clientSocket.getInputStream();
            playFile(input);
        } catch (Exception ex) {

        }
    }
    public static void playFile(InputStream stream)
    {
        try {

            if (stream.available() > 0) {
                System.out.println("File playing");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
