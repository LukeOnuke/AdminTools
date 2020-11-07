# Themes
 Themes were added in `v5.0.0`
 
 Theme file structure
 ```
 ThemeMainFolder
 |
 |--- style.css
 |--- consolecolor.txt
 ```
 
## Creating a theme
You can create themes by copying the default theme and pasting it into the `/Assets/Themes/` folder. 
 
After copy pasting the default theme rename it to what you want.
!!! WARNING Themes can not contain spaces in their file name, otherwise the theme wont work.
 
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