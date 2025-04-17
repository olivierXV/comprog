package olicierrv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tuname;
    private JTextField tusect;

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Test frame = new Test();
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
    public Test() {
    	setBackground(new Color(36, 36, 36));
    	setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(36, 36, 36));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel name = new JLabel("Enter your name:");
        name.setForeground(new Color(255, 255, 255));
        name.setFont(new Font("Cantarell", Font.PLAIN, 20));
        name.setBounds(35, 46, 152, 35);
        contentPane.add(name);

        tuname = new JTextField();
		tuname.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tusect.requestFocusInWindow();
			}
		});
        tuname.setBackground(new Color(54, 54, 54));
        tuname.setForeground(new Color(255, 255, 255));
        tuname.setBounds(224, 48, 170, 35);
        contentPane.add(tuname);
        tuname.setColumns(10);

        JLabel section = new JLabel("Enter your section:");
        section.setForeground(new Color(255, 255, 255));
        section.setFont(new Font("Cantarell", Font.PLAIN, 20));
        section.setBounds(35, 93, 180, 35);
        contentPane.add(section);

        JLabel welcome = new JLabel("");
        welcome.setForeground(new Color(255, 255, 255));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setFont(new Font("Cantarell", Font.PLAIN, 20));
        welcome.setBounds(35, 140, 359, 35);
        contentPane.add(welcome);

        JButton submt = new JButton("Submit");
        submt.setBackground(new Color(54, 54, 54));
        submt.setForeground(new Color(255, 255, 255));
        submt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = tuname.getText();
                String sect = tusect.getText();
                welcome.setText("Hello " + ao.capitalize(name) + " of section " + ao.capitalize(sect));
            }
        });
        submt.setFont(new Font("Cantarell", Font.BOLD, 14));
        submt.setBounds(86, 187, 101, 35);
        contentPane.add(submt);

        tusect = new JTextField();
        tusect.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		submt.doClick();
        	}
        });
        tusect.setBackground(new Color(54, 54, 54));
        tusect.setForeground(new Color(255, 255, 255));
        tusect.setColumns(10);
        tusect.setBounds(224, 95, 170, 35);
        contentPane.add(tusect);
        
        JButton btnCLR = new JButton("Clear");
        btnCLR.setBackground(new Color(54, 54, 54));
        btnCLR.setForeground(new Color(255, 255, 255));
        btnCLR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tuname.setText("");
                tusect.setText("");
                welcome.setText("");
            }
        });
        btnCLR.setFont(new Font("Cantarell", Font.BOLD, 14));
        btnCLR.setBounds(260, 187, 101, 35);
        contentPane.add(btnCLR);

		JButton btnMainMenu = new JButton("Main Menu");
		btnMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int exit = JOptionPane.showConfirmDialog(null, "Return to main menu?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (exit == JOptionPane.YES_OPTION) {
					dispose();
					MainMenu.main(null);
				}
			}
		});
        btnMainMenu.setForeground(new Color(255, 255, 255));
        btnMainMenu.setFont(new Font("Cantarell", Font.BOLD, 18));
        btnMainMenu.setBackground(new Color(54, 54, 54));
        btnMainMenu.setBounds(159, 234, 135, 35);
        contentPane.add(btnMainMenu);
    }
}
