package venedict_1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class gradingSystem extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtcomprog;
	private JTextField txtIII;
	private JTextField txtwork;
	private JTextField txtPerdev;
	private JTextField txteapp;
	private JTextField txtCPAR;
	private JTextField txtpe;
	private JLabel lblgrade;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gradingSystem frame = new gradingSystem();
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
	public gradingSystem() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 548);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ComProg :");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(10, 22, 211, 45);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("III :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1.setBounds(10, 68, 211, 45);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("EAPP");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_2.setBounds(10, 221, 211, 45);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Work Immersion :");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_3.setBounds(10, 112, 211, 45);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("PerDev");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_4.setBounds(10, 165, 211, 45);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("CPAR");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_5.setBounds(10, 277, 211, 45);
		contentPane.add(lblNewLabel_5);
		
		txtcomprog = new JTextField();
		txtcomprog.setBounds(196, 32, 197, 30);
		contentPane.add(txtcomprog);
		txtcomprog.setColumns(10);
		
		txtIII = new JTextField();
		txtIII.setColumns(10);
		txtIII.setBounds(196, 78, 197, 30);
		contentPane.add(txtIII);
		
		txtwork = new JTextField();
		txtwork.setColumns(10);
		txtwork.setBounds(196, 124, 197, 30);
		contentPane.add(txtwork);
		
		txtPerdev = new JTextField();
		txtPerdev.setColumns(10);
		txtPerdev.setBounds(196, 180, 197, 30);
		contentPane.add(txtPerdev);
		
		txteapp = new JTextField();
		txteapp.setColumns(10);
		txteapp.setBounds(196, 236, 197, 30);
		contentPane.add(txteapp);
		
		txtCPAR = new JTextField();
		txtCPAR.setColumns(10);
		txtCPAR.setBounds(196, 281, 197, 30);
		contentPane.add(txtCPAR);
		
		lblgrade = new JLabel("");
		lblgrade.setBounds(10, 366, 197, 37);
		contentPane.add(lblgrade);
		
		JLabel lblhonor = new JLabel("");
		lblhonor.setBounds(208, 366, 185, 37);
		contentPane.add(lblhonor);
		
		JButton btnsubmit = new JButton("Submit");
		btnsubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double prog = Double.parseDouble(txtcomprog.getText());
				double III = Double.parseDouble(txtIII.getText());
				double work = Double.parseDouble(txteapp.getText());
				double perd = Double.parseDouble(txtwork.getText());
				double EAPP = Double.parseDouble(txteapp.getText());
				double CPAR = Double.parseDouble(txtCPAR.getText());
				double PE = Double.parseDouble(txtpe.getText());
				
				double total = (prog+III+work+perd+EAPP+CPAR+PE)/7;
				lblgrade.setText("GWA: "+total);
				
				if (total >= 100 )
				{
					lblhonor.setText("invalid grade");
				}
				else if (total >= 98) {
					lblhonor.setText("you pass with Highest Honor");
				}
				else if (total >= 95 ) {
					lblhonor.setText("you pass with High Honor");
				}
				else if (total >= 90 ) {
					lblhonor.setText("you pass with Honor");
				}
				else if (total >= 75 ) {
					lblhonor.setText("you just made out alive");
				}
				else {
					lblhonor.setText("you fail your ancestor");
				}
				
				
			}
		});
		btnsubmit.setBounds(20, 402, 169, 45);
		contentPane.add(btnsubmit);
		
		JButton btnNewButton_1 = new JButton("Clear");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtcomprog.setText("");
				txtIII.setText("");
				txtwork.setText("");
				txtPerdev.setText("");
				txtpe.setText("");
				txteapp.setText("");
				txtCPAR.setText("");
				lblhonor.setText("");
				lblgrade.setText("");
			}
		});
		btnNewButton_1.setBounds(237, 401, 169, 46);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel_5_1 = new JLabel("PE");
		lblNewLabel_5_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_5_1.setBounds(10, 322, 211, 45);
		contentPane.add(lblNewLabel_5_1);
		
		txtpe = new JTextField();
		txtpe.setColumns(10);
		txtpe.setBounds(196, 325, 197, 30);
		contentPane.add(txtpe);
		
		JButton btnsubmit_1 = new JButton("Back");
		btnsubmit_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainmenu main = new mainmenu();
				main.setVisible(true);
				dispose();
				
			}
		});
		btnsubmit_1.setBounds(122, 454, 169, 45);
		contentPane.add(btnsubmit_1);
	}
}
