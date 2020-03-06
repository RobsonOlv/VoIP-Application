package teste;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class VerificarSaida extends Thread {
	private DatagramSocket serverSocket;
	private Server server;
	private int port1;
	private int port2;
	private int portAudio1;
	private int portAudio2;
	private String ip;
	private String ip2;

	public VerificarSaida(Server server, DatagramSocket serverSocket, int port, int portAudio1, int port2,
			int portAudio2, String ip, String ip2) {
		this.serverSocket = serverSocket;
		this.server = server;
		this.port1 = port;
		this.port2 = port2;
		this.portAudio1 = portAudio1;
		this.portAudio2 = portAudio2;
		this.ip = ip;
		this.ip2 = ip2;
	}

	public void run(){
		System.out.println("CHEGUEI AQUI");
		String tudo;
		String rps;
		int index;
		byte[] send = new byte[1024];
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		while(true) {
			try {
				serverSocket.receive(receivePacket);
				rps = new String(receivePacket.getData());
				index = rps.indexOf(";");
				rps = rps.substring(0, index);
				System.out.println(rps + ";");
				if(rps.charAt(0) == 'e') {
					System.out.println("CHEGUEI AGQWEQ");
					if(receivePacket.getPort() == port1) {
						server.textVisor.append("Cliente1 encerrou sua conexão.\n");
						server.label.setIcon(server.iconRed);
						server.conexao1 = false;
						serverSocket.receive(receivePacket);
						tudo = new String(receivePacket.getData());
						index = tudo.indexOf(";");
						tudo = tudo.substring(0, index);
						String[] dados = tudo.split(" ");
						port1 = Integer.parseInt(dados[0]);
						server.textVisor.append("Aguardando primeiro cliente.\n");
						ip = receivePacket.getAddress().getHostName();
						server.textVisor.append("Cliente1 conectado!\n");
						//ENVIOS
						//IP
						String cliente2 = "" + ip2 + " " + Integer.toString(port2) + " " + Integer.toString(portAudio2) + ";";
						send = cliente2.getBytes();
						System.out.println("IP : " + ip);
						System.out.println("Porta : " + port1);
						receivePacket = new DatagramPacket(send, send.length, InetAddress.getByName(ip), port1);
						System.out.println("ENVIANDO NOVAMENTE");
						serverSocket.send(receivePacket);
						System.out.println("ENVIEI NOVAMENTE");
						server.label.setIcon(server.iconGreen);
					} else {
						server.textVisor.append("Cliente2 encerrou sua conexão.\n");
						server.label_1.setIcon(server.iconRed);
						server.conexao2 = false;
						serverSocket.receive(receivePacket);
						tudo = new String(receivePacket.getData());
						index = tudo.indexOf(";");
						tudo = tudo.substring(0, index);
						String[] dados = tudo.split(" ");
						port2 = Integer.parseInt(dados[0]);
						server.textVisor.append("Aguardando segundo cliente.\n");
						ip2 = receivePacket.getAddress().getHostName();
						server.textVisor.append("Cliente2 conectado!\n");
						
						//ENVIOS
						String cliente1 = "" + ip + " " + Integer.toString(port1) + " " + Integer.toString(portAudio1) + ";";
						send = cliente1.getBytes();
						receivePacket = new DatagramPacket(send, send.length, InetAddress.getByName(ip2), port2);
						serverSocket.send(receivePacket);
						server.label_1.setIcon(server.iconGreen);
					}
				}
			} catch (BindException e) {
				System.out.println("Endereco em uso");
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}
}
