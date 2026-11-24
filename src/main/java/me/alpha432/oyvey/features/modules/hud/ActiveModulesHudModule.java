package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.HudModule;

import java.util.Comparator;
import java.util.List;

public class ActiveModulesHudModule extends HudModule {
    public ActiveModulesHudModule() {
        super("ActiveModules", "List enabled modules on screen", 100, 10);
    }

    @Override
    protected void render(Render2DEvent event) {
        super.render(event);
        if (nullCheck()) return;

        List<String> names = OyVey.moduleManager.stream()
                .filter(Module::isEnabled)
                .filter(Module::isDrawn)
                .filter(module -> !module.hidden)
                .map(Module::getFullArrayString)
                .sorted(Comparator.comparingInt((String name) -> mc.font.width(name)).reversed())
                .toList();

        int x = (int) getX();
        int y = (int) getY();
        for (int i = 0; i < names.size(); i++) {
            event.getContext().drawString(mc.font, names.get(i), x, y + i * mc.font.lineHeight, -1);
        }

        setWidth(names.stream().mapToInt(mc.font::width).max().orElse(0));
        setHeight(names.size() * mc.font.lineHeight);
    }
}
