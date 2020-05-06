# BiomeRTP
[![license](https://img.shields.io/github/license/BrendonCurmi/BiomeRTP)](https://github.com/BrendonCurmi/BiomeRTP/blob/master/LICENSE)
[![discord](https://discordapp.com/api/guilds/699764448155533404/widget.png)](https://discord.gg/VFNTycm)

This plugin allows players to randomly teleport across the world and to specific biome types.

After installing this plugin and starting up and loading the worlds for the first time, the scanner will start scanning
chunks from 0,0 outward to 10000x10000 chunks along the X and Z axes.

The scanner runs asynchronously to server functions, so the server is still usable and playable while the scan is running.
But it's recommended to leave the server idle during scanning for at least 5-20 minutes for a simple world and
30-60 minutes for bigger servers as the server may lag enough to crash if a single tick takes >60 seconds.

Due to the scope of how large the scan region is, the server will most likely be stopped before the scan has completely
finished. The scan will not restart upon starting the server in the future, unless the `config/biomertp` folder
(which contains the saved scanned data) is deleted.

Since this runs a scan on the world, it is recommended to take a backup of the world before installing the plugin.

## Commands and Permissions
| Command    | Permission                | Description                            |
|------------|---------------------------|----------------------------------------|
| /biomertp  | biomertp.command.biomertp | Teleports the player to a random biome |
| /rtp       | biomertp.command.rtp      | Teleports the player to a random spot  |
