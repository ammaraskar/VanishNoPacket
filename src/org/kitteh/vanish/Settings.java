package org.kitteh.vanish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.kitteh.vanish.staticaccess.VanishNoPacket;

public class Settings {
    private static boolean enablePermTest;
    private static String fakeQuit;
    private static String fakeJoin;
    private static boolean autoFakeJoinSilent;
    private static boolean worldChangeCheck;
    private static int lightningEffectCount;

    /**
     * Tracking the config. Don't touch this.
     */
    private static final int confVersion = 4;

    public static void deployDefaultConfig(String name) {
        try {
            final File target = new File(VanishNoPacket.getPlugin().getDataFolder(), name);
            final InputStream source = VanishNoPacket.getPlugin().getResource(name);
            if (source == null) {
                Bukkit.getServer().getLogger().log(Level.SEVERE, Messages.getString("Settings.couldNotFindDefault") + " " + name);
                return;
            }
            if (!VanishNoPacket.getPlugin().getDataFolder().exists()) {
                VanishNoPacket.getPlugin().getDataFolder().mkdir();
            }
            if (!target.exists()) {
                final OutputStream output = new FileOutputStream(target);
                int len;
                final byte[] buffer = new byte[1024];
                while ((len = source.read(buffer)) > 0) {
                    output.write(buffer, 0, len);
                }
                output.close();
            }
            source.close();
        } catch (final Exception ex) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, Messages.getString("Settings.couldNotSaveDefault") + " " + name, ex);
        }
    }

    public static void freshStart(VanishPlugin plugin) {
        final FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        final int ver = config.getInt("configVersionDoNotTouch.SeriouslyThisWillEraseYourConfig", 0);
        if (ver != Settings.confVersion) {
            plugin.log(Messages.getString("Settings.updatingConfiguration"));
            if (ver < 1) {
                config.set("hooks.spoutcraft", config.getBoolean("spoutcraft.enable", true));
                config.set("spoutcraft.enable", null);
                config.set("spoutcraft", null);
            }
            if ((ver <= 1) || config.contains("permtest.enable")) {
                final boolean permtest = config.getBoolean("permtest.enable", false);
                config.set("permtest.enable", null);
                config.set("permtest", permtest);
                config.set("enableColoration", null);
                config.set("enableTabControl", null);
                final boolean updates = config.getBoolean("updates.check", true);
                config.set("updates.check", null);
                config.set("checkupdates", updates);
            }
            if ((ver <= 3)) {
                config.set("effects.lightning.count", 30);
            }
            config.set("configVersionDoNotTouch.SeriouslyThisWillEraseYourConfig", Settings.confVersion);
            plugin.saveConfig();
        }
        Settings.enablePermTest = config.getBoolean("permtest", false);
        Settings.fakeJoin = config.getString("fakeannounce.join", "%p joined the game.").replace("&&", String.valueOf(ChatColor.COLOR_CHAR));
        Settings.fakeQuit = config.getString("fakeannounce.quit", "%p left the game.").replace("&&", String.valueOf(ChatColor.COLOR_CHAR));
        Settings.autoFakeJoinSilent = config.getBoolean("fakeannounce.automaticforsilentjoin", false);
        Settings.worldChangeCheck = config.getBoolean("permissionsupdates.checkonworldchange", false);
        Settings.lightningEffectCount = config.getInt("effects.lightning.count", 30);
        if (Settings.lightningEffectCount < 1) {
            Settings.lightningEffectCount = 1;
        }
        if (config.getBoolean("debug", false)) {
            Debuggle.itsGoTime();
        } else {
            Debuggle.nah();
        }
    }

    public static boolean getAutoFakeJoinSilent() {
        return Settings.autoFakeJoinSilent;
    }

    public static boolean getEnablePermTest() {
        return Settings.enablePermTest;
    }

    public static String getFakeJoin() {
        return Settings.fakeJoin;
    }

    public static String getFakeQuit() {
        return Settings.fakeQuit;
    }

    public static int getLightningCount() {
        return Settings.lightningEffectCount;
    }

    public static boolean getWorldChangeCheck() {
        return Settings.worldChangeCheck;
    }
}
