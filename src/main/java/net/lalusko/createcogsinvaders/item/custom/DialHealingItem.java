package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.item.ModItems;

public class DialHealingItem extends AbstractDialItem {
    public DialHealingItem(Properties p) { super(p, () -> ModItems.DIAL_REPAIR.get(), () -> ModItems.DIAL.get()); }
}

