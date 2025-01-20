# More Mobs

This mod adds more mobs to the game. It is a work in progress.

# Getting Started

Clone the repo. Configure the Makefile to point to your necesse install using the wsl mount path

Run `cp .env.dist .env` and update the path to your necesse install

Run `make init` to get your mods folder created.

Go to steam and update your launch arguments to be '-dev -mod mods'

Run `make build` to build the mod and copy to your mods folder

Run `make client` to start the game. This is handy because we can control the jvm arguments.

# Debugging and "hot reload"

You can update a methods body and run `make hot-reload` to update the class without restarting the game.

This requires you to launch the game with `make debug-client`. Optionally you can open a debugger shell with `make attach-debug`.

# Decompiling

Run `make decompiler` to install `jd-gui` in your Necesse game path and then run it against the game jar

To export these as raw files you can follow:
1. Help -> Preferences
2. disable "Write original line numbers" and "write metadata"
3. File -> Save All Sources and pick a directory on your Windows machine. It bugs out with the WSL networked paths
4. Make a new directory in your mod folder called `decompiled`
5. Copy the zip `cp /mnt/c/foo/wherever/you/saved/it.zip decompiled` 
6. Unzip it `unzip Necesse.jar.src.zip`

Now you can surf the code in your favorite editor.

# Docs

Check out the [modding wiki page](https://necessewiki.com/Modding) for more.
