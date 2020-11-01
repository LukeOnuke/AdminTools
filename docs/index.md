# Admin tools
[![daym](https://img.shields.io/github/downloads/LukeOnuke/AdminTools/total)](https://github.com/LukeOnuke/AdminTools/releases) [![Spiget Downloads](https://img.shields.io/spiget/downloads/81484?label=spigot%20downloads)](https://www.spigotmc.org/resources/admin-tools.81484/) ![wow here](https://img.shields.io/github/license/LukeOnuke/AdminTools) ![wow](https://img.shields.io/github/v/release/LukeOnuke/AdminTools?include_prereleases)

AdminTools is a administration program for minecraft servers.
> Use the **sidebar** to navigate through the docs.

Github : [https://github.com/LukeOnuke/AdminTools](https://github.com/LukeOnuke/AdminTools)

Website : [https://admintools.app](https://admintools.app)

Spigot : [https://www.spigotmc.org/threads/admin-tools.451976/](https://www.spigotmc.org/threads/admin-tools.451976/)


## Legal
This program is licensed under the **GPL-V3 license**
LukeOnuke (the author of this program) and all the contributers aren't asociated with Mojang Synergies AB
Some images contained in the program either contain parts of images copyrighted by Mojang Synergies AB or they are copyrighted by Mojang Synergies AB
For more info read the **legal** section of the `readme.md` file in the github repository

#   Installation
Steps for installing the program

## Basic instalation
 1. Download the zip file from the [Github releases](https://github.com/LukeOnuke/AdminTools/releases/latest)
 2. Place it in the folder of your choice
		 **Protip:** Remember that folder
 3. Extract the contents of the zip file into that folder
 4. Execute the program file (**AdminTools.jar**)

## Advanced instalation
Follow the steps from the basic instalation

**Setting up the program so that in appears in search**
If you are on **windows** : 

 1. Go to `%appdata%\Microsoft\Windows\Start Menu\Programs`
 2. Create a new directory called `AdminTools`
 3. Open the directory
 4. Place a shortcut to the *program file*(**AdminTools.jar**)
 
 
 
## Themes
 Themes were added in `v5.0.0`
 
 Theme file structure
 ```
 ThemeMainFolder
 |
 |--- style.css
 |--- consolecolor.css
 ```
 
## Creating a theme
You can create themes by copying the default theme and pasting it into the `/Assets/Themes/` folder. 
 
After copy pasting the default theme rename it to what you want.
!!!WARNING
```
Themes can not contain spaces in their file name, otherwise the theme wont work.
```
 
Then change the given valiues in the css to easly create a theme. This part :
 
```css
*{
    /*Modify these valiues for creating new themes easly*/
    /*
     *      ___ _____   _____ _                              
     *     / _ \_   _| |_   _| |                             
     *    / /_\ \| |     | | | |__   ___ _ __ ___   ___  ___ 
     *    |  _  || |     | | | '_ \ / _ \ '_ ` _ \ / _ \/ __|
     *    | | | || |     | | | | | |  __/ | | | | |  __/\__ \
     *    \_| |_/\_/     \_/ |_| |_|\___|_| |_| |_|\___||___/
     *
     * Default theme
     *
     */
    -at-darkaccent: #487829;
    -at-accent: #699b2c;
    -at-secondaccent: #d7fc03; 
    -at-lightaccent: #b8cf69;
    -at-background: #424242;
    -at-darkbackground: #303030;
    -at-lightbackground: #525252;
    -at-bordercolor: #5c5c5c;
    -at-text: white;
}
 
```
Change the "Default theme" to The name of your theme.
 
Now you can change the color valiues.
 
If you need to change the color hex valiue in `consolecolor.txt`
 
You can also change the other parts of the css. If you create a theme and publish it, add it to the theme list.
 
## Theme list 
|Theme name| Theme author | Theme github link | Bundled in latest version? |
|--|--|--|--|
| Default | LukeOnuke | [link](https://github.com/LukeOnuke/AdminTools/tree/master/AdminTools/Assets/Themes/Default) | yes |
| Aqua | LukeOnuke | [link](https://github.com/LukeOnuke/AdminTools/tree/master/AdminTools/Assets/Themes/Aqua) | yes |