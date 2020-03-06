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

public class Receber extends Thread {
	private DatagramSocket input;
	private int cont = 1;
	private String msg;
	private Cliente1 conexao1;
	private Cliente2 conexao2;

	public Receber(DatagramSocket in, Cliente1 conexao1, Cliente2 conexao2) {
		this.input = in;
		this.conexao1 = conexao1;
		this.conexao2 = conexao2;
	}

	public void run() {
		if (conexao2 == null) {
			while(true) {
				byte[] data = new byte[1024];
				DatagramPacket pacoteReceiv = new DatagramPacket(data, data.length);
				try {
					this.input.receive(pacoteReceiv);
					this.msg = new String(pacoteReceiv.getData());

					int index = msg.indexOf(";");
					this.msg = msg.substring(0, index);

					if (cont == 1) {
						this.conexao1.enviarmsg(10, this.msg, this.input);
						this.cont--;
					} else {
						this.conexao1.enviarmsg(1, msg, this.input);
					}

				} catch (IOException e) {
					System.out.println("Erro ao receber: " + e);
				}
			}
		} else {
			while(true) {
				byte[] data = new byte[1024];
				DatagramPacket pacoteReceiv = new DatagramPacket(data, data.length);
				try {
					this.input.receive(pacoteReceiv);
					this.msg = new String(pacoteReceiv.getData());

					int index = msg.indexOf(";");
					this.msg = msg.substring(0, index);

					if (cont == 1) {
						this.conexao2.enviarmsg(10, this.msg, this.input);
						this.cont--;
					} else {
						this.conexao2.enviarmsg(1, msg, this.input);
					}

				} catch (IOException e) {
					System.out.println("Erro ao receber: " + e);
				}
			}
		}
	}
}
