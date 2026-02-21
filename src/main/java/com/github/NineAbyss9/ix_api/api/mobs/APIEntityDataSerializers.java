
package com.github.NineAbyss9.ix_api.api.mobs;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class APIEntityDataSerializers {
    //public static final EntityDataSerializer<UUID> UUID_SERIALIZER;
    public static final EntityDataSerializer<ApiSpells.ApiSpell> API_SPELL;
    private APIEntityDataSerializers() {
    }

    static {
        /*UUID_SERIALIZER = new EntityDataSerializer.ForValueType<>() {
            @Override
            public void write(FriendlyByteBuf friendlyByteBuf, UUID uuid) {
                friendlyByteBuf.writeUtf(Integer.toString(uuid.hashCode()));
            }

            @Override
            public UUID read(FriendlyByteBuf friendlyByteBuf) {
                return UUID.fromString(friendlyByteBuf.readUtf());
            }
        };*/
        API_SPELL = new EntityDataSerializer.ForValueType<>() {
            @Override
            public void write(FriendlyByteBuf friendlyByteBuf, ApiSpells.ApiSpell apiSpell) {
                friendlyByteBuf.writeUtf(apiSpell.name());
            }

            @Override
            public ApiSpells.ApiSpell read(FriendlyByteBuf friendlyByteBuf) {
                return ApiSpells.ApiSpell.valueOf(friendlyByteBuf.readUtf());
            }
        };
        //EntityDataSerializers.registerSerializer(UUID_SERIALIZER);
        EntityDataSerializers.registerSerializer(API_SPELL);
    }
}
