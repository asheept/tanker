package asm.asheep.plugin.tanker;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;

public class TankerListener implements Listener
{
    private final TankerPlugin plugin;

    public HashSet<Player> tankerHash = new HashSet();
    public HashMap<Player, Integer> deathHash = new HashMap<>();
    TankerListener(TankerPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if(tankerHash.contains(event.getEntity()))
        {
            if(event.getCause() != EntityDamageEvent.DamageCause.FALL)
            {
                event.setCancelled(true);
            }
        }
        Entity entity = event.getEntity();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if(player != null)
            {
                double health = player.getHealth();
                double damage = event.getFinalDamage();

                if(event instanceof EntityDamageByEntityEvent)
                {
                    Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                    if(damager instanceof  Player)
                    {
                        Player damagerPlayer = (Player)damager;
                        if(damagerPlayer != null)
                        {
                            if(health <= damage)
                            {
                                event.setCancelled(true);
                                player.sendMessage("당신은 죽었습니다");
                                damagerPlayer.sendMessage("당신은 " + player.getName() + "을 죽였습니다");
                                setDeath(player);
                            }
                        }
                    }
                }
            }
        }
    }

    public void setTanker(Player player)
    {
        player.setHealth(60D);
    }

    public void setDeath(Player player)
    {
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(player.getMaxHealth());
        deathHash.put(player, 5);

        new BukkitRunnable()
        {
            int count = deathHash.get(player);

            @Override
            public void run()
            {
                if(deathHash.containsKey(player))
                {
                    --count;
                    player.sendActionBar("당신은 죽었습니다 " + count + "초");

                    if(count <= 0)
                    {
                        player.sendMessage("부활");
                        player.setGameMode(GameMode.SURVIVAL);
                        deathHash.remove(player);
                    }
                }
            }
        }.runTaskTimer(plugin.getInstance(), 0L, 20L);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        Player player = (Player)event.getEntity();

        player.setFoodLevel(20);
        player.setSaturation(20.0F);
    }

}
