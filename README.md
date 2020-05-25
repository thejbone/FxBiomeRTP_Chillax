# FxBiomeRTP
[![license](https://img.shields.io/github/license/BrendonCurmi/FxBiomeRTP)](https://github.com/BrendonCurmi/FxBiomeRTP/blob/master/LICENSE)
[![discord](https://discordapp.com/api/guilds/699764448155533404/widget.png)](https://discord.gg/VFNTycm)

This plugin allows players to randomly teleport across the world and to specific biome types.

## Installing
After [downloading the plugin](#download), place the jar file in the `mods` folder of the Sponge server.

## Scanning
To be able to teleport to biomes, the program will need to run a scan of the available world.

Running `/scans scan <world>` will start a scan of the specified world.

The scanner will start scanning chunks from 0,0 outward to 10000x10000 chunks along the X and Z axes, or up to the
world border. It is recommended to have set a world border limit prior to starting the scan.

The scanner runs asynchronously to server functions, so the server is still usable and playable while the scan is running.
It is recommended to wait until the scan has completely before shutting down the server, as this may corrupt the saved data.

Increasing the world border after the scan has started, will not increase the scan area, unless another scan is taken.

## Download
You can download the latest version from [Ore](https://ore.spongepowered.org/FusionDev/FxBiomeRTP)

## Commands and Permissions
| Command       | Permission                      | Description                              |
|---------------|---------------------------------|------------------------------------------|
| /biomertp     | fxbiomertp.command.biomertp     | Teleports the player to a random biome   |
| /rtp          | fxbiomertp.command.rtp          | Teleports the player to a random spot    |
| /scans        | fxbiomertp.command.scans        | Handles the world scans                  |
| /scans list   | fxbiomertp.command.scans.list   | Lists the scanned worlds                 |
| /scans scan   | fxbiomertp.command.scans.scan   | Scans the specified world                |
| /scans remove | fxbiomertp.command.scans.remove | Removes the scan for the specified world |

## Rescanning
To rescan a world, simple run `/scans scan <world>` for the world.

Running a scan on a world that has already been scanned will delete the previous data and scan the world from scratch.
