package com.axiom.auctionmod.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import com.axiom.auctionmod.AuctionManager;
import com.axiom.auctionmod.AuctionPresetManager;
import com.axiom.auctionmod.AuctionPresetManager.AuctionItem;
import java.util.List;

public class AuctionMainScreenFabric extends Screen {
    private final Screen previousScreen;
    private AuctionItem selectedItem;
    private int quantity = 1;
    private double minimumBid = 10.0;
    private long duration = 60;
    private int itemListScroll = 0;

    public AuctionMainScreenFabric(Screen previousScreen) {
        super(Text.literal("Auction Hub"));
        this.previousScreen = previousScreen;
        List<AuctionItem> catalog = AuctionPresetManager.getItemCatalog();
        this.selectedItem = !catalog.isEmpty() ? catalog.get(0) : null;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("§a▶ START AUCTION"), button -> startAuction())
            .dimensions(this.width / 2 - 105, this.height - 65, 100, 25)
            .build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("§a⚡ PRESETS"), button -> openPresetMenu())
            .dimensions(this.width / 2 + 5, this.height - 65, 100, 25)
            .build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("§c✕ CLOSE"), button -> this.close())
            .dimensions(this.width / 2 - 50, this.height - 30, 100, 20)
            .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context);
        context.fill(0, 0, this.width, this.height, 0xFF0a0a0a);

        int panelX = 15;
        int panelY = 15;
        int panelWidth = this.width - 30;
        int panelHeight = this.height - 100;

        drawGlassPanel(context, panelX, panelY, panelWidth, panelHeight, 4);
        context.drawCenteredTextWithShadow(this.textRenderer, "§a━━ AUCTION HUB ━━", this.width / 2, panelY + 15, 0xFF00FF00);

        int itemListX = panelX + 20;
        int itemListY = panelY + 45;
        int itemListWidth = 220;

        drawGlassPanel(context, itemListX - 5, itemListY - 5, itemListWidth + 10, 150, 3);
        context.drawText(this.textRenderer, "§a◆ SELECT ITEM", itemListX, itemListY - 20, 0xFF00FF00, false);

        List<AuctionItem> catalog = AuctionPresetManager.getItemCatalog();
        for (int i = 0; i < Math.min(4, catalog.size()); i++) {
            AuctionItem item = catalog.get(i);
            int yOffset = itemListY + (i * 35);
            boolean isSelected = selectedItem == item;

            if (isSelected) {
                context.fill(itemListX, yOffset, itemListX + itemListWidth - 10, yOffset + 30, 0x4D00FF00);
            }

            String text = (isSelected ? "§a➜ " : "  ") + item.displayName;
            context.drawText(this.textRenderer, text, itemListX + 8, yOffset + 8, isSelected ? 0xFF00FF00 : 0xFFFFFFFF, false);

            if (mouseX >= itemListX && mouseX <= itemListX + itemListWidth - 10 &&
                mouseY >= yOffset && mouseY <= yOffset + 30) {
                selectedItem = item;
            }
        }

        int settingsX = itemListX + itemListWidth + 20;
        int settingsY = itemListY;

        drawGlassPanel(context, settingsX - 5, settingsY - 5, 200, 150, 3);
        context.drawText(this.textRenderer, "§a⚙ SETTINGS", settingsX, settingsY - 20, 0xFF00FF00, false);

        context.drawText(this.textRenderer, "§7QUANTITY: §a" + quantity, settingsX + 10, settingsY + 15, 0xFFFFFFFF, false);
        context.drawText(this.textRenderer, "§7MIN BID: §a$" + minimumBid, settingsX + 10, settingsY + 35, 0xFFFFFFFF, false);
        context.drawText(this.textRenderer, "§7DURATION: §a" + duration + "s", settingsX + 10, settingsY + 55, 0xFFFFFFFF, false);

        String preview = String.format("§a%dx %s for $%.2f", quantity, selectedItem != null ? selectedItem.displayName : "None", minimumBid);
        context.drawText(this.textRenderer, preview, settingsX + 10, settingsY + 80, 0xFFAAFFAA, false);

        super.render(context, mouseX, mouseY, partialTick);
    }

    private void drawGlassPanel(DrawContext context, int x, int y, int width, int height, int borderThickness) {
        context.fill(x, y, x + width, y + height, 0x1A0a0a0a);
        
        for (int i = 0; i < borderThickness; i++) {
            context.fill(x + i, y + i, x + width - i, y + height - i, 0x4D00FF00);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0) {
            itemListScroll = Math.max(0, itemListScroll - 1);
        } else {
            itemListScroll = Math.min(5, itemListScroll + 1);
        }
        return true;
    }

    private void startAuction() {
        if (selectedItem == null) return;
        AuctionManager.startAuction(selectedItem.displayName, quantity, minimumBid, duration);
        this.close();
    }

    private void openPresetMenu() {
        this.client.setScreen(new PresetScreenFabric(this));
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