package teste;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorVerify extends Thread {
	private DatagramSocket serverSocket;
	private String ip;
	private String ip2;
	private int portVerify;
	private String verify;
	private int aux;
	private String port;
	private int num = 1;
	private Server server;

	public ServidorVerify(Server server, int number, DatagramSocket serverSocket, String ip, String ip2, int portVerify) {
		this.serverSocket = serverSocket;
		this.ip = ip;
		this.aux = number;
		this.server = server;
		this.ip2 = ip2;
		this.portVerify = portVerify;
	}

	public void run() {
		byte[] sendData = new byte[1024];
		DatagramPacket sendPacket;
		if (aux == 1) {
			while (true) {
				try {
					if (InetAddress.getByName(ip).isReachable(5000)) {
						server.conexao1 = true;
						if (num == 0) {
							server.textVisor.append("Conexão com cliente " + this.aux + " foi reestabelecida.\n");
							server.label.setIcon(server.iconGreen);
							num = 1;
						}
						verify = "1" + ";";
						if (server.conexao2) {
							sendData = verify.getBytes();
							sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip2), portVerify);
							serverSocket.send(sendPacket);
						}
					} else {
						server.conexao1 = false;
						this.num = 0;
						server.textVisor.append("Conexao perdida com o cliente " + this.aux + ".\n");
						server.label.setIcon(server.iconRed);
						while (!InetAddress.getByName(ip).isReachable(5000)) {
							verify = "0" + ";";
							if(server.conexao2) {
								sendData = verify.getBytes();
								sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip2), portVerify);
								serverSocket.send(sendPacket);
							}
							server.textVisor.append("Aguardando cliente" + this.aux + ".\n");
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		} else {
			while (true) {
				try {
					if (InetAddress.getByName(ip2).isReachable(5000)) {
						server.conexao2 = true;
						if (num == 0) {
							server.textVisor.append("Conexão com cliente " + this.aux + " foi reestabelecida.\n");
							server.label_1.setIcon(server.iconGreen);
							num = 1;
						}
						verify = "1" + ";";
						if(server.conexao1) {
							sendData = verify.getBytes();
							sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), portVerify);
							serverSocket.send(sendPacket);							
						}
					} else {
						server.conexao2 = false;
						this.num = 0;
						server.textVisor.append("Conexao perdida com o cliente " + this.aux + ".\n");
						server.label_1.setIcon(server.iconRed);
						while (!InetAddress.getByName(ip2).isReachable(5000)) {
							verify = "0" + ";";
							if(server.conexao1) {
								sendData = verify.getBytes();
								sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), portVerify);
								serverSocket.send(sendPacket);								
							}
							server.textVisor.append("Aguardando cliente" + this.aux + ".\n");
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
	}
}
