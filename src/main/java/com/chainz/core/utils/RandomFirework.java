package com.chainz.core.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomFirework {
    private static final List<FireworkEffect.Type> types = new ArrayList<>(Arrays.asList(
            FireworkEffect.Type.BURST,
            FireworkEffect.Type.BALL,
            FireworkEffect.Type.BALL_LARGE,
            FireworkEffect.Type.CREEPER,
            FireworkEffect.Type.STAR
    ));

    private static final Random random = new Random();

    private static FireworkEffect.Type getRandomType() {
        int i = types.size();
        return types.get(random.nextInt(i));
    }

    public static void launchRandomFirework(Location loc) {
        loc = loc.getWorld().getHighestBlockAt(loc).getLocation();
        Firework fw = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta localFireworkMeta = fw.getFireworkMeta();
        localFireworkMeta.setPower(2);

        localFireworkMeta
                .addEffects(FireworkEffect.builder().flicker(true).with(getRandomType())
                        .withColor(Color.PURPLE, Color.PURPLE).withFade(Color.FUCHSIA).build());

        fw.setFireworkMeta(localFireworkMeta);
    }
}