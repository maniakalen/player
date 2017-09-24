package sample;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.DataOutputStream;
import java.net.Socket;

public class MicRunner extends Thread {
    boolean runMic = true;

    @Override
    public void run()
    {
        try{
            Socket s = new Socket("127.0.0.1", 6666);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            AudioFormat form = getAudioFormat();
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, form);
            TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
            mic.open(form);
            System.out.println("Mic open.");
            byte tmpBuff[] = new byte[mic.getBufferSize()/5];
            mic.start();
            while(runMic) {
                System.out.println("Reading from mic.");
                int count = mic.read(tmpBuff,0,tmpBuff.length);
                if (count > 0){
                    for (byte b : tmpBuff)
                        System.out.println("Buffer: " + b);
                    System.out.println("Writing buffer to server.");
                    out.write(tmpBuff, 0, count);
                }
            }
            mic.drain();
            mic.close();
            System.out.println("Stopped listening from mic.");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, true, false);
    }
}
