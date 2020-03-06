package teste;

import java.io.*;
import java.util.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

public class Enviar extends Thread {
	private DatagramSocket socket;
	private String mensagem;
	private int portaTexto;
	private int portaAudio;
	private InetAddress endereco;
	
	public Enviar(String mensagem, DatagramSocket socket, int portaTexto, int portaAudio, String endereco) throws UnknownHostException {
		this.mensagem = mensagem;
		this.portaTexto = portaTexto;
		this.portaAudio = portaAudio;
		this.socket = socket;
		this.endereco = InetAddress.getByName(endereco);
	}
	
	public void run() {
		if(mensagem != "") {
			byte[] send = this.mensagem.getBytes();
			DatagramPacket pacote = new DatagramPacket(send, send.length, this.endereco, this.portaTexto);
			try {
				this.socket.send(pacote);
			} catch(IOException e) {
				System.out.println("Falha ao enviar o pacote :" + e);
			}
		} else {
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
			TargetDataLine microphone;
			
			try {
				microphone = AudioSystem.getTargetDataLine(format);

				DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
				microphone = (TargetDataLine) AudioSystem.getLine(info);
				microphone.open(format);
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int numBytesRead;
				int CHUNK_SIZE = 1024;
				byte[] data = new byte[microphone.getBufferSize() / 5];
				microphone.start();
				
				DatagramSocket socket2 = new DatagramSocket();
				byte[] buffer = new byte[1024];
				
				
				int seqNumber = (int) Math.random();
				int timeStamp = (int) Math.random();
				int ssrc = (int) Math.random();
				
				while(true) {
					//criando cabecalho
					byte[] header = new byte[12];
					
					header[0] = (byte)(2 << 6 | 0 << 5 | 0 << 4 | 0);
					header[1] = (byte)(0 << 7 | 0);
					
					header[2] = (byte)(seqNumber >> 8);
					header[3] = (byte)(seqNumber);
					seqNumber++;
					
					header[4] = (byte)(timeStamp >> 24);
					header[5] = (byte)(timeStamp >> 16);
					header[6] = (byte)(timeStamp >> 8);
					header[7] = (byte)(timeStamp);
					timeStamp += 8000;
					
					header[8] = (byte)(ssrc >> 24);
					header[9] = (byte)(ssrc >> 16);
					header[10] = (byte)(ssrc >> 8);
					header[11] = (byte)(ssrc);
					
					
					
					numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
					byte[] payload = data; //pegando somente audio
					
					int maxLength = header.length  + payload.length;
					byte[] send = new byte[maxLength];//array de audio + cabecalho
					
					//preenchendo o array send com header e payload
					for(int i = 0; i < header.length; i++) {
						send[i] = header[i];
					}
					
					for(int i = header.length; i < send.length; i++) {
						send[i] = payload[i - header.length];
					}
					
					
					DatagramPacket pacote = new DatagramPacket(send, send.length, endereco, this.portaAudio);
					try {
						this.socket.send(pacote);
					} catch(IOException e) {
						System.out.println("Falha ao enviar o pacote :" + e);
					}
					//sourceLine.write(test, 0, numBytesRead);
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}
