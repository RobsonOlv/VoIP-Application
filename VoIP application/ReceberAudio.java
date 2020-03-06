package teste;

import java.io.*;
import java.util.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class ReceberAudio extends Thread {
	private DatagramSocket input;
	private Cliente1 conexao1;
	private Cliente2 conexao2;
	
	public ReceberAudio(DatagramSocket in, Cliente1 conexao1, Cliente2 conexao2) {
		this.input = in;
		this.conexao1 = conexao1;
		this.conexao2 = conexao2;
	}
	
	public void run(){
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
	    SourceDataLine speakers;
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    int numBytesRead;
	    int CHUNK_SIZE = 1024;
	    int bytesRead = 0;
	    
		if(conexao2 == null) {
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
			try {
				speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			    speakers.open(format);
			    speakers.start();
	
				byte[] receiveData = new byte[1024];
				byte[] header = new byte[12];
				byte[] aux;
		    	while(true) {
		    		byte[] buffer = new byte[1024];
					DatagramPacket pacoteReceiv = new DatagramPacket (buffer, buffer.length);
					this.input.receive(pacoteReceiv);
					aux = pacoteReceiv.getData();
					byte[] payload = new byte[pacoteReceiv.getData().length - 12];
					
					for(int i = 12; i < aux.length; i++) {
						payload[i - 12] = aux[i];
					}
					
					out.write(payload, 0, payload.length);
		            speakers.write(payload, 0, payload.length);
		    	}
			} catch (SocketTimeoutException ex) {
		        System.out.println("Timeout error: " + ex.getMessage());
		        ex.printStackTrace();
		    } catch (IOException ex) {
		        System.out.println("Client error: " + ex.getMessage());
		        ex.printStackTrace();
		    } catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
			try {
				speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			    speakers.open(format);
			    speakers.start();
	
				byte[] receiveData = new byte[1024];
				byte[] header = new byte[12];
				byte[] aux;
				
		    	while(true) {
		    		byte[] buffer = new byte[1024];
					DatagramPacket pacoteReceiv = new DatagramPacket (buffer, buffer.length);
					this.input.receive(pacoteReceiv);
					aux = pacoteReceiv.getData();
					byte[] payload = new byte[pacoteReceiv.getData().length - 12];
					
					for(int i = 12; i < aux.length; i++) {
						payload[i - 12] = aux[i];
					}
					
					out.write(payload, 0, payload.length);
		            speakers.write(payload, 0, payload.length);
		    	}
			} catch (SocketTimeoutException ex) {
		        System.out.println("Timeout error: " + ex.getMessage());
		        ex.printStackTrace();
		    } catch (IOException ex) {
		        System.out.println("Client error: " + ex.getMessage());
		        ex.printStackTrace();
		    } catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
