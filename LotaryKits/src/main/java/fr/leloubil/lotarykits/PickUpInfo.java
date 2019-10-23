package fr.leloubil.lotarykits;

import lombok.Getter;

import java.util.UUID;

public class PickUpInfo {
    @Getter
    private Integer maxtime;

    @Getter
    private UUID playerID;

    public PickUpInfo(UUID playerID) {
        this.maxtime = (int)System.currentTimeMillis() + (int)(30 * 1000);
        this.playerID = playerID;
    }
}
