package fr.darklash.darklauncher.darklauncher.utils;

import fr.darklash.darklauncher.darklauncher.Launcher;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

public class MicrosoftThread implements Runnable {

    @Override
    public void run() {
        try {
            Launcher.auth();
        } catch (MicrosoftAuthenticationException e) {
            Launcher.getReporter().catchError(e, "Impossible de se connecter !");
        }
    }
}
