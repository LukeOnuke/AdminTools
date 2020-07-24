# Admin Tools
Program with administration tools for Minecraft servers.

Features:
 - Dark mode
 - Rcon client
 - Server status
 - Minecraft services status (eg. authserver, api...)
 - Barebones mode

For more documentation visit the [wiki](https://github.com/LukeOnuke/AdminTools/wiki).

Found a bug? Report it [here](https://github.com/LukeOnuke/AdminTools/issues/new)

Download: [here (releases)](https://github.com/LukeOnuke/AdminTools/releases)
Downloadable as:
- **EXE installer** - **recommended way to install**
- JAR file with all dependencies **[.zip]**

## Setup
 - Make sure your server have RCON enabled and running. 
 - Run the installer and follow the installation steps
 - Go to the instalation folder* and find a file called `AdminTools.properties`.

*if installed by .exe, the directory is in Appdata\Local - `C:\Users\Users\AppData\Local\Admin Tools\app`
*if installed by .jar, the file is in the folder with the .jar

In the file `AdminTools.properties` edit two values to your credentials

    rcon.host=<ip of your server that hosts rcon>`
    rcon.port=<port of the rcon - 25575>
    rcon.password=<rcon password>

## Gallery
RCON
![rcon](https://i.imgur.com/v8LLB6f.png)
Server and Mojang servers status
![status](https://i.imgur.com/fxjDeEZ.png)
Settings
![Settings](https://i.imgur.com/GbcCCAg.png)

## Libraries used
[rkon-core](https://github.com/Kronos666/rkon-core)  - by [Kronos666](https://github.com/Kronos666)

[gson](https://github.com/google/gson) - by [Google](https://github.com/google)

[MinecraftServerPing](https://github.com/jamietech/MinecraftServerPing) - by [jamietech](https://github.com/jamietech)

## Legal info
LukeOnuke is not affiliated with Mojang Synergies AB.
"Minecraft" is a trademark of Mojang Synergies AB.
Gear Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from www.flaticon.com
This program and its code is licensed on the [GNU GPL V3.0 licence](https://github.com/LukeOnuke/AdminTools/blob/master/LICENSE)
