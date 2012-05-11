package org.kitteh.vanish.hooks;

import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.Messages;
import org.kitteh.vanish.VanishPlugin;

import com.alecgorge.minecraft.jsonapi.JSONAPI;

public class JSONAPIHook extends Hook {

    public JSONAPIHook(VanishPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        final Plugin grab = this.plugin.getServer().getPluginManager().getPlugin("JSONAPI");
        if ((grab != null) && (grab instanceof JSONAPI)) {
            ((JSONAPI) grab).registerMethod("{   \"name\": \"isVanished\",   \"desc\": \"Checks if named player is vanished\",   \"call\": \"Plugins.VanishNoPacket.getManager().isVanished(0)\",  \"returns\": [\"boolean\", \"True if player is invisible. False if offline or visible.\"],   \"args\": [       [\"String\", \"Player's name\"]   ]}");
            this.plugin.log(Messages.getString("JSONAPIHook.hookingIntoJSONAPI"));
        } else {
            this.plugin.log(Messages.getString("JSONAPIHook.noJSONAPI"));
            return;
        }
    }

}
