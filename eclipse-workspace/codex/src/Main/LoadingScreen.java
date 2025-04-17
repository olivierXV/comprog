package Main;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadingScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar;
    private JLabel loadingLabel;
    private boolean isLoading = true; 

    public LoadingScreen() {
        setTitle("Loading...");
        setSize(500, 400); 
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel(new GridBagLayout()); 
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); 

        try {
            URL gifUrl = new URL("https://gifdb.com/images/high/walking-duck-animated-happy-waddle-swnffm726l0qmw5j.gif");
            ImageIcon gifIcon = new ImageIcon(gifUrl);
            JLabel illustration = new JLabel(gifIcon);
            centerPanel.add(illustration, gbc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        gbc.gridy = 1;
        loadingLabel = new JLabel("Loading", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 35)); 
        loadingLabel.setForeground(Color.BLACK);
        centerPanel.add(loadingLabel, gbc); 

        panel.add(centerPanel, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(500, 20));
        panel.add(progressBar, BorderLayout.SOUTH);

        add(panel);
    }

    public void showLoadingScreen() {
        SwingUtilities.invokeLater(() -> setVisible(true));

        new LoadingAnimation().execute();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(5000); 
                return null;
            }

            @Override
            protected void done() {
                isLoading = false; 
                dispose(); 

                SwingUtilities.invokeLater(() -> {
                    try {
                        new home(); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }.execute();
    }

    private class LoadingAnimation extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            String baseText = "Loading";
            int dotCount = 0;

            while (isLoading) {
                dotCount = (dotCount + 1) % 4;
                String dots = ".".repeat(dotCount);
                loadingLabel.setText(baseText + dots);
                Thread.sleep(500);
            }
            return null;
        }
    }

    public static void main(String[] args) {
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.showLoadingScreen();
    }
}
