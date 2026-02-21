
package com.bilibili.player_ix.noixmod_api.commands;

import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class CommandMaths {
    public CommandMaths() {
    }

    static int nextBoolean(CommandSourceStack stack) {
        stack.sendSuccess(()-> Component.literal(String.valueOf(Maths.random.nextBoolean())), false);
        return 1;
    }

    static int nextFloat(CommandSourceStack stack) {
        stack.sendSuccess(()->Component.literal(String.valueOf(Maths.random.nextFloat())), false);
        return 1;
    }

    static int nextDouble(CommandSourceStack stack) {
        stack.sendSuccess(()->Component.literal(String.valueOf(Maths.random.nextDouble())), false);
        return 1;
    }

    static int nextInt(CommandSourceStack stack) {
        stack.sendSuccess(()->Component.literal(String.valueOf(Maths.random.nextInt())), false);
        return 1;
    }
}
