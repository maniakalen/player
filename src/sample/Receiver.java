package sample;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends Thread
{
    private Socket clientSocket;
    static void receive()
    {
        Receiver r = new Receiver();
        r.start();
    }
    @Override
    public void run()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            clientSocket = serverSocket.accept();
            playRawSound(clientSocket.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void playFile(InputStream stream)
    {
        try {

            //if (stream.available() > 0) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            //}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playRawSound(InputStream input)
    {
        try {
            AudioFormat af = MicRunner.getAudioFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            byte[] buffer = new byte[4096];

            line.open(af, 4096);
            line.start();
            int nRead;
            while (clientSocket.isConnected() && (nRead = input.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, nRead);
                buffer = new byte[4096];
            }
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
