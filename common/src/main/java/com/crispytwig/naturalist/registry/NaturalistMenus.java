package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import com.crispytwig.naturalist.world.inventory.ElephantInventoryMenu;

@SuppressWarnings("unused")
public class NaturalistMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU.key(), Naturalist.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ElephantInventoryMenu>> ELEPHANT = MENUS.register("elephant",
            () -> new MenuType<>(ElephantInventoryMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
