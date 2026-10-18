package com.axiom.auctionmod.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import com.axiom.auctionmod.AuctionPresetManager;

public class NewPresetDialogFabric extends Screen {
    private final Screen previousScreen;
    private TextFieldWidget presetNameBox;
    private TextFieldWidget itemNameBox;
    private TextFieldWidget quantityBox;
    private TextFieldWidget minBidBox;
    private TextFieldWidget durationBox;

    public NewPresetDialogFabric(Screen previousScreen) {
        super(Text.literal("New Preset"));
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        presetNameBox = new TextFieldWidget(this.textRenderer, centerX - 100, centerY - 80, 200, 25, Text.literal("Name"));
        presetNameBox.setMaxLength(32);
        this.addSelectableChild(presetNameBox);
        this.addDrawableChild(presetNameBox);

        itemNameBox = new TextFieldWidget(this.textRenderer, centerX - 100, centerY - 45, 200, 25, Text.literal("Item"));
        itemNameBox.setMaxLength(64);
        this.addSelectableChild(itemNameBox);
        this.addDrawableChild(itemNameBox);

        quantityBox = new TextFieldWidget(this.textRenderer, centerX - 100, centerY - 10, 200, 25, Text.literal("Qty"));
        quantityBox.setText("1");
        this.addSelectableChild(quantityBox);
        this.addDrawableChild(quantityBox);

        minBidBox = new TextFieldWidget(this.textRenderer, centerX - 100, centerY + 25, 200, 25, Text.literal("Min Bid"));
        minBidBox.setText("10.0");
        this.addSelectableChild(minBidBox);
        this.addDrawableChild(minBidBox);

        durationBox = new TextFieldWidget(this.textRenderer, centerX - 100, centerY + 60, 200, 25, Text.literal("Duration"));
        durationBox.setText("60");
        this.addSelectableChild(durationBox);
        this.addDrawableChild(durationBox);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("§aSAVE"), button -> savePreset())
            .dimensions(centerX - 105, centerY + 105, 100, 25)
            .build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("§cCANCEL"), button -> this.close())
            .dimensions(centerX + 5, centerY + 105, 100, 25)
            .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context);
        context.fill(0, 0, this.width, this.height, 0xFF0a0a0a);

        int centerX = this.width / 2;
        context.drawCenteredTextWithShadow(this.textRenderer, "§a✦ NEW PRESET ✦", centerX, this.height / 2 - 120, 0xFF00FF00);

        presetNameBox.render(context, mouseX, mouseY, partialTick);
        itemNameBox.render(context, mouseX, mouseY, partialTick);
        quantityBox.render(context, mouseX, mouseY, partialTick);
        minBidBox.render(context, mouseX, mouseY, partialTick);
        durationBox.render(context, mouseX, mouseY, partialTick);

        super.render(context, mouseX, mouseY, partialTick);
    }

    private void savePreset() {
        try {
            String presetName = presetNameBox.getText();
            String itemName = itemNameBox.getText();
            int quantity = Integer.parseInt(quantityBox.getText());
            double minBid = Double.parseDouble(minBidBox.getText());
            long duration = Long.parseLong(durationBox.getText());

            if (!presetName.isEmpty() && !itemName.isEmpty()) {
                AuctionPresetManager.createPreset(presetName, itemName, quantity, minBid, duration);
                this.close();
            }
        } catch (NumberFormatException e) {
            // Error
        }
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