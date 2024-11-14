package fr.darklash.darklauncher.darklauncher;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.darklash.darklauncher.darklauncher.utils.Animation;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.util.WindowMover;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Frame extends JFrame {

    private static Frame instance;
    private Panel panel;
    private static File ramFile = new File(String.valueOf(Launcher.getPath()), "ram.txt");
    private static File saverFile = new File(String.valueOf(Launcher.getPath()), "user.stock");
    private static Saver saver = new Saver(saverFile);

    public Frame() throws IOException {
        instance = this;
        this.setTitle("DarkLauncher");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(getImage("images/icon.png"));
        this.setContentPane(panel = new Panel());

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        Animation.fadeInFrame(this, 30);

        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Launcher.crashFile.mkdirs();
        if (!ramFile.exists()) {
            ramFile.createNewFile();
        }
        if (!saverFile.exists()) {
            saverFile.createNewFile();
        }
        instance = new Frame();
        rpc();
    }

    public static void rpc() {
        final DiscordRPC lib = DiscordRPC.INSTANCE;
        final String appIP = "1287160385052348426";
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(appIP, handlers, true, "");
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "Development of DarkLauncher...";
        presence.largeImageKey = "pp-chatgpt";

        lib.Discord_UpdatePresence(presence);
    }

    public static Image getImage(String file) throws IOException {
        InputStream inputStream = Frame.getInstance().getClass().getClassLoader().getResourceAsStream(file);
        return ImageIO.read(inputStream);
    }

    public static BufferedImage getBufferedImage(String file) throws IOException {
        InputStream inputStream = Frame.getInstance().getClass().getClassLoader().getResourceAsStream(file);
        return ImageIO.read(inputStream);
    }

    public static Frame getInstance() {
        return instance;
    }

    public Panel getPanel() {
        return this.panel;
    }

    public static File getRamFile() {
        return ramFile;
    }

    public static Saver getSaver() {
        return saver;
    }
}
