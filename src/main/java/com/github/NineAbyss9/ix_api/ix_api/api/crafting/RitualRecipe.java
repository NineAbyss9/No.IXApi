
package com.github.NineAbyss9.ix_api.ix_api.api.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RitualRecipe extends ShapeRecipe implements Recipe<CraftingContainer> {
    //public static final RitualSerializer SERIALIZER = new RitualSerializer();
    private final String craftType;
    private final int summonLifeTime;
    @Nullable
    private final TagKey<EntityType<?>> entityToRitual;
    @Nullable
    private final TagKey<EntityType<?>> entityToSummon;
    private final int cost;
    private final int ritualTime;
    public RitualRecipe(ResourceLocation location, String s, ItemStack stack, NonNullList<Ingredient> list,
                        String pCraftType,
                        int pSummonLifeTime,
                        @Nullable TagKey<EntityType<?>> entityToRitual,
                        @Nullable TagKey<EntityType<?>> entityToSummon,
                        int pCost,
                        int pRitualTime) {
        super(location, s, CraftingBookCategory.MISC, stack, list);
        this.craftType = pCraftType;
        this.summonLifeTime = pSummonLifeTime;
        this.entityToRitual = entityToRitual;
        this.entityToSummon = entityToSummon;
        this.cost = pCost;
        this.ritualTime = pRitualTime;
    }

    public RecipeSerializer<?> getSerializer() {
        return null;//SERIALIZER;
    }

    public boolean matches(CraftingContainer p_44262_, Level p_44263_) {
        return false;
    }

    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return null;
    }

    /*@MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public static class RitualSerializer implements RecipeSerializer<RitualRecipe> {
        public RitualRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            String craftType = GsonHelper.getAsString(json, "craftType", "");
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(json,
                    "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            ResourceLocation ritualType = new ResourceLocation(json.get("ritual_type").getAsString());
            EntityType<?> entityToSummon = null;
            if (json.has("entity_to_summon")) {
                entityToSummon = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(GsonHelper.getAsString(json, "entity_to_summon")));
            }
            JsonElement activationItemElement =
                    GsonHelper.isArrayNode(json, "activation_item") ? GsonHelper.getAsJsonArray(json,
                            "activation_item") : GsonHelper.getAsJsonObject(json, "activation_item");
            Ingredient activationItem = Ingredient.fromJson(activationItemElement);
            int duration = GsonHelper.getAsInt(json, "duration", 30);
            int summonLife = GsonHelper.getAsInt(json, "summonLife", -1);
            int soulCost = GsonHelper.getAsInt(json, "soulCost", 0);
            TagKey<EntityType<?>> entityToSacrifice = null;
            String entityToSacrificeDisplayName = "";
            if (json.has("entity_to_sacrifice")) {
                var tagRL = new ResourceLocation(GsonHelper.getAsString(json.getAsJsonObject("entity_to_sacrifice"), "tag"));
                entityToSacrifice = TagKey.create(Registries.ENTITY_TYPE, tagRL);
                entityToSacrificeDisplayName = json.getAsJsonObject("entity_to_sacrifice").get("display_name").getAsString();
            }
            EntityType<?> entityToConvertInto = null;
            TagKey<EntityType<?>> entityToConvert = null;
            String entityToConvertDisplayName = "";
            String research = "";
            Enchantment enchantment = null;
            int xpLevelCost = 0;
            if (json.has("entity_to_convert")){
                var tagRL = new ResourceLocation(GsonHelper.getAsString(json.getAsJsonObject("entity_to_convert"), "tag"));
                entityToConvert = TagKey.create(Registries.ENTITY_TYPE, tagRL);
                entityToConvertDisplayName = json.getAsJsonObject("entity_to_convert").get("display_name").getAsString();
            }
            if (json.has("entity_to_convert_into")) {
                entityToConvertInto = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(GsonHelper.getAsString(json, "entity_to_convert_into")));
            }
            if (json.has("enchantment")){
                enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "enchantment")));
                xpLevelCost = GsonHelper.getAsInt(json, "xpLevelCost", 0);
            }
            if (json.has("research")){
                research = GsonHelper.getAsString(json, "research", "");
            }
            return new RitualRecipe(recipeId, group, craftType, ritualType,
                    result, entityToSummon, entityToConvertInto, activationItem, ingredients, duration,
                    summonLife, soulCost, entityToSacrifice, entityToSacrificeDisplayName,
                    entityToConvert, entityToConvertDisplayName, enchantment, xpLevelCost);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }

        @Nullable
        public RitualRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapeRecipe recipe = serializer.fromNetwork(recipeId, buffer);
            if (recipe == null){
                return null;
            }
            String craftType = buffer.readUtf(32767);
            ResourceLocation ritualType = buffer.readResourceLocation();
            EntityType<?> entityToSummon = null;
            if (buffer.readBoolean()) {
                entityToSummon = buffer.readRegistryId();
            }
            int duration = buffer.readVarInt();
            int summonLife = buffer.readVarInt();
            int soulCost = buffer.readVarInt();
            Ingredient activationItem = Ingredient.fromNetwork(buffer);
            TagKey<EntityType<?>> entityToSacrifice = null;
            String entityToSacrificeDisplayName = "";
            if (buffer.readBoolean()) {
                var tagRL = buffer.readResourceLocation();
                entityToSacrifice = TagKey.create(Registries.ENTITY_TYPE, tagRL);
                entityToSacrificeDisplayName = buffer.readUtf();
            }
            EntityType<?> entityToConvertInto = null;
            TagKey<EntityType<?>> entityToConvert = null;
            String entityToConvertDisplayName = "";
            if (buffer.readBoolean()) {
                var tagRL = buffer.readResourceLocation();
                entityToConvert = TagKey.create(Registries.ENTITY_TYPE, tagRL);
                entityToConvertDisplayName = buffer.readUtf();
            }
            if (buffer.readBoolean()){
                entityToConvertInto = buffer.readRegistryId();
            }
            Enchantment enchantment = null;
            int xpLevelCost = 0;
            if (buffer.readBoolean()){
                enchantment = buffer.readRegistryId();
                xpLevelCost = buffer.readVarInt();
            }
            String research = "";
            if (buffer.readBoolean()){
                research = buffer.readUtf(32767);
            }
            return new RitualRecipe(recipe.getId(), recipe.getGroup(), craftType, ritualType, recipe.getResultItem(null), entityToSummon, entityToConvertInto,
                    activationItem, recipe.getIngredients(), duration, summonLife, soulCost, entityToSacrifice, entityToSacrificeDisplayName, entityToConvert, entityToConvertDisplayName, enchantment, xpLevelCost, research);
        }

        public void toNetwork(FriendlyByteBuf buffer, RitualRecipe recipe) {
            serializer.toNetwork(buffer, recipe);
            buffer.writeUtf(recipe.craftType);
            buffer.writeResourceLocation(recipe.ritualType);
            buffer.writeBoolean(recipe.entityToSummon != null);
            if (recipe.entityToSummon != null) {
                buffer.writeRegistryId(ForgeRegistries.ENTITY_TYPES, recipe.entityToSummon);
            }
            buffer.writeVarInt(recipe.summonLifeTime);
            buffer.writeVarInt(recipe.cost);
            buffer.writeVarInt(recipe.ritualTime);
            buffer.writeBoolean(recipe.entityToSacrifice != null);
            if (recipe.entityToSacrifice != null) {
                buffer.writeResourceLocation(recipe.entityToSacrifice.location());
                buffer.writeUtf(recipe.entityToSacrificeDisplayName);
            }
            buffer.writeBoolean(recipe.entityToConvert != null);
            if (recipe.entityToConvert != null){
                buffer.writeResourceLocation(recipe.entityToConvert.location());
                buffer.writeUtf(recipe.entityToConvertDisplayName);
            }
            buffer.writeBoolean(recipe.entityToConvertInto != null);
            if (recipe.entityToConvertInto != null) {
                buffer.writeRegistryId(ForgeRegistries.ENTITY_TYPES, recipe.entityToConvertInto);
            }
            buffer.writeBoolean(recipe.enchantment != null);
            if (recipe.enchantment != null) {
                buffer.writeRegistryId(ForgeRegistries.ENCHANTMENTS, recipe.enchantment);
                buffer.writeVarInt(recipe.xpLevelCost);
            }
        }
    }*/
}
