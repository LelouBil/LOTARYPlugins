package fr.leloubil.lotawarp;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Warp {

    @Getter
    private Integer index;
    @Getter
    private Location position;
    @Getter
    private ItemStack icon;

    public void setIndex(Integer index) {
        this.index = index;
        this.modified = true;
    }

    public void setPosition(Location position) {
        this.position = position;
        this.modified = true;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
        this.modified = true;
    }

    @Getter @Setter
    private boolean modified = false;

    public Warp(Integer anIndex,Location aPosition, ItemStack anIcon) {
        this.index = anIndex;
        this.position = aPosition;
        this.icon = anIcon;
    }
}
