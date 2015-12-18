package co.ryred.ncpbroadcaster.bukkit;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Set;

public class BroadcasterSender implements Permissible, CommandSender {

	private final NCPBroadcasterPlugin plugin;

	public BroadcasterSender( NCPBroadcasterPlugin plugin ) {
		this.plugin = plugin;
	}

	public void sendMessage( String s ) {
		Collection onlinePlayers = this.plugin.getServer().getOnlinePlayers();

		if ( onlinePlayers.isEmpty() ) {
			this.plugin.getLogger().warning( "Attempted to broadcast message but there are no players online!" );
			this.plugin.getLogger().warning( "  Message: \"" + s + "\"." );
		} else {
			Player player = ( Player ) onlinePlayers.iterator().next();

			if ( player == null ) {
				this.plugin.getLogger().warning( "Attempted to broadcast message but there are no players online!" );
				this.plugin.getLogger().warning( "  Message: \"" + s + "\"." );
			} else {
				try {
					player.sendPluginMessage( this.plugin, "NCPBroadcasterPlugin", NCPBroadcasterPlugin.convertString( s ) );
				} catch ( Exception exception ) {
					exception.printStackTrace();
				}
			}
		}

	}

	public void sendMessage( String[] strings ) {
		String[] astring = strings;
		int i = strings.length;

		for ( int j = 0; j < i; ++j ) {
			String s = astring[ j ];

			this.sendMessage( s );
		}

	}

	public Server getServer() {
		return this.plugin.getServer();
	}

	public String getName() {
		return "NCPBroadcaster";
	}

	public boolean isPermissionSet( String s ) {
		return s.equalsIgnoreCase( "nocheatplus.admin.notify" ) || s.equalsIgnoreCase( "nocheatplus.notify" );
	}

	public boolean isPermissionSet( Permission permission ) {
		return permission.getName().equalsIgnoreCase( "nocheatplus.admin.notify" ) || permission.getName().equalsIgnoreCase( "nocheatplus.notify" );
	}

	public boolean hasPermission( String s ) {
		return s.equalsIgnoreCase( "nocheatplus.admin.notify" ) || s.equalsIgnoreCase( "nocheatplus.notify" );
	}

	public boolean hasPermission( Permission permission ) {
		return permission.getName().equalsIgnoreCase( "nocheatplus.admin.notify" ) || permission.getName().equalsIgnoreCase( "nocheatplus.notify" );
	}

	public PermissionAttachment addAttachment( Plugin plugin, String s, boolean b ) {
		return null;
	}

	public PermissionAttachment addAttachment( Plugin plugin ) {
		return null;
	}

	public PermissionAttachment addAttachment( Plugin plugin, String s, boolean b, int i ) {
		return null;
	}

	public PermissionAttachment addAttachment( Plugin plugin, int i ) {
		return null;
	}

	public void removeAttachment( PermissionAttachment permissionAttachment ) {
	}

	public void recalculatePermissions() {
	}

	public Set getEffectivePermissions() {
		return null;
	}

	public boolean isOp() {
		return true;
	}

	public void setOp( boolean b ) {
	}
}
