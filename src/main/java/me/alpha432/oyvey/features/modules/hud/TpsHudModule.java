package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;

public class TpsHudModule extends HudModule {
    public TpsHudModule() {
        super("TPS", "Display the estimated server tick rate", 50, 10);
    }

    @Override
    protected void render(Render2DEvent event) {
        super.render(event);
        if (nullCheck()) return;

        String text = OyVey.serverManager == null || mc.getConnection() == null
                ? "TPS: N/A"
                : "TPS: %.1f".formatted(OyVey.serverManager.getTps());
        event.getContext().drawString(mc.font, text, (int) getX(), (int) getY(), -1);
        setWidth(mc.font.width(text));
        setHeight(mc.font.lineHeight);
    }
}
