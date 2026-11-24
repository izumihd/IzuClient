package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class ZoomModule extends Module {
    public final Setting<Integer> zoomFov = num("FOV", 30, 10, 70);

    private Integer originalFov;

    public ZoomModule() {
        super("Zoom", "Temporarily lower the field of view", Category.RENDER);
    }

    @Override
    public void onEnable() {
        originalFov = mc.options.fov().get();
        applyZoom();
    }

    @Override
    public void onTick() {
        applyZoom();
    }

    @Override
    public void onDisable() {
        if (originalFov != null) {
            mc.options.fov().set(originalFov);
            originalFov = null;
        }
    }

    private void applyZoom() {
        mc.options.fov().set(zoomFov.getValue());
    }
}
