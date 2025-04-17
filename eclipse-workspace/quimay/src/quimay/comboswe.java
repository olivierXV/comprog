package quimay;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class comboswe extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel lblresulat;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					comboswe frame = new comboswe();
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
	public comboswe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		lblresulat = new JPanel();
		lblresulat.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(lblresulat);
		lblresulat.setLayout(null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Montserrat ExtraBold", Font.BOLD, 18));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Celsius to Fahrenheit", "Fahrenheit to Celsius"}));
		comboBox.setBounds(46, 53, 322, 62);
		lblresulat.add(comboBox);
		
		textField = new JTextField();
		textField.setBounds(46, 127, 160, 49);
		lblresulat.add(textField);
		textField.setColumns(10);
		
		JButton btnsubmit = new JButton("Submit");
		btnsubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cell = (String) comboBox.getSelectedItem();
			}
		});
		btnsubmit.setBounds(244, 138, 101, 27);
		lblresulat.add(btnsubmit);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(46, 191, 160, 49);
		lblresulat.add(lblNewLabel);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(244, 202, 101, 27);
		lblresulat.add(btnClear);
	}
}
