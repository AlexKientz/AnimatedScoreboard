package fr.harrysto.main;

import java.io.File;
import java.lang.reflect.Field;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class main extends JavaPlugin implements Listener {
    private boolean titlechanged = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File configFile = new File("plugins/Julien/config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
        //this.getServer().getPluginManager().registerEvents(this, this);
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    Field a = packet.getClass().getDeclaredField("a");
                    a.setAccessible(true);
                    Field b = packet.getClass().getDeclaredField("b");
                    b.setAccessible(true);

                    Object header1 = new ChatComponentText(getConfig().getString("header1"));
                    Object header2 = new ChatComponentText(getConfig().getString("header2"));

                    Object footer = new ChatComponentText(getConfig().getString("online") + Bukkit.getServer().getOnlinePlayers().size());
                    if (titlechanged) {
                        a.set(packet, header2);
                        titlechanged = false;

                    } else {
                        a.set(packet, header1);
                        titlechanged = true;
                    }
                    b.set(packet, footer);

                    if (Bukkit.getOnlinePlayers().size() == 0) return;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

}