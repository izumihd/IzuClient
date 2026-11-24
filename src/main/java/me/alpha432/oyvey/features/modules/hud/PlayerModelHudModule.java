package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public class PlayerModelHudModule extends HudModule {
    public final Setting<Float> scale = num("Scale", 30.0f, 10.0f, 60.0f);

    public PlayerModelHudModule() {
        super("PlayerModel", "Display your player model", 42, 62);
    }

    @Override
    protected void render(Render2DEvent event) {
        super.render(event);
        if (nullCheck()) return;

        int x = (int) getX();
        int y = (int) getY();
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                event.getContext(),
                x, y, x + 42, y + 62,
                Math.round(scale.getValue()),
                0.0625f, x + 21.0f, y + 31.0f,
                mc.player
        );
        setWidth(42);
        setHeight(62);
    }
}
