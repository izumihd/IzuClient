package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

import java.net.URI;

public class DiscordPresenceModule extends Module {
    private static final String DEFAULT_IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHRX1GJ9I5mafTYy3ROVi0oM6zcq23PcpN9X_p1v-tSA&s";

    private final Setting<String> applicationId = str("Application ID", "1574627645235142707");
    private final Setting<String> largeImageUrl = str("Large Image URL", DEFAULT_IMAGE_URL);
    private boolean initialized;
    private String lastUsername;
    private String lastImageUrl;
    private long lastCallbackTime;

    public DiscordPresenceModule() {
        super("DiscordPresence", "Show IzuClient and your Minecraft account in Discord", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        String id = applicationId.getValue().trim();
        if (!id.matches("\\d+")) {
            OyVey.LOGGER.error("Discord Presence requires a numeric Discord Application ID.");
            disable();
            return;
        }

        try {
            DiscordRPC.discordInitialize(id, new DiscordEventHandlers.Builder().build(), true);
            initialized = true;
            updatePresence();
        } catch (RuntimeException | LinkageError exception) {
            OyVey.LOGGER.error("Failed to initialize Discord Rich Presence.", exception);
            disable();
        }
    }

    @Override
    public void onTick() {
        if (!initialized) return;

        long now = System.currentTimeMillis();
        try {
            if (now - lastCallbackTime >= 1000) {
                DiscordRPC.discordRunCallbacks();
                lastCallbackTime = now;
            }

            String username = mc.player == null ? null : mc.player.getGameProfile().name();
            String imageUrl = largeImageUrl.getValue().trim();
            if (username != null && (!username.equals(lastUsername) || !imageUrl.equals(lastImageUrl))) {
                updatePresence();
            }
        } catch (RuntimeException | LinkageError exception) {
            OyVey.LOGGER.error("Failed to update Discord Rich Presence.", exception);
            disable();
        }
    }

    @Override
    public void onDisable() {
        if (!initialized) return;

        try {
            DiscordRPC.discordClearPresence();
        } catch (RuntimeException | LinkageError exception) {
            OyVey.LOGGER.error("Failed to clear Discord Rich Presence.", exception);
        }

        try {
            DiscordRPC.discordShutdown();
        } catch (RuntimeException | LinkageError exception) {
            OyVey.LOGGER.error("Failed to shut down Discord Rich Presence.", exception);
        } finally {
            initialized = false;
            lastUsername = null;
            lastImageUrl = null;
        }
    }

    private void updatePresence() {
        if (mc.player == null) return;

        String username = mc.player.getGameProfile().name();
        String imageUrl = largeImageUrl.getValue().trim();
        DiscordRichPresence.Builder presenceBuilder = new DiscordRichPresence.Builder(null)
                .setDetails("Playing Minecraft")
                .setSmallImage(asExternalImage("https://mc-heads.net/avatar/" + username + "/64"), username);
        if (!imageUrl.isEmpty()) {
            presenceBuilder.setBigImage(asExternalImage(imageUrl), "IzuClient");
        }

        DiscordRPC.discordUpdatePresence(presenceBuilder.build());
        lastUsername = username;
        lastImageUrl = imageUrl;
    }

    private static String asExternalImage(String url) {
        URI imageUri = URI.create(url);
        String scheme = imageUri.getScheme();
        if (imageUri.getHost() == null || (!"https".equalsIgnoreCase(scheme) && !"http".equalsIgnoreCase(scheme))) {
            throw new IllegalArgumentException("Discord presence image must be an HTTP(S) URL.");
        }

        return "mp:external/" + scheme.toLowerCase(java.util.Locale.ROOT) + "/"
                + imageUri.getRawAuthority() + imageUri.getRawPath()
                + (imageUri.getRawQuery() == null ? "" : "?" + imageUri.getRawQuery());
    }
}
