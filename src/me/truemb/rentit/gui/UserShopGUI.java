package me.truemb.rentit.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.truemb.rentit.enums.RentTypes;
import me.truemb.rentit.handler.CategoryHandler;
import me.truemb.rentit.handler.RentTypeHandler;
import me.truemb.rentit.main.Main;

public class UserShopGUI {

	public static Inventory getSellInv(Main instance, int shopId, ItemStack[] contents) {

		RentTypeHandler rentHandler = instance.getMethodes().getTypeHandler(RentTypes.SHOP, shopId);

		if (rentHandler == null)
			return null;

		CategoryHandler catHandler = instance.getMethodes().getCategory(RentTypes.SHOP, rentHandler.getCatID());

		if (catHandler == null)
			return null;

		String ownerName = rentHandler.getOwnerName();
		int size = catHandler.getSize();

		Inventory inv = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', instance.manageFile().getString("GUI.shopUser.displayNameSell") + " " + ownerName));

		if (contents != null) {

			int puf = 0;
			if (contents != null)
				for (int i = 0; i < inv.getSize() && i < contents.length; i++) {

					ItemStack item = contents[i];
					if (item == null || item.getType() == Material.AIR) {
							puf++;
						continue;
					}
					
					ItemMeta meta = item.getItemMeta();
					
					NamespacedKey idKey = new NamespacedKey(instance, "ID");
					PersistentDataContainer container = meta.getPersistentDataContainer();
					
					if(!container.has(idKey , PersistentDataType.INTEGER))
						meta.getPersistentDataContainer().set(idKey, PersistentDataType.INTEGER, shopId);
					
					item.setItemMeta(meta);
					
					inv.setItem(i - puf, item);

				}
		}
		return inv;
	}

	public static Inventory getBuyInv(Main instance, int shopId, ItemStack[] contents) {

		RentTypeHandler rentHandler = instance.getMethodes().getTypeHandler(RentTypes.SHOP, shopId);

		if (rentHandler == null)
			return null;

		CategoryHandler catHandler = instance.getMethodes().getCategory(RentTypes.SHOP, rentHandler.getCatID());

		if (catHandler == null)
			return null;

		String ownerName = rentHandler.getOwnerName();
		int size = catHandler.getSize();

		Inventory inv = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', instance.manageFile().getString("GUI.shopUser.displayNameBuy") + " " + ownerName));

		if (contents != null) {

			int puf = 0;
			if (contents != null)
				for (int i = 0; i < inv.getSize() && i < contents.length; i++) {
					
					ItemStack item = contents[i];
					if (item == null || item.getType() == Material.AIR) {
						puf++;
						continue;
					}
					
					ItemMeta meta = item.getItemMeta();
					
					NamespacedKey idKey = new NamespacedKey(instance, "ID");
					PersistentDataContainer container = meta.getPersistentDataContainer();
					
					if(!container.has(idKey , PersistentDataType.INTEGER))
						meta.getPersistentDataContainer().set(idKey, PersistentDataType.INTEGER, shopId);
					
					item.setItemMeta(meta);
					
					inv.setItem(i - puf, item);

				}
		}
		return inv;
	}
}
