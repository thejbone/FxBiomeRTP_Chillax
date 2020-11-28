# FxBiomeRTP
[![license](https://img.shields.io/github/license/BrendonCurmi/FxBiomeRTP)](https://github.com/BrendonCurmi/FxBiomeRTP/blob/master/LICENSE)
[![discord](https://discordapp.com/api/guilds/699764448155533404/widget.png)](https://discord.gg/VFNTycm)

This plugin allows players to randomly teleport across the world and to specific biome types.

## Installing
After [downloading the plugin](#download), place the jar file in the `mods` folder of the Sponge server.

## Scanning
To be able to teleport to biomes, the plugin will need to run a scan of the available world.

Running `/scans scan <world>` will start a scan of the specified world.

The scanner will start scanning chunks from origin (0,0) outward to 1000x1000 chunks along the X and Z axes, or up to the
world border. Hence, it is recommended to have set a world border limit prior to starting the scan.

The scanner runs asynchronously to server functions, so the server is still usable and playable while the scan is running.
It is recommended to wait until the scan has completed before shutting down the server, as this may corrupt the saved data.

Increasing the world border after the scan has started, will not increase the scan area, unless another scan is taken.

## Download
You can download the latest version from [Ore](https://ore.spongepowered.org/FusionDev/FxBiomeRTP)

## Commands and Permissions
| Command                       | Permission                        | Description                              |
|-------------------------------|-----------------------------------|------------------------------------------|
| /biomertp (biome)             | fxbiomertp.command.biomertp       | Teleports the player to a random biome   |
| /biomertp (biome) (player)    | fxbiomertp.command.admin.biomertp | Teleports the player to a random biome   |
| /rtp                          | fxbiomertp.command.rtp            | Teleports the player to a random spot    |
| /rtp (player)                 | fxbiomertp.command.admin.rtp      | Teleports the player to a random spot    |
| /scans                        | fxbiomertp.command.scans          | Handles the world scans                  |
| /scans list                   | fxbiomertp.command.scans.list     | Lists the scanned worlds                 |
| /scans scan                   | fxbiomertp.command.scans.scan     | Scans the specified world                |
| /scans remove                 | fxbiomertp.command.scans.remove   | Removes the scan for the specified world |

## Config
The config file is saved to `config/fxbiomertp/fxbiomertp.conf`

You can configure cooldowns for the below commands.
If the player has the below permissions, they will skip the cooldown.

| Command   | Permission                   |
|-----------|------------------------------|
| /biomertp | fxbiomertp.cooldown.biomertp |
| /rtp      | fxbiomertp.cooldown.rtp      |

To disable a cooldown entirely, just set the config value to 0.

## Rescanning
To rescan a world, simple run `/scans scan <world>` for the world.

Running a scan on a world that has already been scanned will delete the previous data and scan the world from scratch.
