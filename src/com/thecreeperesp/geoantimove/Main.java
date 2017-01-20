package com.thecreeperesp.geoantimove;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class Main extends JavaPlugin

        implements Listener
{
    ArrayList<UUID> frozenPlayers = new ArrayList();
    String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
    int freezeToggle = 0;
    HashMap<UUID, ItemStack> helmet = new HashMap();
    HashMap<UUID, Integer> playerFrozenTime = new HashMap();

    public void onEnable()
    {
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = Logger.getLogger("Minecraft");
        logger.info(pdfFile.getName() + " ha sido activado (v" + pdfFile.getVersion() + ")");

        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                for (Player players :  Bukkit.getOnlinePlayers())
                {
                    if (!Main.this.playerFrozenTime.containsKey(players.getUniqueId())) {
                        Main.this.playerFrozenTime.put(players.getUniqueId(), Integer.valueOf(0));
                    }
                    if (((Integer)Main.this.playerFrozenTime.get(players.getUniqueId())).intValue() != 0)
                    {
                        if (!Main.this.frozenPlayers.contains(players.getUniqueId())) {
                            Main.this.freezePlayer(players.getUniqueId());
                        } else {
                            Main.this.playerFrozenTime.replace(players.getUniqueId(), Integer.valueOf(((Integer)Main.this.playerFrozenTime.get(players.getUniqueId())).intValue() - 1));
                        }
                    }
                    else if ((((Integer)Main.this.playerFrozenTime.get(players.getUniqueId())).intValue() == 0) &&
                            (Main.this.frozenPlayers.contains(players.getUniqueId()))) {
                        Main.this.unFreezePlayer(players.getUniqueId());
                    }
                }
            }
        }, 0L, 20L);
    }

    public void onDisable()
    {
        for (UUID uuid : this.frozenPlayers)
        {
            Player player = Bukkit.getPlayer(uuid);
            player.getInventory().setHelmet((ItemStack)this.helmet.get(player.getUniqueId()));
        }
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = Logger.getLogger("Minecraft");
        logger.info(pdfFile.getName() + " ha sido desactivado (v" + pdfFile.getVersion() + ")");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if (this.frozenPlayers.contains(player.getUniqueId()))
        {
            Location to = event.getFrom();
            to.setPitch(event.getTo().getPitch());
            to.setYaw(event.getTo().getYaw());
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if ((player.hasPermission("geoantimove.use")) || (player.isOp())) {
            return;
        }
        if (this.frozenPlayers.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("commandsMsg")) + ".");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if (this.frozenPlayers.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("breakMsg")) + ".");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        if (this.frozenPlayers.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("placeMsg")) + ".");
        }
    }

    @EventHandler
    public void onInventoryOption(InventoryOpenEvent event)
    {
        Player player = (Player)event.getPlayer();
        if (this.frozenPlayers.contains(player.getUniqueId()))
        {
            InventoryType type = event.getInventory().getType();
            if (type == InventoryType.CHEST)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.ENDER_CHEST)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.HOPPER)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.PLAYER)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.FURNACE)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.ENCHANTING)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.MERCHANT)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.ANVIL)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.BEACON)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            else if (type == InventoryType.DROPPER)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("interactMsg")) + ".");
            }
            player.closeInventory();
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        if (this.frozenPlayers.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            player.closeInventory();
            event.getPlayer().sendMessage(
                    this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("dropItemMsg")));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        Player player = (Player)event.getWhoClicked();
        if (this.frozenPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if ((event.getEntity() instanceof Player))
        {
            Player player = (Player)event.getEntity();
            if (this.frozenPlayers.contains(player.getUniqueId()))
            {
                event.setCancelled(true);
                event.getDamager().sendMessage(this.prefix + ChatColor.RED + "No puedes atacar a un jugador congelado!");
            }
        }
        if ((event.getDamager() instanceof Player))
        {
            Player attacker = (Player)event.getDamager();
            if (this.frozenPlayers.contains(attacker.getUniqueId()))
            {
                event.setCancelled(true);
                attacker.sendMessage(this.prefix + ChatColor.RED + "No puedes atacar a un jugador estando congelado!");
            }
        }
    }

    public void unFreezePlayer(UUID uuid)
    {
        this.frozenPlayers.remove(uuid);
        this.playerFrozenTime.replace(uuid, Integer.valueOf(0));
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("geoantimove.notify")) {
                staff.sendMessage(this.prefix + ChatColor.GREEN + Bukkit.getPlayer(uuid).getName() + " " +
                        ChatColor.translateAlternateColorCodes('&', getConfig().getString("notifyUnfrozenMsg")) +
                        ".");
            }
        }
        Bukkit.getPlayer(uuid).sendMessage(
                this.prefix + ChatColor.translateAlternateColorCodes('&', new StringBuilder(String.valueOf(getConfig().getString("unfrozenMsg"))).append(".").toString()));
        Bukkit.getPlayer(uuid).getInventory().setHelmet((ItemStack)this.helmet.get(uuid));
    }

    public void freezePlayer(UUID uuid)
    {
        Player target = Bukkit.getPlayer(uuid);
        this.playerFrozenTime.put(uuid, Integer.valueOf(-1));
        this.frozenPlayers.add(uuid);
        int frozenTime = ((Integer)this.playerFrozenTime.get(uuid)).intValue();
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if ((staff.hasPermission("geoantimove.notify")) && (frozenTime > 0)) {
                staff.sendMessage(this.prefix + ChatColor.GREEN + target.getName() + " " +
                        ChatColor.translateAlternateColorCodes('&',
                                getConfig().getString("notifyFrozenMsg")) +
                        " por " + frozenTime + " segundos.");
            } else if ((staff.hasPermission("geoantimove.notify")) && (frozenTime < 1)) {
                staff.sendMessage(this.prefix + ChatColor.GREEN + target.getName() + " " +
                        ChatColor.translateAlternateColorCodes('&', getConfig().getString("notifyFrozenMsg")) +
                        ".");
            }
        }
        if (frozenTime > 1) {
            target.sendMessage(this.prefix +
                    ChatColor.translateAlternateColorCodes('&', getConfig().getString("frozenMsg")) +
                    " por " + frozenTime + " segundos.");
        } else if (frozenTime == 1) {
            target.sendMessage(this.prefix +
                    ChatColor.translateAlternateColorCodes('&', getConfig().getString("frozenMsg")) +
                    " por " + frozenTime + " segundo.");
        } else if (frozenTime < 1) {
            target.sendMessage(this.prefix +
                    ChatColor.translateAlternateColorCodes('&', getConfig().getString("frozenMsg")) +
                    ".");
        }
        this.helmet.put(target.getUniqueId(), target.getInventory().getHelmet());
        target.getInventory().setHelmet(new ItemStack(Material.ICE, 1));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if ((commandLabel.equalsIgnoreCase("freeze")) && ((sender.hasPermission("geoantimove.use")) || (sender.isOp())))
        {
            if (args.length == 0)
            {
                sender.sendMessage(this.prefix + ChatColor.BLUE + "Comandos:");
                sender.sendMessage(ChatColor.RED + "/freeze <name>" + ChatColor.WHITE + " - Congela a un jugador.");
                sender.sendMessage(ChatColor.RED + "/freeze temp <name> <segundos>" + ChatColor.WHITE +
                        " - Congela a un jugador temporalmente");
                sender.sendMessage(
                        ChatColor.RED + "/frozen <name>" + ChatColor.WHITE + " - Mira si ese jugador está congelado.");
                sender.sendMessage(ChatColor.RED + "/frozen list" + ChatColor.WHITE + " - Mira todos los jugadores congelados.");
                sender.sendMessage(ChatColor.RED + "/freeze all" + ChatColor.WHITE + " - Congela a todos los jugadores en linea.");
                sender.sendMessage(ChatColor.RED + "/freeze reload" + ChatColor.WHITE + " - Recarga el plugin.");
                sender.sendMessage(ChatColor.RED + "/freeze version" + ChatColor.WHITE + " - Mira la version del plugin.");
                return true;
            }
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("version"))
                {
                    if ((sender.hasPermission("geoantimove.use")) || (sender.isOp()))
                    {
                        sender.sendMessage(this.prefix + ChatColor.RED + "Version de GeoAntiMove es 3.0-SNAPSHOT");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("reload"))
                {
                    if ((sender.hasPermission("geoantimove.reload")) || (sender.isOp()))
                    {
                        reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "GeoAntiMove por TheCreeperESP y ByIsrael02 ha sido recargado!");
                        return true;
                    }
                    sender.sendMessage(this.prefix + ChatColor.RED + "Neccesitas el permiso 'geoantimove.reload' para ejecutar este comando.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("temp"))
                {
                    if ((sender.hasPermission("geoantimove.use")) || (sender.isOp()))
                    {
                        sender.sendMessage(this.prefix + ChatColor.RED + "Usa: /freeze temp <name> <segundos>");
                        return true;
                    }
                }
                else
                {
                    if (args[0].equalsIgnoreCase("all"))
                    {
                        if ((sender.hasPermission("geoantimove.use")) || (sender.isOp()))
                        {
                            if (this.freezeToggle == 0)
                            {
                                sender.sendMessage(this.prefix + ChatColor.YELLOW + "Todos los jugadores han sido congelaods!");
                                for (Player player : Bukkit.getOnlinePlayers())
                                {
                                    freezePlayer(player.getUniqueId());
                                    player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("frozenMsg")) + ".");
                                }
                                this.freezeToggle = 1;
                            }
                            else if (this.freezeToggle == 1)
                            {
                                sender.sendMessage(this.prefix + ChatColor.YELLOW + "Todos los jugadores han sido descongelados!");
                                for (Player player : Bukkit.getOnlinePlayers())
                                {
                                    unFreezePlayer(player.getUniqueId());
                                    player.sendMessage(this.prefix +
                                            ChatColor.translateAlternateColorCodes('&', getConfig().getString("unfrozenMsg")) + ".");
                                }
                                this.freezeToggle = 0;
                            }
                            return true;
                        }
                        sender.sendMessage(ChatColor.RED + "Neccesitas 'geoantimove.use' para usar ese comando.");
                        return true;
                    }
                    if ((sender.hasPermission("geoantimove.use")) || (sender.isOp()))
                    {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null)
                        {
                            sender.sendMessage(this.prefix + ChatColor.RED + "Tienes que especificar un jugador.");
                            return true;
                        }
                        UUID uuid = target.getUniqueId();
                        if (this.frozenPlayers.contains(uuid))
                        {
                            unFreezePlayer(uuid);
                            return true;
                        }
                        freezePlayer(uuid);

                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "Neccesitas 'geoantimove.use' para usar ese comando.");
                    return true;
                }
            }
            else if ((args.length == 3) &&
                    ((sender.hasPermission("geoantimove.use")) || (sender.isOp())) &&
                    (args[0].equalsIgnoreCase("temp")))
            {
                Player target1 = Bukkit.getPlayer(args[1]);
                if (target1 == null)
                {
                    sender.sendMessage(this.prefix + ChatColor.RED + "Tienes que especificar un jugador.");
                    return true;
                }
                if (this.playerFrozenTime.containsKey(target1.getUniqueId()))
                {
                    freezePlayer(target1.getUniqueId());
                    this.playerFrozenTime.replace(target1.getUniqueId(), Integer.valueOf(Integer.parseInt(args[2])));
                }
                else
                {
                    this.playerFrozenTime.put(target1.getUniqueId(), Integer.valueOf(Integer.parseInt(args[2])));
                }
                return true;
            }
        }
        if (commandLabel.equalsIgnoreCase("frozen"))
        {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if ((args.length == 1) && (args[0].equalsIgnoreCase("list")) && (
                    (sender.hasPermission("geoantimove.use")) || (sender.isOp())))
            {
                sender.sendMessage(this.prefix + ChatColor.BLUE + "Jugadores congelados:");
                for (UUID uuid : this.frozenPlayers)
                {
                    Player player = Bukkit.getPlayer(uuid);
                    String playerName = player.getName();
                    sender.sendMessage(ChatColor.GOLD + playerName);
                }
                return true;
            }
            if (args.length == 0)
            {
                sender.sendMessage(this.prefix + ChatColor.RED + "Tienes que especificar un jugador.");
                return true;
            }
            if (target == null)
            {
                sender.sendMessage(this.prefix + ChatColor.RED + "No se ha encontrado al jugador " + args[0] + ".");
                return true;
            }
            UUID uuid = target.getUniqueId();
            Player player = Bukkit.getPlayer(uuid);
            String playerName = player.getName();
            if (this.frozenPlayers.contains(uuid))
            {
                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (staff.hasPermission("geoantimove.use")) {
                        staff.sendMessage(this.prefix + ChatColor.GREEN + playerName + " está congelado.");
                    }
                }
                return true;
            }
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("geoantimove.use")) {
                    staff.sendMessage(this.prefix + ChatColor.GREEN + playerName + " no está congelado.");
                }
            }
            return true;
        }
        return false;
    }
}

