package com.axiom.auctionmod;

import java.util.*;

public class AuctionPresetManager {
    private static final Map<String, AuctionPreset> presets = new HashMap<>();

    public static class AuctionPreset {
        public String presetId;
        public String presetName;
        public String itemName;
        public int quantity;
        public double minimumBid;
        public long durationSeconds;

        public AuctionPreset(String presetName, String itemName, int quantity, double minimumBid, long durationSeconds) {
            this.presetId = UUID.randomUUID().toString();
            this.presetName = presetName;
            this.itemName = itemName;
            this.quantity = quantity;
            this.minimumBid = minimumBid;
            this.durationSeconds = durationSeconds;
        }
    }

    public static class AuctionItem {
        public String displayName;
        public String description;

        public AuctionItem(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
    }

    private static final List<AuctionItem> ITEM_CATALOG = Arrays.asList(
        new AuctionItem("§bDiamond", "Rare mineral"),
        new AuctionItem("§8Netherite Ingot", "Ultimate material"),
        new AuctionItem("§6Enchanted Golden Apple", "Legendary buff"),
        new AuctionItem("§5Dragon Egg", "Rarest loot"),
        new AuctionItem("§7Wither Star", "Summoning material"),
        new AuctionItem("§2Emerald", "Trading currency"),
        new AuctionItem("§eBeacon", "Powerup structure"),
        new AuctionItem("§dEnchanted Book", "Custom enchantments"),
        new AuctionItem("§8Ancient Debris", "Netherite source"),
        new AuctionItem("§dAmethyst Cluster", "Decorative ore")
    );

    public static void createPreset(String presetName, String itemName, int quantity, double minimumBid, long durationSeconds) {
        AuctionPreset preset = new AuctionPreset(presetName, itemName, quantity, minimumBid, durationSeconds);
        presets.put(preset.presetId, preset);
    }

    public static void deletePreset(String presetId) {
        presets.remove(presetId);
    }

    public static Map<String, AuctionPreset> getAllPresets() {
        return new HashMap<>(presets);
    }

    public static List<AuctionItem> getItemCatalog() {
        return new ArrayList<>(ITEM_CATALOG);
    }

    public static void launchPresetAuction(String presetId) {
        AuctionPreset preset = presets.get(presetId);
        if (preset != null) {
            AuctionManager.startAuction(preset.itemName, preset.quantity, preset.minimumBid, preset.durationSeconds);
        }
    }

    public static void loadPresetsFromFile() {
    }
}