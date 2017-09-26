package sample;

import javax.sound.sampled.*;
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
            AudioFormat form = MicRunner.getAudioFormat();
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, form);
            TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
            mic.open(form);
            byte tmpBuff[] = new byte[mic.getBufferSize()/5];
            mic.start();
            while(runMic) {
                int count = mic.read(tmpBuff,0,tmpBuff.length);
                if (count > 0){
                    out.write(tmpBuff, 0, count);
                }
            }
            mic.drain();
            mic.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



    static AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, true, false);
    }
}
