package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.client.multiplayer.PlayerInfo;

public class PingHudModule extends HudModule {
    public PingHudModule() {
        super("Ping", "Display your current server latency", 60, 10);
    }

    @Override
    protected void render(Render2DEvent event) {
        super.render(event);
        if (nullCheck()) return;

        PlayerInfo playerInfo = mc.getConnection() == null
                ? null
                : mc.getConnection().getPlayerInfo(mc.player.getUUID());
        String text = playerInfo == null ? "Ping: N/A" : "Ping: " + playerInfo.getLatency() + "ms";
        event.getContext().drawString(mc.font, text, (int) getX(), (int) getY(), -1);
        setWidth(mc.font.width(text));
        setHeight(mc.font.lineHeight);
    }
}
