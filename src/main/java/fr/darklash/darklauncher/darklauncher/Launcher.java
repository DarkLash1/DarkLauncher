package fr.darklash.darklauncher.darklauncher;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.fabric.FabricVersion;
import fr.flowarg.flowupdater.versions.fabric.FabricVersionBuilder;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Launcher {

    private static GameInfos gameInfos = new GameInfos("darklauncher", new GameVersion("1.21", GameType.FABRIC), new GameTweak[]{GameTweak.OPTIFINE});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(path.toFile(), "crashes");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), path);
    private static AuthInfos authInfos;

    public static void auth() throws MicrosoftAuthenticationException {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        final String refresh_token = Frame.getSaver().get("refresh_token");
        MicrosoftAuthResult result = null;
        if (refresh_token != null && !refresh_token.isEmpty()) {
            result = authenticator.loginWithRefreshToken(refresh_token);
        } else {
            result = authenticator.loginWithWebview();
            Frame.getSaver().set("refresh_token", result.getRefreshToken());
        }
        authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
    }

    public static void update(String version) throws Exception {
        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName(version).build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();
        FabricVersion fabricVersion = new FabricVersionBuilder().withFabricVersion("0.16.9").withFileDeleter(new ModFileDeleter(true)).build();
        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(vanillaVersion).withUpdaterOptions(options).withModLoaderVersion(fabricVersion).build();
        updater.update(path);
    }

    public static void launch() throws Exception {
        String selectedVersion = (String) Frame.getInstance().getPanel().getVersionComboBox().getSelectedItem();
        NoFramework noFramework = new NoFramework(path, authInfos, GameFolder.FLOW_UPDATER);
        noFramework.getAdditionalVmArgs().addAll(List.of(Frame.getInstance().getPanel().getRamSelector().getRamArguments()));
        noFramework.launch(selectedVersion, "0.16.9", NoFramework.ModLoader.FABRIC);
    }

    public static CrashReporter getReporter() {
        return reporter;
    }

    public static Path getPath() {
        return path;
    }
}
