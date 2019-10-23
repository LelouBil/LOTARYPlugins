package fr.leloubil.lotaryitems;

import fr.leloubil.lotaryitems.Items.*;
import fr.sipixer.main.lotarybox;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ItemsListener implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem() == null || e.getItem().getType().equals(Material.AIR)) return;
        ItemStack i = e.getItem();
        String name = ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName());
        if (Main.items.containsKey(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()))) {


            if (ChatColor.stripColor(i.getItemMeta().getLore().get(0)).equals(ChatColor.stripColor(Main.items.get(name).getCItemID()))) {
                ItemMeta imeta = i.getItemMeta();
                if (!Main.isBypassCooldown() && BaseItem.hasCoolDown(e.getPlayer().getUniqueId())) {
                    Main.cooldowns.forEach(c -> {
                        if (c.getPlayer().equals(e.getPlayer().getUniqueId()) && ChatColor.stripColor(c.getI().getItemMeta().getLore().get(0)).equals(ChatColor.stripColor(i.getItemMeta().getLore().get(0)))) {
                            e.getPlayer().sendMessage(c.getI().getItemMeta().getDisplayName().substring(0, 2) + "[" + c.getI().getItemMeta().getDisplayName() + "]" + ChatColor.RED + Main.items.get(name).getCooldownMsg().replace("%s%", String.valueOf(c.getTime())) + ChatColor.RESET);

                        }
                    });
                    return;
                }
                Main.items.get(name).OnClick(e.getPlayer(), e.getItem());
                if (!Main.isBypassCooldown()) {
                    Main.cooldowns.add(new ItemCoolDown(Main.items.get(name).getCoolDown(), e.getPlayer().getUniqueId(), i));
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        ItemStack i = e.getItemDrop().getItemStack();
        String name = ChatColor.stripColor(i.getItemMeta().getDisplayName());
        if (Main.items.containsKey(name)) {
            if (ChatColor.stripColor(i.getItemMeta().getLore().get(0)).equals(ChatColor.stripColor(Main.items.get(name).getCItemID()))) {
                if (!Main.items.get(name).isDropable()) {
                    // e.getItemDrop().remove();
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.getDrops().forEach(itemStack -> {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                String name = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
                if (Main.items.containsKey(name)) {
                    if (ChatColor.stripColor(itemStack.getItemMeta().getLore().get(0)).equals(ChatColor.stripColor(Main.items.get(name).getCItemID()))) {
                        if (!Main.items.get(name).isPersistDeath()) {
                            itemStack.setAmount(0);
                            itemStack.setType(Material.AIR);

                        }
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public final void onChestMove(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();

        if ((!EnumUtils.isValidEnum(NonStockInvs.class, top.getType().toString()) || top.getName().startsWith("BackPack")) && bottom.getType() == InventoryType.PLAYER) {
            if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                ItemStack i = event.getCurrentItem();
                String name = ChatColor.stripColor(i.getItemMeta().getDisplayName());
                if (Main.items.containsKey(name)) {
                    if (ChatColor.stripColor(i.getItemMeta().getLore().get(0)).equals(ChatColor.stripColor(Main.items.get(name).getCItemID()))) {
                        if (!Main.items.get(name).isChestable()) event.setCancelled(true);
                        if (i.getType().equals(Material.CHEST) && top.getName().startsWith("BackPack"))
                            event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public final void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().getInventory().getItemInHand().getType().equals(Material.AIR)) return;
        ItemStack i = e.getPlayer().getInventory().getItemInHand();
        String name = ChatColor.stripColor(e.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName());
        if (Main.items.containsKey(ChatColor.stripColor(e.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName()))) {
            if (ChatColor.stripColor(i.getItemMeta().getLore().get(0)).equals(ChatColor.stripColor(Main.items.get(name).getCItemID()))) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public final void onCraft(CraftItemEvent e) {
        ItemStack i = e.getCurrentItem();
        String name = ChatColor.stripColor(i.getItemMeta().getDisplayName());
        if (Main.items.containsKey(ChatColor.stripColor(i.getItemMeta().getDisplayName()))) {
            if (ChatColor.stripColor(i.getItemMeta().getLore().get(0)).equals(ChatColor.stripColor(Main.items.get(name).getCItemID()))) {
                if (i.getType().equals(Material.CHEST)) {
                    if (e.isShiftClick()) {
                        e.setCancelled(true);
                        return;
                    }
                    ItemMeta itemMeta = i.getItemMeta();
                    List<String> lore = itemMeta.getLore();
                    if (lore.size() >= 2) lore.removeIf(s -> s.startsWith("Numéro"));
                    lore.add("Numéro de série : " + new PackID().toString());
                    itemMeta.setLore(lore);
                    i.setItemMeta(itemMeta);
                    PackID id = new PackID(itemMeta.getLore().get(1).substring(18, 24));
                    BackPack.getPacks().put(id, Bukkit.createInventory(null, 18, "BackPack n° " + id.toString()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public final void onInvClick(InventoryClickEvent e) {
        Inventory i = e.getView().getTopInventory();
        Inventory rec = e.getView().getBottomInventory();
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if (Recycleur.isrecycl(i)) {
            e.setCancelled(true);
            if (e.getClickedInventory() != null && e.getClickedInventory().getType().equals(rec.getType())) {
                if (!EnumUtils.isValidEnum(RecyclItems.class, e.getCurrentItem().getType().toString())) {
                    p.sendMessage("EH ! Ce n'est pas un item valide !");
                    return;
                }
                int count = 10;
                while (true) {
                    if (i.getItem(count) == null || i.getItem(count).getType().equals(Material.AIR)) break;
                    else count++;
                    if (count > 16) {
                        p.sendMessage("EH ! Tu m'en donne trop !");
                        return;
                    }
                }
                if (clicked.getAmount() != 64) {
                    p.sendMessage("Eh ! Il n'y en as pas assez !");
                    return;
                }
                i.setItem(count, clicked);
                p.getInventory().setItem(e.getSlot(), null);
                e.getCursor().setType(Material.AIR);
                e.getCursor().setAmount(0);
            } else if (e.getClickedInventory().getType().equals(i.getType())) {
                if (e.getSlot() == 18) {
                    p.closeInventory();
                }
                if (e.getSlot() == 26) {
                    if (i.getItem(16) == null) {
                        p.sendMessage("Eh ! Il n'y en a pas assez  pour recycler !!");
                        return;
                    }
                    i.setContents(new ItemStack[]{});
                    p.closeInventory();
                    Random r = new Random();
                    if (r.nextFloat() <= 0.50f) {
                        p.sendMessage("§cTu a gagné le lot: §fn°1 -> §6Clé box C1");
                        Collection<ItemStack> iiis = p.getInventory().addItem(lotarybox.getKey("C1")).values();
                        iiis.forEach((ItemStack ill) -> {
                            p.getWorld().dropItem(p.getLocation(), ill);
                            p.getWorld().dropItem(p.getLocation(), ill);
                        });
                    } else if (r.nextFloat() <= 0.80f) {
                        p.sendMessage("§cTu a gagné le lot: §fn°2 -> §6Clé box C2");
                        Collection<ItemStack> iiis = p.getInventory().addItem(lotarybox.getKey("C2")).values();
                        iiis.forEach((ItemStack ill) -> {
                            p.getWorld().dropItem(p.getLocation(), ill);
                            p.getWorld().dropItem(p.getLocation(), ill);
                        });
                    } else if (r.nextFloat() <= 0.95f) {
                        p.sendMessage("§cTu a gagné le lot: §fn°3 -> §6Clé box C3");
                        Collection<ItemStack> iiis = p.getInventory().addItem(lotarybox.getKey("C3")).values();
                        iiis.forEach((ItemStack ill) -> {
                            p.getWorld().dropItem(p.getLocation(), ill);
                            p.getWorld().dropItem(p.getLocation(), ill);
                        });
                    } else {
                        p.sendMessage("§cTu a gagné le lot: §fn°4 -> §6Clé box C4");
                        Collection<ItemStack> iiis = p.getInventory().addItem(lotarybox.getKey("C4")).values();
                        iiis.forEach((ItemStack ill) -> {
                            p.getWorld().dropItem(p.getLocation(), ill);
                            p.getWorld().dropItem(p.getLocation(), ill);
                        });
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public final void onInvClosed(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory i = e.getView().getTopInventory();
        if (Recycleur.isrecycl(i)) {
            for (int cnt = 10; cnt < 16; cnt++) {
                if (i.getItem(cnt) != null) {
                    p.getInventory().addItem(new ItemStack(i.getItem(cnt).getType(), i.getItem(cnt).getAmount()));
                    p.updateInventory();
                }
            }
            p.sendMessage("Dommage");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public final void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (Icare.IcareUsers.contains(e.getEntity().getUniqueId())) {
                e.setDamage(0);
                Icare.IcareUsers.remove(e.getEntity().getUniqueId());
            }
        }
    }
}
