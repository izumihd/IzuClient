<div align="center">

# IzuClient
An open-source client base for modern Minecraft.

<img src="images/ui.png" width="90%" />

# Why
![](https://i.imgur.com/VYjIphG.png)

# Features
- Configurable zoom
- Draggable FPS, ping, TPS, inventory, player-model, and active-module HUD widgets
- Optional player ESP and nearby ore highlighting
- Toggleable modules managed through the click GUI

# Building
Install a 64-bit JDK 21 and make sure `JAVA_HOME` points to it. From PowerShell,
run:

```powershell
.\gradlew.bat build
```

The Fabric mod JAR will be in `build\libs` with a name like
`izuclient-<git-hash>-1.0.0.jar`. Use that JAR with a Fabric installation; it
is not a standalone launcher.

</div>
