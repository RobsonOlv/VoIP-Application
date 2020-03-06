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
import javax.sound.sampled.DataLine;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.JScrollBar;
import java.awt.SystemColor;
import java.awt.ScrollPane;
import java.awt.Window.Type;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Cliente1 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblUsuarios;
	private JTextArea textVisor;
	private JButton btnEnviar;
	private JTextArea txtMensagem;
	private JScrollPane scroll;
	private JList list;
	static DefaultListModel modelo = new DefaultListModel();
	private int contador = 2;
	private String meuNome;
	private String seuNome;
	private String enderecoDestino;
	private int portaDestino;
	private int portaDestinoAudio;
	private DatagramSocket socketEntradaText;
	private DatagramSocket socketEntradaAudio;
	private DatagramSocket socketEntradaVerify;
	private String debug = "testando";
	public boolean verify;
	private String host = "G3C23";
	
	private ArrayList <String> buffer = new ArrayList();
	public int aux = 1;
	private JButton btnSair;

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
				try {
					String a;
					int portText, portVerify;
					int portAudio;
					int index;
					String address;
					Cliente1 frame = new Cliente1();
					frame.setVisible(true);
					byte[] sendData = new byte[1024];
					byte[] receiveData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					String tudo = "" + "6666" + " " + "6667" + " " + "6668" + ";";
					sendData = tudo.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(frame.host), 8888);
					frame.socketEntradaText.send(sendPacket);
					
					String cliente2;
					frame.socketEntradaText.receive(receivePacket);
					cliente2 = new String (receivePacket.getData());
					index = cliente2.indexOf(";");
					cliente2 = cliente2.substring(0, index);
					String[] test = cliente2.split(" ");
					//pegando IP
					frame.enderecoDestino = test[0];
					//Pegando porta texto
					frame.portaDestino = Integer.parseInt(test[1]);					
					//pegando porta audio
					frame.portaDestinoAudio = Integer.parseInt(test[2]);
					
					frame.textVisor.append("Iniciando conexão com o Servidor ...\n");
					
					
					//Socket socket = new Socket(address, portText);
					//Socket socket2 = new Socket(address, portVerify);
					//DataInputStream input = new DataInputStream(socket.getInputStream());
					//frame.input2 = new DataInputStream(socket2.getInputStream());
					//String port2 = input.readUTF();
					//int newPort = Integer.parseInt(port2);

					frame.textVisor.append("A porta do cliente2 é: " + frame.portaDestino + "\n");
					//socket.close();
					frame.textVisor.append("Digite seu nome.\n");
					// recebimento
					
					Thread saidaAudio = new Enviar("", frame.socketEntradaAudio, frame.portaDestino, frame.portaDestinoAudio, frame.enderecoDestino);
					saidaAudio.start();
					
					Thread entradaText = new Receber(frame.socketEntradaText, frame, null);
					entradaText.start();
					
					Thread entradaAudio = new ReceberAudio(frame.socketEntradaAudio, frame, null);
					entradaAudio.start();
					
					Thread entradaVerify = new VerificarConexao(frame.socketEntradaVerify, frame, null);
					entradaVerify.start();
					// tratando envio
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
		modelo.addElement(usuario);
	}

	public Cliente1() throws SocketException {
		socketEntradaText = new DatagramSocket(6666);
		socketEntradaAudio = new DatagramSocket(6667);
		socketEntradaVerify = new DatagramSocket(6668);
		setResizable(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setBackground(new Color(0, 0, 0));
		setTitle("ChatBox (Cliente 1)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 409, 360);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 204, 153));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblUsuarios = new JLabel("Usuarios");
		lblUsuarios.setFont(new Font("Georgia", Font.ITALIC, 13));
		lblUsuarios.setBounds(326, 11, 65, 14);
		contentPane.add(lblUsuarios);

		textVisor = new JTextArea();
		textVisor.setBackground(new Color(255, 255, 204));
		textVisor.setFont(new Font("Georgia", Font.PLAIN, 14));
		textVisor.setBorder(
				new SoftBevelBorder(BevelBorder.LOWERED, UIManager.getColor("InternalFrame.activeTitleBackground"),
						new Color(255, 0, 0), new Color(105, 105, 105), new Color(0, 0, 0)));
		textVisor.setEditable(false);
		textVisor.setBounds(10, 27, 285, 240);

		// contentPane.add(textVisor);

		txtMensagem = new JTextArea();
		txtMensagem.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtMensagem.setToolTipText("Digite aqui sua mensagem");
		txtMensagem.setBounds(10, 281, 296, 29);
		contentPane.add(txtMensagem);
		enviarmsg(0, "", socketEntradaText);

		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEnviar.setBorder(UIManager.getBorder("Button.border"));
		btnEnviar.setBackground(new Color(153, 153, 153));
		btnEnviar.setBounds(316, 278, 75, 33);
		contentPane.add(btnEnviar);
		pressionarbotao(0, "", socketEntradaText);

		scroll = new JScrollPane();
		scroll.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(10, 26, 296, 241);
		scroll.setViewportView(textVisor);
		contentPane.add(scroll);

		list = new JList();
		list.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 13));
		list.setForeground(new Color(255, 255, 255));
		list.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		list.setBackground(new Color(0, 102, 51));
		list.setBounds(316, 26, 77, 241);
		contentPane.add(list);
		list.setModel(modelo);
		
		btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnSair.setBorder(UIManager.getBorder("Button.border"));
		btnSair.setBackground(new Color(153, 153, 153));
		btnSair.setBounds(105, 0, 110, 25);
		contentPane.add(btnSair);
		pressionarSair(socketEntradaText);
	}

	public void enviarmsg(int aux, String msg, DatagramSocket socket) {
		if (aux == 10) {
			preencherUsuarios(msg);
			seuNome = "(" + msg + ") : ";
		} else if (aux == 1) {
			textVisor.append(seuNome + msg + "\n");
		} else if (aux == 2) {
			if(buffer.size() > 0) {
				for(int i = 0; i < buffer.size(); i++) {
					try {
						Thread saida = new Enviar(buffer.get(i), socket, portaDestino, portaDestinoAudio, enderecoDestino);
						saida.start();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				}
				buffer.clear();
			}
		}

		txtMensagem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// txtrDigiteAqui.getText()
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String mensg;
					if (contador == 2) {
						meuNome = txtMensagem.getText().substring(0, txtMensagem.getText().length() - 1);
						preencherUsuarios(meuNome);
						textVisor.append("Seja bem vindo ao chat, " + meuNome + ". Divirta-se!\n");
						meuNome = "(" + meuNome + ") : ";
						contador--;
					}
					if(txtMensagem.getText().length() != 0) {
						mensg = txtMensagem.getText().substring(0, txtMensagem.getText().length() - 1) + ";";
						if(verify) {
							try {
								Thread saida = new Enviar(mensg, socket, portaDestino, portaDestinoAudio, enderecoDestino);
								saida.start();
							} catch (UnknownHostException e1) {
								textVisor.append("Sua conexão foi perdida e a mensagem não pôde ser enviada. Por favor, aguarde reconectar-se.");
							}
							
							if(contador == 0) {
								textVisor.append(meuNome + txtMensagem.getText());
							} else {
								contador --;
							}
						} else {
							buffer.add(mensg);
						}
					}
					txtMensagem.setText("");
				}
			}
		});
	}
	public void pressionarbotao(int aux, String msg, DatagramSocket socket) {
		btnEnviar.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				String mensg;
				if (contador == 2) {
					meuNome = txtMensagem.getText();
					preencherUsuarios(meuNome);
					textVisor.append("Seja bem vindo ao chat, " + meuNome + ". Divirta-se!\n");
					meuNome = "(" + meuNome + ") : ";
					contador--;
				}
				if(txtMensagem.getText().length() != 0) {
					mensg = txtMensagem.getText() + ";";
					try {
						Thread saida = new Enviar(mensg, socket, portaDestino, portaDestinoAudio, enderecoDestino);
						saida.start();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
					
					if(contador == 0) {
						textVisor.append(meuNome + txtMensagem.getText() + "\n");
					} else {
						contador --;
					}
				}
				txtMensagem.setText("");
			}
		});
	}
	public void pressionarSair(DatagramSocket socket) {
		btnSair.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					String exit = "exit" + ";";
					byte[] send = new byte[1024];
					send = exit.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(send, send.length, InetAddress.getByName(host), 8888);
					socketEntradaText.send(sendPacket);
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
