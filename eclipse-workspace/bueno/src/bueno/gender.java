package bueno;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class gender extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gender frame = new gender();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public gender() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setBounds(58, 222, 101, 27);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnEnter);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(238, 222, 101, 27);
		contentPane.add(btnClear);
		
		textField = new JTextField();
		textField.setBounds(159, 37, 114, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(58, 39, 58, 17);
		contentPane.add(lblName);
		
		JLabel lblSection = new JLabel("Section:");
		lblSection.setBounds(58, 68, 58, 17);
		contentPane.add(lblSection);
		
		JLabel lblAge = new JLabel("Age:");
		lblAge.setBounds(58, 97, 58, 17);
		contentPane.add(lblAge);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(159, 70, 114, 21);
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(159, 103, 114, 21);
		contentPane.add(textField_2);
		
		JPanel panel = new JPanel();
		panel.setBounds(58, 147, 216, 47);
		contentPane.add(panel);
	}
}
