package fr.leloubil.lotaryitems;

import org.bukkit.scheduler.BukkitRunnable;

class CoolDownRunnable extends BukkitRunnable {
    @Override
    public void run() {
        Main.cooldowns.forEach(((coolDown) -> coolDown.setTime(coolDown.getTime() - 1)));
        Main.cooldowns.removeIf(e -> e.getTime() <= 0);
    }
}
