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
import java.net.UnknownHostException;

/**
 * Created by JA on 2017/5/24.
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
            JLabel addrLabel = new JLabel("Target Address:");
            addrText = new JTextField("127.0.0.1");
            addrLabel.setBounds(80, 150, 250, 30);
            addrText.setBounds(350, 150, 100, 30);
            addrLabel.setFont(GoudyStoutFont.getInstance());
            this.add(addrLabel);
            this.add(addrText);
        }

        JLabel portLabel = new JLabel("Port:");
        portText = new JTextField("8079");
        portLabel.setBounds(230, 200, 200, 30);
        portText.setBounds(350, 200, 100, 30);
        portLabel.setFont(GoudyStoutFont.getInstance());
        this.add(portLabel);
        this.add(portText);

        info = new JLabel();
        info.setBounds(100, 350, 500, 30);
        info.setForeground(Color.RED);
        info.setFont(info.getFont().deriveFont(15.0f));
        this.add(info);


        JButton btn = new JButton("Connect");
        btn.setBounds(250, 300, 150, 30);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServer) {
                    mainFrame.setNetComm(new ServerNetComm(Integer.valueOf(portText.getText())));
                } else {
                    mainFrame.setNetComm(new ClientNetComm(addrText.getText().trim(),
                            Integer.valueOf(portText.getText())));
                }

                mainFrame.getNetComm().start();
                if(mainFrame.getNetComm().isClosed()){
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
