package engine.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import engine.logger.Logger;

public class Window extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1996551126781267895L;
	
	private JTextField inputTextField;
	private JButton sendButton;
	private JTextPane displayTextPane;
	private JLabel titleLabel;
	
	private Logger logger;

	public Window(Logger logger) {
		this.logger = logger;
		this.font = new Font("Lucida Console", Font.PLAIN, 12);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setSize(400, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(3, 3, 5, 3));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(0, 3, 0, 3));
		mainPanel.add(inputPanel, BorderLayout.SOUTH);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		
		inputTextField = new JTextField();
		inputPanel.add(inputTextField);
		inputTextField.setColumns(10);
		
		Component inputSpacer = Box.createHorizontalStrut(5);
		inputPanel.add(inputSpacer);
		
		sendButton = new JButton("Send");
		inputPanel.add(sendButton);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
		mainPanel.add(displayPanel, BorderLayout.CENTER);
		displayPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane displayScrollPane = new JScrollPane();
		displayPanel.add(displayScrollPane);
		displayScrollPane.setViewportBorder(new EmptyBorder(2, 2, 2, 2));
		displayScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		displayTextPane = new JTextPane();
		displayScrollPane.setViewportView(displayTextPane);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBorder(new CompoundBorder(new EmptyBorder(2, 3, 2, 3), new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new EmptyBorder(1, 1, 1, 1))));
		mainPanel.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		
		titleLabel = new JLabel("Game Title");
		titleLabel.setFont(new Font("Nyala", Font.BOLD, 21));
		titlePanel.add(titleLabel);
		
		sendButton.addActionListener(this);
		inputTextField.addActionListener(this);
		
		this.addWindowListener(this);
	}
	
//	-------------------------------------------------- Frame Management ------------------------------------------------------ \\
	public void setTitle(String title) {
		if(titleLabel.getIcon() == null)
			titleLabel.setText(title);
		super.setTitle(title);
	}
	
	public void setTitleImage(URL image) {
		titleLabel.setText("");
		titleLabel.setIcon(new ImageIcon(image));
	}
	
//	--------------------------------------------------- Input / Output ------------------------------------------------------- \\
	public void actionPerformed(ActionEvent e) {
		logger.addInput(inputTextField.getText());
		inputTextField.setText("");
	}

	private Font font;
	public void setFont(Font font) { this.font = font; }
	
	public void addString(String text, Color color) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, font.getFamily());
        aset = sc.addAttribute(aset, StyleConstants.FontSize, font.getSize());
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = displayTextPane.getDocument().getLength();
        displayTextPane.setCaretPosition(len);
        displayTextPane.setCharacterAttributes(aset, false);
        displayTextPane.replaceSelection(text + "\n");
	}

	public void windowClosing(WindowEvent e) {
		logger.closeLogger();
	}
	
// ------------------------------------------------- Unused Events ---------------------------------------------------------- \\
	
	public void windowOpened(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
}
