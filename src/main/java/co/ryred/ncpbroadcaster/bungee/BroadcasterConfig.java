package co.ryred.ncpbroadcaster.bungee;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BroadcasterConfig {

	private Plugin plugin;
	private File file;
	private Configuration config;

	public BroadcasterConfig( Plugin plugin ) {
		this.plugin = plugin;
		this.file = new File( this.getDataFolder(), "config.yml" );
		if ( !this.getDataFolder().exists() ) {
			this.getDataFolder().mkdir();
		}

		if ( !this.file.exists() ) {
			try {
				this.file.createNewFile();
				InputStream e = this.getResourceAsStream();
				Throwable throwable = null;

				try {
					FileOutputStream os = new FileOutputStream( this.file );
					Throwable throwable1 = null;

					try {
						ByteStreams.copy( e, os );
					} catch ( Throwable throwable2 ) {
						throwable1 = throwable2;
						throw throwable2;
					} finally {
						if ( os != null ) {
							if ( throwable1 != null ) {
								try {
									os.close();
								} catch ( Throwable throwable3 ) {
									throwable1.addSuppressed( throwable3 );
								}
							} else {
								os.close();
							}
						}

					}
				} catch ( Throwable throwable4 ) {
					throwable = throwable4;
					throw throwable4;
				} finally {
					if ( e != null ) {
						if ( throwable != null ) {
							try {
								e.close();
							} catch ( Throwable throwable5 ) {
								throwable.addSuppressed( throwable5 );
							}
						} else {
							e.close();
						}
					}

				}
			} catch ( IOException ioexception ) {
				throw new RuntimeException( "Unable to create config file", ioexception );
			}
		}

	}

	public Configuration getConfig() {
		try {
			return this.config == null ? ( this.config = ConfigurationProvider.getProvider( YamlConfiguration.class ).load( this.file ) ) : this.config;
		} catch ( IOException ioexception ) {
			ioexception.printStackTrace();
			return null;
		}
	}

	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider( YamlConfiguration.class ).save( this.config, new File( this.getDataFolder(), "config.yml" ) );
		} catch ( IOException ioexception ) {
			ioexception.printStackTrace();
			this.plugin.getLogger().severe( "Couldn\'t save config file!" );
		}

	}

	private File getDataFolder() {
		return this.plugin.getDataFolder();
	}

	private InputStream getResourceAsStream() {
		return this.plugin.getResourceAsStream( "config.yml" );
	}
}
