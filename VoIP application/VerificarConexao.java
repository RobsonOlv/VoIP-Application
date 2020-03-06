package teste;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.SingleSelectionModel;

public class VerificarConexao extends Thread {
	private String verify;
	private Cliente1 conexao1;
	private Cliente2 conexao2;
	private DatagramSocket socket;
	
	public VerificarConexao(DatagramSocket socket, Cliente1 conexao1, Cliente2 conexao2) {
		this.conexao1 = conexao1;
		this.conexao2 = conexao2;
		this.socket = socket;
	}
	
	public void run() {
		char aux;
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		if(conexao2 == null) {
			while(true) {
				try {
					socket.receive(receivePacket);
					this.verify = new String(receivePacket.getData());
					aux = verify.charAt(0);
					if(aux == '1') {
						this.conexao1.verify = true;
						if(this.conexao1.aux == 2) {
							this.conexao1.enviarmsg(2, "", this.socket);
							this.conexao1.aux = 1;
						}
					//	System.out.println("O cliente 2 esta conectado");
					} else {
						this.conexao1.verify = false;
						this.conexao1.aux = 2;
					}
				} catch (IOException e) {
					System.out.println("Erro ao receber: " + e);
				}
			}
		} else {
			while(true) {
				try {
					socket.receive(receivePacket);
					this.verify = new String(receivePacket.getData());
					aux = verify.charAt(0);
					if(aux == '1') {
						this.conexao2.verify2 = true;
						if(this.conexao2.aux == 2) {
							this.conexao2.enviarmsg(2, "", this.socket);
							this.conexao2.aux = 1;
						}
					} else {
						this.conexao2.verify2 = false;
						this.conexao2.aux = 2;
					}
				} catch (IOException e) {
					System.out.println("Erro ao receber: " + e);
				}
			}
		}
	}
}
