package com.axiom.auctionmod;

import java.util.*;

public class AuctionManager {
    private static final Map<String, Auction> activeAuctions = new HashMap<>();

    public static class Auction {
        public String itemName;
        public int quantity;
        public double currentBid;
        public String highestBidder;
        public long startTime;
        public long durationSeconds;
        public String auctionId;

        public Auction(String itemName, int quantity, double minBid, long durationSeconds) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.currentBid = minBid;
            this.startTime = System.currentTimeMillis();
            this.durationSeconds = durationSeconds;
            this.auctionId = UUID.randomUUID().toString();
        }

        public long getTimeRemaining() {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            return Math.max(0, durationSeconds - elapsed);
        }
    }

    public static void startAuction(String itemName, int quantity, double minBid, long duration) {
        Auction auction = new Auction(itemName, quantity, minBid, duration);
        activeAuctions.put(auction.auctionId, auction);
    }

    public static void placeBid(String auctionId, String playerName, double bidAmount) {
        Auction auction = activeAuctions.get(auctionId);
        if (auction != null && bidAmount > auction.currentBid) {
            auction.currentBid = bidAmount;
            auction.highestBidder = playerName;
        }
    }

    public static Map<String, Auction> getActiveAuctions() {
        return new HashMap<>(activeAuctions);
    }
}