# Admin Tools
Administration tools for minecraft servers.
Features :

 - Dark mode
 - Rcon client
 - Server status
 - Minecraft services status (eg. authserver, api...)
 - Barebones mode

For more documentation visit the [wiki](https://github.com/LukeOnuke/AdminTools/wiki) .

Found a bug? -Report it [here](https://github.com/LukeOnuke/AdminTools/issues/new)

Download : [here (github releases)](https://github.com/LukeOnuke/AdminTools/releases)
Downloadable as:
- **EXE installer** (witch will install the program) - **Recomended way to install**
- JAR file with all dependencies **[Comes in a zip]**

## Setup
First run the program so it can setup files (It will error out and thats normal because it couldnt connect to a server), then folow instructions bellow

Go to the instalation folder and find `AdminTools.properties`
If you installed it using a installer it is in `AppData\Local\AdminTools` folder by default.
Example Directory - `C:\Users\lukak\AppData\Local\Admin Tools\app`

In the file `AdminTools.properties` edit two valiues to your credentials

    rcon.host=<ip of server>`
    rcon.port=<port of rcon - 25575>
    rcon.password=<password of server>

## Galery
Rcon 
![rcon](https://i.imgur.com/v8LLB6f.png)
Status
![status](https://i.imgur.com/fxjDeEZ.png)
Settings
![Settings](https://i.imgur.com/GbcCCAg.png)

## Libraries used
[rkon-core](https://github.com/Kronos666/rkon-core)  - by [Kronos666](https://github.com/Kronos666)

[gson](https://github.com/google/gson) - by [Google](https://github.com/google)

[MinecraftServerPing](https://github.com/jamietech/MinecraftServerPing) - by [jamietech](https://github.com/jamietech)

## Legal info
LukeOnuke is not affiliated with Mojang Synergies AB
"Minecraft" is a trademark of Mojang Synergies AB

Gear Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from www.flaticon.com

This program and its code is licensed on the [GNU GPL V3.0 licence](https://github.com/LukeOnuke/AdminTools/blob/master/LICENSE)
