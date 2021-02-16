package asm.asheep.plugin.tanker;

import org.bukkit.plugin.java.JavaPlugin;

public final class TankerPlugin extends JavaPlugin
{

    public static TankerPlugin instance;

    public TankerPlugin getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;
        getServer().getPluginManager().registerEvents(new TankerListener(this), this);

    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
