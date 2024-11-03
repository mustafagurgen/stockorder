package com.inghub.StockOrder.enums;

public enum AssetEnum {
    TRY,
    INGBANK,
    XBANK,
    YBANK,
    ZBANK;

    public static AssetEnum getAssetEnum(String asset) {
        for (AssetEnum assetEnum : AssetEnum.values()) {
            if (assetEnum.name().equals(asset)) {
                return assetEnum;
            }
        }
        return null;
    }
}
