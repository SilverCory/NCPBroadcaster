package co.ryred.ncpbroadcaster.bungee;

import co.ryred.uuidcredits.BungeeListener;
import co.ryred.uuidcredits.Credits;
import lombok.Getter;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class NCPBroadcasterPlugin extends Plugin implements Listener {

	@Getter
	private BroadcasterConfig config;
	@Getter
	private HashSet<String> admins = new HashSet<>();
	@Getter
	private HashSet<UUID> adminsUuid = new HashSet<>();

	public void onEnable() {

		final int port = getProxy().getConfig().getListeners().iterator().next().getHost().getPort();
		getLogger().info( "Loading the config." );
		config = new BroadcasterConfig( this );
		getAdmins().addAll( this.getConfig().getConfig().getStringList( "admins" ) );
		Iterator iterator = this.getConfig().getConfig().getStringList( "admins-uuid" ).iterator();

		while ( iterator.hasNext() ) {
			String uuidStr = ( String ) iterator.next();

			try {
				uuidStr = uuidStr.replace( "-", "" );
				this.getAdminsUuid().add( Util.getUUID( uuidStr ) );
			} catch ( Exception exception ) {
				exception.printStackTrace();
				( new Exception( uuidStr + " is not a valid uuid!" ) ).printStackTrace();
			}
		}

		this.getLogger().info( "Loaded the config." );
		this.getProxy().registerChannel( "NCPBroadcasterPlugin" );
		this.getProxy().getPluginManager().registerListener( this, this );
	}

	@EventHandler
	public void onPluginMessage( PluginMessageEvent e ) {
		if ( e.getTag().equals( "NCPBroadcasterPlugin" ) ) {
			try {
				ByteArrayInputStream ex = new ByteArrayInputStream( e.getData() );
				DataInputStream dis = new DataInputStream( ex );
				String message = dis.readUTF();

				if ( message == null ) {
					throw new Exception( "The message was empty." );
				}

				this.sendToAdmins( message, e.getSender() );
			} catch ( Exception exception ) {
				exception.printStackTrace();
			}
		}

	}

	private void sendToAdmins( String message, Connection sender ) {

		String msg = getConfig().getConfig().getString( "broadcast-format", "&9[&e%SERVER%&9] &r%MESSAGE%" );
		msg = ChatColor.translateAlternateColorCodes( '&', msg ).replace( "%MESSAGE%", message );

		ServerInfo si = null;
		if ( sender instanceof Server ) {
			Server srv = ( Server ) sender;
			si = srv.getInfo();
		}

		msg = msg.replace( "%SERVER%", si != null && si.getName() != null ? si.getName() : "UNKNOWN" );

		for ( ProxiedPlayer player : getProxy().getPlayers() ) {
			if ( ( this.getAdmins().contains( player.getName().toLowerCase() ) || this.getAdminsUuid().contains( player.getUniqueId() ) ) && ( si == null || !si.equals( player.getServer().getInfo() ) ) ) {
				player.sendMessage( msg );
			}
		}

	}

}
