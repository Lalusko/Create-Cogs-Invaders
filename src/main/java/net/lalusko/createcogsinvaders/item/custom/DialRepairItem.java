package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.item.ModItems;

public class DialRepairItem extends AbstractDialItem {
    public DialRepairItem(Properties p) { super(p, () -> ModItems.DIAL.get(), () -> ModItems.DIAL_HEALING.get()); }
}
