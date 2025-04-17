package venedict_1;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import java.awt.Checkbox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel tx;
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JLabel lbWelcome;
	private JTextField txfmiddlename;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
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
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		tx = new JPanel();
		tx.setBackground(Color.GRAY);
		tx.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(tx);
		tx.setLayout(null);
		
		tfFirstName = new JTextField();
		tfFirstName.setBounds(197, 10, 192, 30);
		tx.add(tfFirstName);
		tfFirstName.setColumns(10);
		
		JLabel lbfirstname = new JLabel("First name : ");
		lbfirstname.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
		lbfirstname.setBounds(33, 10, 222, 30);
		tx.add(lbfirstname);
		
		JLabel lblastname = new JLabel("Last name : ");
		lblastname.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
		lblastname.setBounds(33, 51, 222, 34);
		tx.add(lblastname);
		
		tfLastName = new JTextField();
		tfLastName.setColumns(10);
		tfLastName.setBounds(197, 51, 192, 34);
		tx.add(tfLastName);
		
		JButton btnconfirm = new JButton("Confirm");
		btnconfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstName = tfFirstName.getText();
				String lastName = tfLastName.getText();
				String middlename = txfmiddlename.getText();
				lbWelcome.setText("Welcome  " + firstName + " " + lastName + " " + middlename);
			}
		});
		btnconfirm.setBounds(54, 181, 100, 30);
		tx.add(btnconfirm);
		
		JButton btnReset = new JButton("Clear");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfFirstName.setText("");
				tfLastName.setText("");
				txfmiddlename.setText("");
				lbWelcome.setText("");
				
			}
		});
		btnReset.setBounds(268, 181, 100, 30);
		tx.add(btnReset);
		
	    lbWelcome = new JLabel("");
	    lbWelcome.setFont(new Font("Verdana", Font.BOLD, 16));
		lbWelcome.setBounds(10, 136, 414, 34);
		tx.add(lbWelcome);
		
		JLabel lblMiddleName = new JLabel("Middle Name :");
		lblMiddleName.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
		lblMiddleName.setBounds(33, 91, 222, 34);
		tx.add(lblMiddleName);
		
		txfmiddlename = new JTextField();
		txfmiddlename.setColumns(10);
		txfmiddlename.setBounds(197, 91, 192, 34);
		tx.add(txfmiddlename);
	}
}
