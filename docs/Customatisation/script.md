# Scripting
AdminTools uses Admin Tools Script for its scripting.
*Added in version 6.0.0`

Call them with `@fileNameWithoutTheExtention`

**Example :** 
File named wa.atsf , you call it with just @wa [args]

Situated in the `/Assets/scripts/` folder

File ends with `.atsf` and **can not contain** any dots or mc formatting codes.

Example of valid names :
 - `serverShutdown.atsf`
 - `script1.atsf`
 - `goodScript.atsf`

Examples of invalid names:

 - `yeet.me.atsf`
 - `§cinvalid§9name§f.atsf`


It is recomended by the atsf file convention to name your scripts with big letters to seperate words.

## ATSF file convention

The convention specifying naming and contents of `atsf` files

### Naming
File ends with `.atsf` and **can not contain** any dots , mc formatting codes or spaces

`.atsf` stands for Admin Tools Script File.

Example of valid names :
 - `serverShutdown.atsf`
 - `script1.atsf`
 - `goodScript.atsf`

Examples of invalid names:

 - `yeet.me.atsf`
 - `§cinvalid§9name§f.atsf`

Words are separated by big letters (cammel case).

Example : `validName.atsf`
### Function and commands
Commands are seperated by newline characters.

Comments start with `#`
```atsf
# This is a comment
#This is too a comment, but looks less nice
```

You cant call a script (function) from another function.

### Arguments
Arguments get parsed from the arguments you give in the call to the script.

So `@example [arg0] [arg1] [arg2] [arg3] [arg4] [arg5]` and so on 

In the file you can mention the arguments with a `argN`  constant.

**Example : **
```atsf
# Example of arguments
!print arg0 arg3
```

If a argument is not provided it will just stay in the argN form.

# Commands
All the commands used in the console can be used here.

Although some commands were added for the sole purpuse of being used in scripts.

## IF (LOGIC COMMAND)
Logic commands are commands used for logic.

The usual if statement.

**Syntax** :
```
!if [logic] [command]
```

**Examples :**
```atsf
!if arg0==Server_Shutdown kick @a Server maintenence

!if arg2!=add kick arg1
```

**Logic :**

Consists of two operators **==** *(equal)* and **!=** *(is not equal)*.

**⚠⚠⚠ The check cant have spaces**, `arg1!=yeet me` **wont work!**

## PRINT
Prints out what it has been given. Supports color.

Syntax : 
```atsf
!print [text]
```

Example ; 
```atsf
!print Batch job done
```
Since print supports color, you can print colored text to the console. Using bukkit highlight.
[Usefull tool for creating colored messages :  https://www.thomasvdbulk.nl/minecraft/](https://www.thomasvdbulk.nl/minecraft/)
```atsf
!print §aAdded §barg0 §ato the whitelist
```

# Example
```atsf
# Kick all the players with a specified message

# Kick them with default message 
!if arg0==e kick @a Server maintenence

# Kick them with more elaborate message
!if arg0!=e kick @a arg0
```