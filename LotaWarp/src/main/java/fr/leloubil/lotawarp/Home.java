package fr.leloubil.lotawarp;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

public class Home {

    @Getter
    private UUID owner;

    @Getter
    private Location location;

    @Getter
    private String name;

    public Home(UUID owner, Location location, String name) {
        this.owner = owner;
        this.location = location;
        this.name = name;
    }


    public Home(String uuid, String name, String location) {
        this.owner = UUID.fromString(uuid);
        this.location = WarpManager.StrToLocation(location);
        this.name = name;
    }
}
