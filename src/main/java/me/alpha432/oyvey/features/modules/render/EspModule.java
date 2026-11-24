package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class EspModule extends Module {
    public final Setting<Float> range = num("Range", 64.0f, 8.0f, 128.0f);
    public final Setting<Color> color = color("Color", 255, 80, 80, 220);

    public EspModule() {
        super("ESP", "Draw outlines around nearby players", Category.RENDER);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;

        double rangeSquared = range.getValue() * range.getValue();
        for (Player player : mc.level.players()) {
            if (player == mc.player || !player.isAlive()
                    || player.distanceToSqr(mc.player) > rangeSquared) {
                continue;
            }
            RenderUtil.drawBox(event.getMatrix(), player.getBoundingBox(), color.getValue(), 1.5f);
        }
    }
}
