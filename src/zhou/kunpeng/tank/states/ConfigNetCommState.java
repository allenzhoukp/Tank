package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.comm.ClientNetComm;
import zhou.kunpeng.tank.comm.ServerNetComm;
import zhou.kunpeng.tank.display.GoudyStoutFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by JA on 2017/5/24.
 * <p>
 * ConfigNetCommState allows users to config connection settings. <br>
 * For player 1, the user should assign a port as server. <br>
 * For player 2, the user should tell what is the target server address and port. <br>
 * Once the connection is made, the game will switch to PrepareLevelState.
 * </p>
 */
public class ConfigNetCommState extends JPanel {

    private MainFrame mainFrame;

    private JTextField addrText;
    private JTextField portText;

    private JLabel info;

    public ConfigNetCommState(MainFrame mainFrame, boolean isServer) {
        super();
        this.mainFrame = mainFrame;
        this.setSize(mainFrame.getSize());
        this.setLayout(null);

        if (!isServer) {
            JLabel msg = new JLabel("Connect to existing 1P");
            msg.setFont(GoudyStoutFont.getInstance());
            msg.setBounds(170, 140, 500, 30);
            this.add(msg);

            JLabel addrLabel = new JLabel("Target Address:");
            addrText = new JTextField("127.0.0.1");
            addrLabel.setBounds(125, 200, 250, 30);
            addrText.setBounds(400, 200, 100, 30);
            addrLabel.setFont(GoudyStoutFont.getInstance());
            this.add(addrLabel);
            this.add(addrText);
        }

        JLabel portLabel = new JLabel("Port:");
        portText = new JTextField("8079");
        portLabel.setBounds(280, 250, 200, 30);
        portText.setBounds(400, 250, 100, 30);
        portLabel.setFont(GoudyStoutFont.getInstance());
        this.add(portLabel);
        this.add(portText);

        info = new JLabel();
        info.setBounds(140, 300, 500, 30);
        info.setForeground(Color.RED);
        info.setFont(info.getFont().deriveFont(15.0f));
        this.add(info);


        JButton btn = new JButton("Connect");
        btn.setBounds(250, 360, 150, 30);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.valueOf(portText.getText());
                if (port < 1024 || port > 65535) {
                    info.setText("Please assign a valid port between 1024 and 65535.");
                    return;
                }

                if (isServer) {
                    mainFrame.setNetComm(new ServerNetComm(port));
                } else {
                    mainFrame.setNetComm(new ClientNetComm(addrText.getText().trim(), port));
                }

                // Add MainFrame reference to show message when socket is unexpectedly terminated.
                mainFrame.getNetComm().setMainFrame(mainFrame);

                mainFrame.getNetComm().start();
                if (mainFrame.getNetComm().isClosed()) {
                    info.setText("Player 1 has to connect first, or, check your hostname.");
                    return;
                }

                mainFrame.setTitle("Tank: Player " + (isServer ? 1 : 2));

                PrepareLevelState prepareLevelState = new PrepareLevelState
                        (mainFrame, 1, new PlayerState(), new PlayerState());
                mainFrame.nextState(prepareLevelState);
            }
        });
        this.add(btn);
    }

}
