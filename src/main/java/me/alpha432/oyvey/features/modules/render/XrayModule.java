package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.entity.player.TickEvent;
import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class XrayModule extends Module {
    public final Setting<Float> range = num("Range", 12.0f, 4.0f, 16.0f);
    public final Setting<Color> color = color("Color", 70, 220, 255, 230);

    private final List<BlockPos> ores = new ArrayList<>();
    private long nextScanTime;

    public XrayModule() {
        super("Xray", "Highlight nearby ore blocks through terrain", Category.RENDER);
    }

    @Override
    public void onEnable() {
        nextScanTime = 0L;
    }

    @Override
    public void onDisable() {
        ores.clear();
    }

    @Subscribe
    public void onTick(TickEvent.Pre event) {
        if (nullCheck()) return;

        long now = System.currentTimeMillis();
        if (now < nextScanTime) return;
        nextScanTime = now + 500L;

        ores.clear();
        int radius = Math.round(range.getValue());
        BlockPos center = mc.player.blockPosition();
        int radiusSquared = radius * radius;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z > radiusSquared) continue;

                    BlockPos pos = center.offset(x, y, z);
                    if (!mc.level.hasChunkAt(pos)) continue;
                    if (isOre(pos)) ores.add(pos.immutable());
                }
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (BlockPos pos : ores) {
            RenderUtil.drawBox(event.getMatrix(), pos, color.getValue(), 1.0f);
        }
    }

    private boolean isOre(BlockPos pos) {
        var state = mc.level.getBlockState(pos);
        return state.is(BlockTags.COAL_ORES)
                || state.is(BlockTags.COPPER_ORES)
                || state.is(BlockTags.IRON_ORES)
                || state.is(BlockTags.GOLD_ORES)
                || state.is(BlockTags.REDSTONE_ORES)
                || state.is(BlockTags.LAPIS_ORES)
                || state.is(BlockTags.DIAMOND_ORES)
                || state.is(BlockTags.EMERALD_ORES)
                || state.is(Blocks.NETHER_QUARTZ_ORE)
                || state.is(Blocks.NETHER_GOLD_ORE)
                || state.is(Blocks.ANCIENT_DEBRIS);
    }
}
