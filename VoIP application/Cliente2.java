package teste;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.JScrollBar;
import java.awt.SystemColor;
import java.awt.ScrollPane;

public class Cliente2 extends JFrame {

	private JPanel contentPane2;
	private JLabel lblUsuarios2;
	private JTextArea textVisor2;
	private JButton btnEnviar2;
	private JTextArea txtMensagem2;
	private JScrollPane scroll2;
	private JList list2;
	static DefaultListModel modelo2 = new DefaultListModel();
	private int contador = 2;
	private String meuNome2;
	private String seuNome2;
	private String enderecoDestino;
	private int portaDestino2;
	private int portaDestino2Audio;
	private DatagramSocket socketEntrada2Text;
	private DatagramSocket socketEntrada2Audio;
	private DatagramSocket socketEntradaVerify2;
	public boolean verify2;
	private DataInputStream input2;
	private ArrayList<String> buffer = new ArrayList();
	public int aux = 1;
	private JButton btnSair;
	private String host = "G3C23";

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
		// enviarmsg("");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int portText;
					int portVerify;
					int portAudio;
					int index;
					String a;
					String address = "localhost";
					Cliente2 frame = new Cliente2();
					byte[] sendData = new byte[1024];
					byte[] receiveData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					String tudo = "" + "6969" + " " + "6970" + " " + "6971" + ";";
					System.out.println(tudo);
					sendData = tudo.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
							InetAddress.getByName(frame.host), 8888);
					frame.socketEntrada2Text.send(sendPacket);

					String cliente1;
					frame.socketEntrada2Text.receive(receivePacket);
					cliente1 = new String(receivePacket.getData());
					index = cliente1.indexOf(";");
					cliente1 = cliente1.substring(0, index);
					String[] test = cliente1.split(" ");
					// pegando IP
					frame.enderecoDestino = test[0];
					// Pegando porta texto
					frame.portaDestino2 = Integer.parseInt(test[1]);
					// pegando porta audio
					frame.portaDestino2Audio = Integer.parseInt(test[2]);

					frame.textVisor2.append("Iniciando conexão com o Servidor ...\n");
					frame.setVisible(true);
					/*
					 * Socket socket = new Socket(address, portText); Socket socket2 = new
					 * Socket(address, portVerify); DataInputStream input = new
					 * DataInputStream(socket.getInputStream()); frame.input2 = new
					 * DataInputStream(socket2.getInputStream()); String port2 = input.readUTF();
					 * int newPort = Integer.parseInt(port2);
					 */
					frame.textVisor2.append("A porta do cliente1 é: " + frame.portaDestino2 + "\n");
					// socket.close();
					frame.textVisor2.append("Digite seu nome.\n");
					// recebimento

					Thread saidaAudio = new Enviar("", frame.socketEntrada2Audio, frame.portaDestino2,
							frame.portaDestino2Audio, frame.enderecoDestino);
					saidaAudio.start();

					Thread entradaText = new Receber(frame.socketEntrada2Text, null, frame);
					entradaText.start();

					Thread entradaAudio = new ReceberAudio(frame.socketEntrada2Audio, null, frame);
					entradaAudio.start();

					Thread entradaVerify = new VerificarConexao(frame.socketEntradaVerify2, null, frame);
					entradaVerify.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public static void preencherUsuarios(String usuario) {
		modelo2.addElement(usuario);
	}

	public Cliente2() throws SocketException {
		socketEntrada2Text = new DatagramSocket(6969);
		socketEntrada2Audio = new DatagramSocket(6970);
		socketEntradaVerify2 = new DatagramSocket(6971);
		setResizable(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setBackground(new Color(0, 0, 0));
		setTitle("ChatBox (Cliente 2)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 409, 360);
		contentPane2 = new JPanel();
		contentPane2.setBackground(new Color(153, 204, 153));
		contentPane2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane2);
		contentPane2.setLayout(null);

		lblUsuarios2 = new JLabel("Usuarios");
		lblUsuarios2.setFont(new Font("Georgia", Font.ITALIC, 13));
		lblUsuarios2.setBounds(326, 11, 65, 14);
		contentPane2.add(lblUsuarios2);

		textVisor2 = new JTextArea();
		textVisor2.setBackground(new Color(255, 255, 204));
		textVisor2.setFont(new Font("Georgia", Font.PLAIN, 14));
		textVisor2.setBorder(
				new SoftBevelBorder(BevelBorder.LOWERED, UIManager.getColor("InternalFrame.activeTitleBackground"),
						new Color(255, 0, 0), new Color(105, 105, 105), new Color(0, 0, 0)));
		textVisor2.setEditable(false);
		textVisor2.setBounds(10, 27, 285, 240);

		// contentPane.add(textVisor);

		txtMensagem2 = new JTextArea();
		txtMensagem2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtMensagem2.setToolTipText("Digite aqui sua mensagem");
		txtMensagem2.setBounds(10, 281, 296, 29);
		contentPane2.add(txtMensagem2);
		enviarmsg(0, "", socketEntrada2Text);

		btnEnviar2 = new JButton("Enviar");
		btnEnviar2.setBorder(UIManager.getBorder("Button.border"));
		btnEnviar2.setBackground(new Color(153, 153, 153));
		btnEnviar2.setBounds(316, 278, 75, 33);
		contentPane2.add(btnEnviar2);
		pressionarbotao(0, "", socketEntrada2Text);

		scroll2 = new JScrollPane();
		scroll2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll2.setBounds(10, 26, 296, 241);
		scroll2.setViewportView(textVisor2);
		contentPane2.add(scroll2);

		list2 = new JList();
		list2.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 13));
		list2.setForeground(new Color(255, 255, 255));
		list2.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		list2.setBackground(new Color(0, 102, 51));
		list2.setBounds(316, 26, 77, 241);
		contentPane2.add(list2);
		list2.setModel(modelo2);

		btnSair = new JButton("Sair");
		btnSair.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnSair.setBorder(UIManager.getBorder("Button.border"));
		btnSair.setBackground(new Color(153, 153, 153));
		btnSair.setBounds(108, 0, 110, 25);
		contentPane2.add(btnSair);
		pressionarSair(socketEntrada2Text);
	}

	public void enviarmsg(int aux, String msg, DatagramSocket socket) {
		if (aux == 10) {
			preencherUsuarios(msg);
			seuNome2 = "(" + msg + ") : ";
		} else if (aux == 1) {
			textVisor2.append(seuNome2 + msg + "\n");
		} else if (aux == 2) {
			if (buffer.size() > 0) {
				for (int i = 0; i < buffer.size(); i++) {
					try {
						Thread saida = new Enviar(buffer.get(i), socket, portaDestino2, portaDestino2Audio,
								enderecoDestino);
						saida.start();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				}
				buffer.clear();
			}
		}

		txtMensagem2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// txtrDigiteAqui.getText()
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String mensg;
					if (contador == 2) {
						meuNome2 = txtMensagem2.getText().substring(0, txtMensagem2.getText().length() - 1);
						preencherUsuarios(meuNome2);
						textVisor2.append("Seja bem vindo ao chat, " + meuNome2 + ". Divirta-se!\n");
						meuNome2 = "(" + meuNome2 + ") : ";
						contador--;
					}
					if (txtMensagem2.getText().length() != 0) {
						mensg = txtMensagem2.getText().substring(0, txtMensagem2.getText().length() - 1) + ";";
						if (verify2) {
							try {
								Thread saida = new Enviar(mensg, socket, portaDestino2, portaDestino2Audio,
										enderecoDestino);
								saida.start();
							} catch (UnknownHostException e1) {
								textVisor2.append(
										"Sua conexão foi perdida e a mensagem não pôde ser enviada. Pr favor, aguarde reconectar-se.");
							}

							if (contador == 0) {
								textVisor2.append(meuNome2 + txtMensagem2.getText());
							} else {
								contador--;
							}
						} else {
							buffer.add(mensg);
						}
					}
					txtMensagem2.setText("");
				}
			}
		});
	}

	public void pressionarbotao(int aux, String msg, DatagramSocket socket) {
		btnEnviar2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String mensg;
				if (contador == 2) {
					meuNome2 = txtMensagem2.getText();
					preencherUsuarios(meuNome2);
					textVisor2.append("Seja bem vindo ao chat, " + meuNome2 + ". Divirta-se!\n");
					meuNome2 = "(" + meuNome2 + ") : ";
					contador--;
				}
				if (txtMensagem2.getText().length() != 0) {
					mensg = txtMensagem2.getText() + ";";
					try {
						Thread saida = new Enviar(mensg, socket, portaDestino2, portaDestino2, enderecoDestino);
						saida.start();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}

					if (contador == 0) {
						textVisor2.append(meuNome2 + txtMensagem2.getText() + "\n");
					} else {
						contador--;
					}
				}
				txtMensagem2.setText("");
			}
		});
	}

	public void pressionarSair(DatagramSocket socket) {
		btnSair.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					System.out.println("APERTANDO");
					String exit = "exit" + ";";
					byte[] send = new byte[1024];
					send = exit.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(send, send.length, InetAddress.getByName(host),
							8888);
					socketEntrada2Text.send(sendPacket);
					System.exit(0);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}
