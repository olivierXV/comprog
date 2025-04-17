package venedict_1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class fahrenheit extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtype;
	private final JComboBox comboBox = new JComboBox();
	private JLabel lblresulat;
	private JButton btnclear_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fahrenheit frame = new fahrenheit();
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
	public fahrenheit() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 318);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtype = new JTextField();
		txtype.setBounds(19, 11, 405, 45);
		contentPane.add(txtype);
		txtype.setColumns(10);
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 17));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Fahrenheit", "Celsius"}));
		comboBox.setBounds(19, 67, 405, 45);
		contentPane.add(comboBox);
		
		JButton btnENTER = new JButton("ENTER");
		btnENTER.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double convert, txtyp1e = Double.parseDouble(txtype.getText());
				String calra = (String) comboBox.getSelectedItem();
				
				if (calra == "Celsius") {
					
					convert = (txtyp1e * 9/5) + 32;
					lblresulat.setText(convert+"°C");
				}else {
					convert = (txtyp1e - 32)* 5/9;
					lblresulat.setText(convert+"°F");
				}
				
			}
			});
		btnENTER.setBounds(29, 181, 162, 45);
		contentPane.add(btnENTER);
		
		JButton btnclear = new JButton("CLEAR");
		btnclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtype.setText(null);
				lblresulat.setText(null);
				
			}
		});
		btnclear.setBounds(251, 181, 162, 45);
		contentPane.add(btnclear);
		
		lblresulat = new JLabel("");
		lblresulat.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblresulat.setBounds(19, 123, 405, 45);
		contentPane.add(lblresulat);
		
		btnclear_1 = new JButton("Back");
		btnclear_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainmenu main = new mainmenu();
				main.setVisible(true);
				dispose();
			}
		});
		btnclear_1.setBounds(134, 230, 162, 45);
		contentPane.add(btnclear_1);
	}
}
