package wtf.melonthedev.log4minecraft.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationSerializableAdapter implements JsonSerializer<ConfigurationSerializable>, JsonDeserializer<ConfigurationSerializable> {

    final Type objectStringMapType = new TypeToken<Map<String, Object>>() {}.getType();

    @Override
    public ConfigurationSerializable deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException
    {
        final Map<String, Object> map = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            final JsonElement value = entry.getValue();
            final String name = entry.getKey();

            System.out.println(name + " -- " + value + " - " + value + " - " + value.getClass());
            if (value.isJsonObject() && value.getAsJsonObject().has(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                map.put(name, this.deserialize(value, value.getClass(), context));
            //} else if (name.equalsIgnoreCase("enchants")) {
            //    map.put(name, deserializeItemMeta((Map<String, Object>) value));
                //map.put(name, context.deserialize(value, ItemMeta.class));
            } else {
                map.put(name, context.deserialize(value, Object.class));
            }
        }
        return ConfigurationSerialization.deserializeObject(map);
    }

    public static ItemStack deserialise(Map<String, Object> map) {
        map.computeIfPresent("meta", ($, serialised) -> (ItemMeta) ConfigurationSerialization.deserializeObject((Map<String, Object>) serialised));
        return ItemStack.deserialize(map);
    }

    public static ItemMeta deserializeItemMeta(Map<String, Object> map) {

        ItemMeta meta = null;

        try {
            Class[] craftMetaItemClasses = Class.forName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem").getDeclaredClasses();

            for (Class craftMetaItemClass : craftMetaItemClasses) {
                if (!craftMetaItemClass.getSimpleName().equals("SerializableMeta"))
                    continue;

                Method deserialize = craftMetaItemClass.getMethod("deserialize", Map.class);
                meta = (ItemMeta) deserialize.invoke(null, map);

                if (map.containsKey("enchants")) {

                    String string = map.get("enchants").toString().replace("{", "").replace("}", "").replace("\"", "");

                    for (String s : string.split(",")) {
                        String[] split = s.split(":");
                        Enchantment enchantment = Enchantment.getByName(split[0]);
                        int level = Integer.parseInt(split[1]);
                        meta.addEnchant(enchantment, level, true);
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return meta;

    }

    @Override
    public JsonElement serialize(
            ConfigurationSerializable src,
            Type typeOfSrc,
            JsonSerializationContext context)
    {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(src.getClass()));
        map.putAll(src.serialize());
        return context.serialize(map, objectStringMapType);
    }
}