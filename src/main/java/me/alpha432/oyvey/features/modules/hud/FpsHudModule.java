package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;

public class FpsHudModule extends HudModule {
    public FpsHudModule() {
        super("FPS", "Display the current frame rate", 50, 10);
    }

    @Override
    protected void render(Render2DEvent event) {
        super.render(event);
        if (nullCheck()) return;

        String text = "FPS: " + mc.getFps();
        event.getContext().drawString(mc.font, text, (int) getX(), (int) getY(), -1);
        setWidth(mc.font.width(text));
        setHeight(mc.font.lineHeight);
    }
}
