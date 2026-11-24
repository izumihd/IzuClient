package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.world.item.ItemStack;

public class InventoryHudModule extends HudModule {
    private static final int COLUMNS = 9;
    private static final int ROWS = 4;
    private static final int SLOT_SIZE = 18;

    public InventoryHudModule() {
        super("Inventory", "Display your inventory", 9 * SLOT_SIZE, 10 + ROWS * SLOT_SIZE);
    }

    @Override
    protected void render(Render2DEvent event) {
        super.render(event);
        if (nullCheck()) return;

        int x = (int) getX();
        int y = (int) getY();
        event.getContext().drawString(mc.font, "Inventory", x, y, -1);

        int gridY = y + mc.font.lineHeight + 2;
        for (int slot = 0; slot < COLUMNS * ROWS; slot++) {
            int slotX = x + (slot % COLUMNS) * SLOT_SIZE;
            int slotY = gridY + (slot / COLUMNS) * SLOT_SIZE;
            event.getContext().fill(slotX, slotY, slotX + SLOT_SIZE - 1, slotY + SLOT_SIZE - 1, 0x66000000);

            ItemStack stack = mc.player.getInventory().getItem(slot);
            if (!stack.isEmpty()) {
                event.getContext().renderItem(stack, slotX + 1, slotY + 1);
                event.getContext().renderItemDecorations(mc.font, stack, slotX + 1, slotY + 1);
            }
        }

        setWidth(COLUMNS * SLOT_SIZE);
        setHeight(mc.font.lineHeight + 2 + ROWS * SLOT_SIZE);
    }
}
