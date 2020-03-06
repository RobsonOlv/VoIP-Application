package teste;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Server extends JFrame {

	private JPanel contentPane;
	public JTextArea textVisor = new JTextArea();
	private JList list1 = new JList();
	static DefaultListModel modelo1 = new DefaultListModel();
	private JList list2 = new JList();
	static DefaultListModel modelo2 = new DefaultListModel();
	public JLabel label_1 = new JLabel("");
	public JLabel label = new JLabel("");
	public boolean conexao1 = true;
	public boolean conexao2 = true;
	public static ServerSocket servsocket3;
	public static ServerSocket servsocket4;
	private static URL url;
	private static URL url2;
	public ImageIcon iconGreen;
	public ImageIcon iconRed;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
			System.err.println(ex);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				int port;//= 6666;
				String ip;
				int port2; //= 6969;
				String ip2;
				int portAudio, portAudio2, portVerify, portVerify2;
				//int portVerify; //= 6668;
				//int portVerify2; //= 6971;
				String aux;
				try {
					Server frame = new Server();
					frame.setVisible(true);
					try {
						frame.url = new URL("https://i.imgur.com/gtBERxp.png");
						frame.url2 = new URL("https://i.imgur.com/6U21WNg.png");
						frame.iconGreen = new ImageIcon(url);
						frame.iconRed = new ImageIcon(url2);
						frame.label.setIcon(frame.iconGreen);
						frame.label_1.setIcon(frame.iconGreen);
				    } catch (MalformedURLException e) {
				        e.printStackTrace();
				    }
					Socket servsocket1 = new Socket();
					DatagramSocket serverSocket = new DatagramSocket(8888);
					byte[] sendData = new byte[1024];
					byte[] receiveData = new byte[1024];
					
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					frame.textVisor.append("Aguardando primeiro cliente.\n");
					String cliente1, cliente2;
					int auxb;
					//System.out.println("Aguardando primeiro cliente.");
					serverSocket.receive(receivePacket);
					ip = receivePacket.getAddress().getHostName();
					cliente1 = new String(receivePacket.getData());
					auxb = cliente1.indexOf(";");
					cliente1 = cliente1.substring(0, auxb);
					String[] test = cliente1.split(" ");
					
					port = Integer.parseInt(test[0]);
					
					portAudio = Integer.parseInt(test[1]);
					portVerify = Integer.parseInt(test[2]);
					modelo1.addElement("IP: " + ip);
					modelo1.addElement("Porta: " + port);
					
					frame.textVisor.append("Cliente1 conectado!\n");
					//aux = new String(receivePacket.getData());
					//audio
					//verify
					
					
					//segundo
					frame.textVisor.append("Aguardando segundo cliente.\n");
					serverSocket.receive(receivePacket);
					ip2 = receivePacket.getAddress().getHostName();
					cliente2 = new String(receivePacket.getData());
					auxb = cliente2.indexOf(";");
					cliente2 = cliente2.substring(0, auxb);
					String[] test2 = cliente2.split(" ");

					port2 = Integer.parseInt(test2[0]);
					portAudio2 = Integer.parseInt(test2[1]);
					portVerify2 = Integer.parseInt(test2[2]);
					
					modelo2.addElement("IP: " + ip2);
					modelo2.addElement("Porta: " + port2);
					frame.textVisor.append("Cliente2 conectado!\n");
					
					//ENVIOS
					//cliente1
					aux = ip2 + " " + cliente2 + ";";
					sendData = aux.getBytes();
					receivePacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
					serverSocket.send(receivePacket);

					
					//CLIENTE2
					aux = ip + " " + cliente1 + ";";
					sendData = aux.getBytes();
					receivePacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip2), port2);
					serverSocket.send(receivePacket);
					/**
					ServerSocket servsocket1 = new ServerSocket(port);
					ServerSocket servsocket2 = new ServerSocket(port2);

					servsocket3 = new ServerSocket(portVerify);
					servsocket4 = new ServerSocket(portVerify2);
					
					System.out.println("Aguardando cliente1.");
					Socket socket1 = servsocket1.accept();
					System.out.println("Cliente1 conectado!");

					System.out.println("Aguardando cliente2.");
					Socket socket2 = servsocket2.accept();
					System.out.println("Cliente2 conectado!");

					DataOutputStream output1 = new DataOutputStream(socket1.getOutputStream());
					output1.writeUTF(Integer.toString(socket2.getLocalPort()));

					DataOutputStream output2 = new DataOutputStream(socket2.getOutputStream());
					output2.writeUTF(Integer.toString(socket1.getLocalPort()));
					Socket socket3 = servsocket3.accept();
					Socket socket4 = servsocket4.accept();

					DataOutputStream outputVerify = new DataOutputStream(socket3.getOutputStream());
					DataOutputStream outputVerify2 = new DataOutputStream(socket4.getOutputStream());**/
					
					Thread servidorVerify = new ServidorVerify(frame, 1, serverSocket, ip, ip2, portVerify2);
					servidorVerify.start();
					Thread servidorVerify2 = new ServidorVerify(frame, 2, serverSocket, ip, ip2, portVerify);
					servidorVerify2.start();
					String rps;
					int index;
					System.out.println("PORTA ANTES DE ENTRAR : " + port);
					Thread verificarSaida = new VerificarSaida(frame, serverSocket, port, portAudio, port2, portAudio2, ip, ip2);
					verificarSaida.start();
				}  catch (BindException e) {
					System.out.println("Endereco em uso");
				} catch (Exception e) {
					System.out.println("Error: " + e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		setTitle("ChatBox (Server)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 204, 153));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 11, 314, 239);
		contentPane.add(scrollPane);
		
		textVisor.setBackground(new Color(255, 250, 205));
		textVisor.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textVisor.setFont(new Font("Georgia", Font.PLAIN, 14));
		scrollPane.setViewportView(textVisor);
		
		JLabel lblCliente = new JLabel("Cliente 1");
		lblCliente.setFont(new Font("Georgia", Font.ITALIC, 13));
		lblCliente.setBounds(329, 11, 65, 14);
		contentPane.add(lblCliente);
		
		JLabel lblCliente_1 = new JLabel("Cliente 2");
		lblCliente_1.setFont(new Font("Georgia", Font.ITALIC, 13));
		lblCliente_1.setBounds(329, 142, 65, 14);
		contentPane.add(lblCliente_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(329, 26, 95, 80);
		contentPane.add(scrollPane_1);
		scrollPane_1.setViewportView(list1);
		
		list1.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 13));
		list1.setForeground(new Color(255, 255, 255));
		list1.setBackground(new Color(0, 102, 51));
		list1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list1.setModel(modelo1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(329, 157, 95, 79);
		contentPane.add(scrollPane_2);
		scrollPane_2.setViewportView(list2);
		
		list2.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 13));
		list2.setForeground(new Color(255, 255, 255));
		list2.setBackground(new Color(0, 102, 51));
		list2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list2.setModel(modelo2);
		label_1.setBounds(389, 143, 15, 15);
		contentPane.add(label_1);
		
		label.setBounds(389, 11, 15, 15);
		contentPane.add(label);
	}
}
