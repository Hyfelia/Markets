package me.truemb.rentit.utils;

import java.util.UUID;

import org.bukkit.entity.Player;
import me.truemb.rentit.main.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import net.milkbowl.vault.permission.Permission;

public class LuckPermsAPI {
	
	private LuckPerms luckPerms;
	private Permission permission;
	private Main instance;
	
	public LuckPermsAPI(Main plugin) {
		this.instance = plugin;
		
		try {
			LuckPerms api = LuckPermsProvider.get();
			this.luckPerms = api;
			this.instance.getLogger().info("LuckPerms Permission System was found.");
		}catch(IllegalStateException ex){
			return;
		}
	}
	
	public String getPrimaryGroup(UUID uuid) {
		User user = this.getLuckPerms().getUserManager().getUser(uuid);
		return user.getPrimaryGroup();
	}
	
	public String getPrefix(UUID uuid) {
		User user = this.getLuckPerms().getUserManager().getUser(uuid);
		return user.getCachedData().getMetaData().getPrefix();
	}
	
	public void addGroup(UUID uuid, String groupS) {
		Group group = this.getLuckPerms().getGroupManager().getGroup(groupS);
		User user = this.getLuckPerms().getUserManager().getUser(uuid);
		InheritanceNode node = InheritanceNode.builder(groupS).build();
		user.data().add(node);
		this.getLuckPerms().getUserManager().saveUser(user);
		this.getLuckPerms().getGroupManager().saveGroup(group);
	}
	
	public void removeGroup(UUID uuid, String groupS) {
		Group group = this.getLuckPerms().getGroupManager().getGroup(groupS);
		User user = this.getLuckPerms().getUserManager().getUser(uuid);
		InheritanceNode node = InheritanceNode.builder(groupS).build();
		user.data().remove(node);
		this.getLuckPerms().getUserManager().saveUser(user);
		this.getLuckPerms().getGroupManager().saveGroup(group);
	}
	
	public boolean isPlayerInGroup(Player player, String group) {
		return player.hasPermission("group." + group);
	}
	
	public boolean hasPermission(UUID uuid, String permission) {
		ContextManager cm = this.getLuckPerms().getContextManager();
		User user = this.getLuckPerms().getUserManager().getUser(uuid);
			
		QueryOptions queryOptions = cm.getQueryOptions(user).orElse(cm.getStaticQueryOptions());
		CachedPermissionData permissionData = user.getCachedData().getPermissionData(queryOptions);
		return permissionData.checkPermission(permission).asBoolean();
	}
	
	public boolean addPlayerPermission(UUID uuid, String permission) {

		User user = this.getLuckPerms().getUserManager().getUser(uuid);
		this.getLuckPerms().getUserManager().saveUser(user);
		DataMutateResult result = user.data().add(Node.builder(permission).build());
		this.getLuckPerms().getUserManager().saveUser(user);
		return result.wasSuccessful();
	}

	public LuckPerms getLuckPerms() {
		return this.luckPerms;
	}
	
	public Permission getPerms() {
		return this.permission;
	}
}
