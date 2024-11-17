import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.openlauncherlib.util.explorer.FileList;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {

    private static GameInfos gameInfos = new GameInfos("darklauncher", new GameVersion("1.21", GameType.FABRIC), new GameTweak[]{GameTweak.OPTIFINE});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(path.toFile(), "crashes");
    public static File launcherFile = new File(String.valueOf(path), "launchers");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), path);

    public static void main(String[] args) throws Exception {
        crashFile.mkdirs();
        launcherFile.mkdirs();
        showSplashScreen();
        doUpdate();
        launchLauncher();
    }

    public static void showSplashScreen() throws IOException {
        SplashScreen screen = new SplashScreen("DarkLauncher Updater", ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("resources/icon.png"))));
        screen.displayFor(5000L);
    }

    public static void doUpdate() throws Exception {
        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withExternalFiles(ExternalFile.getExternalFilesFromJson("https://raw.githubusercontent.com/DarkLash1/DarkLauncher/refs/heads/main/launcher_update/launcher.json")).build();
        updater.update(Paths.get(path + "/launchers"));
    }

    public static void launchLauncher() throws LaunchException {
        ClasspathConstructor constructor = new ClasspathConstructor();
        FileList fileList = new FileList();
        fileList.add(new File(launcherFile, "DarkLauncher.jar").toPath());
        constructor.add(fileList);

        ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.darklash.darklauncher.darklauncher.Frame", constructor.make());
        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process p = launcher.launch();
    }
}
