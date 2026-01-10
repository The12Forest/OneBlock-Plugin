# OneBlock - The Ultimate One-Block Experience!

Tired of complex, laggy One-Block plugins? OneBlock is a **lightweight, simple, and powerful** plugin that brings the popular One-Block challenge to your server. It's designed to be easy to use, highly configurable, and compatible with [Multiverse-Core](https://dev.bukkit.org/projects/multiverse-core), allowing you to create a unique One-Block world for your players.

## Why Choose OneBlock?

-   **Effortless Setup:** Get started in minutes! Simply designate a block as a OneBlock and you're ready to go.
-   **Customizable Progression:** Tailor the One-Block experience to your liking. The `config.yml` file allows you to define the blocks that spawn at each level, creating a unique and engaging challenge for your players.
-   **Seamless Gameplay:** OneBlock is designed to be as unobtrusive as possible. It works in the background, tracking players' progress and leveling them up as they break the OneBlock.
-   **Admin-Friendly:** Manage your OneBlocks with ease using a set of simple, intuitive commands.
-   **Built for Performance:** OneBlock is optimized for performance, ensuring a smooth and lag-free experience for your players.

## Features

-   **Create Unlimited OneBlocks:** Turn any block into a OneBlock.
-   **Dynamic Leveling System:** The OneBlock evolves as you break it, spawning new and more valuable blocks.
-   **Persistent Progress:** All data is saved in a database, so you'll never lose your progress.
-   **Explosion-Proof:** Your OneBlocks are safe from griefers and creepers!
-   **Multi-World Support:** Works flawlessly with Multiverse-Core.

## Commands

All commands require the `OneBlock.Admin` permission.

-   `/oneblock set <x> <y> <z>`: Creates a new OneBlock at the specified coordinates.
-   `/oneblock delete <x> <y> <z>`: Removes a OneBlock.
-   `/oneblock deletebyid <ID>`: Deletes a OneBlock by its unique ID.
-   `/oneblock deleteAllOnebLocks`: Wipes all OneBlocks from the server.
-   `/oneblock list`: Displays a list of all active OneBlocks.

## Configuration

Creating your own custom One-Block progression is easy. Simply edit the `config.yml` file to define the blocks and level sizes for each stage.

```yml
Max_Level: 2
0:
  level-size: 10
  blocks:
    - "DIRT"
    - "STONE"
1:
  level-size: 20
  blocks:
    - "COBBLESTONE"
    - "COAL_ORE"
2:
  level-size: 30
  blocks:
    - "IRON_ORE"
    - "GOLD_ORE"
```

## Installation

1.  Drop the `OneBlock.jar` file into your server's `plugins` folder.
2.  Restart your server.
3.  Enjoy the ultimate One-Block experience!

This plugin was lovingly crafted by The12Forest.
