package com.axiom.auctionmod.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import com.axiom.auctionmod.AuctionPresetManager;
import com.axiom.auctionmod.AuctionPresetManager.AuctionPreset;
import java.util.ArrayList;
import java.util.List;

public class PresetScreenFabric extends Screen {
    private final Screen previousScreen;
    private List<AuctionPreset> presetList;

    public PresetScreenFabric(Screen previousScreen) {
        super(Text.literal("Presets"));
        this.previousScreen = previousScreen;
        this.presetList = new ArrayList<>(AuctionPresetManager.getAllPresets().values());
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("§a+ CREATE"), button -> openNewPresetDialog())
            .dimensions(this.width / 2 - 105, this.height - 65, 100, 25)
            .build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("§c◄ BACK"), button -> this.close())
            .dimensions(this.width / 2 + 5, this.height - 65, 100, 25)
            .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context);
        context.fill(0, 0, this.width, this.height, 0xFF0a0a0a);

        context.drawCenteredTextWithShadow(this.textRenderer, "§a⚡ QUICK PRESETS ⚡", this.width / 2, 30, 0xFF00FF00);

        if (presetList.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer, "§7NO PRESETS", this.width / 2, this.height / 2, 0xFF666666);
        } else {
            for (int i = 0; i < presetList.size(); i++) {
                AuctionPreset preset = presetList.get(i);
                int y = 60 + (i * 50);

                context.fill(40, y, this.width - 40, y + 45, 0x2a2a2a);
                context.drawText(this.textRenderer, "§a⬤ " + preset.presetName, 50, y + 8, 0xFF00FF00, false);
                context.drawText(this.textRenderer, String.format("§7%dx %s - $%.2f", preset.quantity, preset.itemName, preset.minimumBid), 50, y + 20, 0xFFCCCCCC, false);
            }
        }

        super.render(context, mouseX, mouseY, partialTick);
    }

    private void openNewPresetDialog() {
        this.client.setScreen(new NewPresetDialogFabric(this));
    }

    @Override
    public void close() {
        this.client.setScreen(this.previousScreen);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}