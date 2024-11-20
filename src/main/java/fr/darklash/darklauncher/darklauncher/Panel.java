package fr.darklash.darklauncher.darklauncher;

import fr.darklash.darklauncher.darklauncher.utils.Animation;
import fr.darklash.darklauncher.darklauncher.utils.MicrosoftThread;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Panel extends JPanel implements SwingerEventListener {

    private Image background = Frame.getImage("images/tonight.png");
    private STexturedButton play = new STexturedButton(Frame.getBufferedImage("images/bouton.png"));
    private STexturedButton microsoft = new STexturedButton(Frame.getBufferedImage("images/microsoft.png"));
    private STexturedButton settings = new STexturedButton(Frame.getBufferedImage("images/reglage.png"));
    private STexturedButton close = new STexturedButton(Frame.getBufferedImage("images/croix.png"));
    private RamSelector ramSelector = new RamSelector(Frame.getRamFile());

    private JComboBox<String> versionComboBox;

    public Panel() throws IOException {
        this.setLayout(null);

        versionComboBox = new JComboBox<>(new String[] {"1.21", "1.20", "1.19", "1.18"});
        versionComboBox.setBounds(425, 360, 150, 30);
        this.add(versionComboBox);

        play.setBounds(109, 109);
        play.setLocation(445, 245);
        play.addEventListener(this);
        this.add(play);

        microsoft.setBounds(100, 100);
        microsoft.setLocation(100, 100);
        microsoft.addEventListener(this);
        this.add(microsoft);

        settings.setBounds(64, 64);
        settings.setLocation(0, 0);
        settings.addEventListener(this);
        this.add(settings);

        close.setBounds(64, 64);
        close.setLocation(936, 0);
        close.addEventListener(this);
        this.add(close);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == microsoft) {
            Thread t = new Thread(new MicrosoftThread());
            t.start();
        } else if (swingerEvent.getSource() == play) {
            ramSelector.save();
            try {
                String selectedVersion = (String) versionComboBox.getSelectedItem();
                Launcher.update(selectedVersion);
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Impossible de mettre à jour !");
            }
            try {
                Launcher.launch();
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Impossible de lancer le jeu !");
            }
        } else if (swingerEvent.getSource() == settings) {
            ramSelector.display();
        } else if (swingerEvent.getSource() == close) {
            Animation.fadeOutFrame(Frame.getInstance(), 30, () -> System.exit(0));
        }
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }

    public JComboBox<String> getVersionComboBox() {
        return versionComboBox;
    }
}
