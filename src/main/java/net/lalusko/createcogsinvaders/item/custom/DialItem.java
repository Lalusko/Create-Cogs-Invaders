package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.item.ModItems;

public class DialItem extends AbstractDialItem {
    public DialItem(Properties p) { super(p, () -> ModItems.DIAL_HEALING.get(), () -> ModItems.DIAL_REPAIR.get()); }
}
