# LoreCraft — Commands, Permissions, & Features
**Version:** 1.0.0  
**Tagline:** _Craft quests, cutscenes, and NPC tales—without leaving the game._  
**Supports:** Paper/Spigot/Bukkit 1.8.x → 1.21.x (best on modern Paper)  
**Soft Dependency:** PlaceholderAPI (optional)

---

## ✨ Core Feature Set
- **Story Builder GUI** — Fast, in‑game editors for Books, Dialogues, and Cutscenes (no server reload needed for small tweaks).
- **Lore Books** — Create readable story books, codices, journals, and quest handbooks with pages, chapters, and unlock conditions.
- **NPC Dialogues** — Branching conversation trees, choices, variables, flags, and simple conditions (permission, placeholder value, item possession).
- **World Cutscenes** — Play camera paths, timed actions (titles, chat, sounds, particles, commands), hologram cues, and NPC emotes.
- **Holograms + Particles + Sounds** — Lightweight effects with presets; trigger on join, region enter, command, or dialogue step.
- **Placeholders Everywhere** — Rich built‑in placeholders (e.g., `%lc_story_active%`) and **PlaceholderAPI** expansion for dynamic values.
- **Modular Systems** — Toggle systems on/off in `config.yml` (Books, Dialogues, Scenes, Holograms, Effects, Placeholders, GUI).
- **Backward Compatible** — Runs on 1.8.x; modern Paper unlocks better timings, async IO, and MiniMessage formatting.
- **Import/Export** — Export single stories or whole packs to `.zip` for sharing; import safely with ID remap.
- **Permissions‑First** — Granular `lorecraft.*` nodes for admins, builders, and creators.

> _Note:_ Some visual niceties (RGB/MiniMessage, high‑frequency particles, and camera smoothness) improve significantly on 1.16+ and Paper builds.

---

## 🧭 Commands
All commands can be prefixed with `/lorecraft` or `/lc` (alias).

| Command | Description | Permission |
|---|---|---|
| `/lc` | Opens the main LoreCraft GUI (if enabled). | `lorecraft.gui.open` |
| `/lc help [page]` | Show help for commands and subcommands. | `lorecraft.help` |
| `/lc about` | Show plugin info & credits. | `lorecraft.help` |
| `/lc reload` | Reload configs and hot‑reload story data where safe. | `lorecraft.admin.reload` |
| `/lc save` | Force‑save all pending authoring changes. | `lorecraft.admin.save` |

### Books
| Command | Description | Permission |
|---|---|---|
| `/lc book gui` | Open the Books editor GUI. | `lorecraft.book.gui` |
| `/lc book create <id>` | Create a new lore book. | `lorecraft.book.create` |
| `/lc book edit <id>` | Edit book metadata/pages. | `lorecraft.book.edit` |
| `/lc book open <id> [player]` | Give/open a book to self or a player. | `lorecraft.book.open` |
| `/lc book publish <id>` | Mark book as ready/locked. | `lorecraft.book.publish` |
| `/lc book delete <id>` | Delete a book. | `lorecraft.book.delete` |
| `/lc book list [filter]` | List books with optional filter. | `lorecraft.book.list` |

### NPC Dialogues
| Command | Description | Permission |
|---|---|---|
| `/lc npc gui` | Open the Dialogue editor GUI. | `lorecraft.npc.gui` |
| `/lc npc link <npcId> <dialogueId>` | Link a Citizens/Entity NPC to a dialogue. | `lorecraft.npc.link` |
| `/lc npc unlink <npcId>` | Unlink dialogue from NPC. | `lorecraft.npc.unlink` |
| `/lc npc test <dialogueId> [player]` | Start a dialogue for testing. | `lorecraft.npc.test` |
| `/lc npc list [filter]` | List dialogues. | `lorecraft.npc.list` |

### Cutscenes (Scenes)
| Command | Description | Permission |
|---|---|---|
| `/lc scene gui` | Open the Scene editor GUI. | `lorecraft.scene.gui` |
| `/lc scene create <id>` | Create a new scene/cutscene. | `lorecraft.scene.create` |
| `/lc scene play <id> [player|@selector]` | Play a scene for self, a target, or selector. | `lorecraft.scene.play` |
| `/lc scene stop [player|@selector]` | Stop currently playing scene for target(s). | `lorecraft.scene.stop` |
| `/lc scene list [filter]` | List scenes. | `lorecraft.scene.list` |
| `/lc scene record <id>` | Begin/finish recording a camera path. | `lorecraft.scene.record` |

### Holograms & Effects
| Command | Description | Permission |
|---|---|---|
| `/lc holo create <id> [text]` | Create a hologram at your location. | `lorecraft.holo.create` |
| `/lc holo move <id>` | Move hologram to your location. | `lorecraft.holo.move` |
| `/lc holo set <id> <text>` | Edit hologram text (MiniMessage on modern). | `lorecraft.holo.edit` |
| `/lc holo remove <id>` | Delete hologram. | `lorecraft.holo.delete` |
| `/lc particle play <preset> [duration]` | Play a particle preset at your location. | `lorecraft.effect.particle` |
| `/lc sound play <key> [volume] [pitch]` | Play a sound to yourself or radius (if specified). | `lorecraft.effect.sound` |

### Modules & Maintenance
| Command | Description | Permission |
|---|---|---|
| `/lc module list` | Show module states (enabled/disabled). | `lorecraft.module.view` |
| `/lc module <enable|disable> <module>` | Toggle a module at runtime (where safe). | `lorecraft.module.toggle` |
| `/lc placeholder list` | List provided placeholders. | `lorecraft.placeholder.list` |
| `/lc placeholder test <id>` | Resolve a placeholder for you (debug). | `lorecraft.placeholder.test` |
| `/lc export <scope>` | Export a book/dialogue/scene or all content. | `lorecraft.export` |
| `/lc import <pack.zip>` | Import a pack (requires file in /plugins/LoreCraft/imports). | `lorecraft.import` |

> **Selectors:** Where `@selector` appears, Paper/Spigot selectors like `@a[r=20]` are supported on versions that provide them.

---

## 🔐 Permissions
Permissions are designed for three typical roles: **Admin**, **Creator**, and **Player**.

### High‑Level Wildcards
- `lorecraft.*` — Full access _(default: op)_
- `lorecraft.admin.*` — All admin/maintenance _(default: op)_
- `lorecraft.creator.*` — Authoring & testing (no dangerous admin ops) _(default: false)_

### GUI & General
- `lorecraft.gui.open` — Open main GUI _(default: false)_
- `lorecraft.help` — Use `/lc help`, `/lc about` _(default: true)_
- `lorecraft.admin.reload` — `/lc reload` _(default: op)_
- `lorecraft.admin.save` — `/lc save` _(default: op)_

### Books
- `lorecraft.book.*` — All book actions _(default: false)_
    - `lorecraft.book.gui` — Open book editor GUI
    - `lorecraft.book.create` — Create books
    - `lorecraft.book.edit` — Edit pages/metadata
    - `lorecraft.book.publish` — Publish/lock
    - `lorecraft.book.open` — Give/open
    - `lorecraft.book.delete` — Delete
    - `lorecraft.book.list` — List

### NPC Dialogues
- `lorecraft.npc.*` — All dialogue actions _(default: false)_
    - `lorecraft.npc.gui` — Dialogue editor GUI
    - `lorecraft.npc.link` — Link dialogue to NPC
    - `lorecraft.npc.unlink` — Unlink dialogue
    - `lorecraft.npc.test` — Test dialogue
    - `lorecraft.npc.list` — List dialogues

### Scenes (Cutscenes)
- `lorecraft.scene.*` — All scene actions _(default: false)_
    - `lorecraft.scene.gui` — Scene editor GUI
    - `lorecraft.scene.create` — Create scenes
    - `lorecraft.scene.play` — Play scenes
    - `lorecraft.scene.stop` — Stop scenes
    - `lorecraft.scene.record` — Record camera paths
    - `lorecraft.scene.list` — List scenes

### Holograms & Effects
- `lorecraft.holo.*` — All hologram actions _(default: false)_
    - `lorecraft.holo.create` — Create
    - `lorecraft.holo.move` — Move
    - `lorecraft.holo.edit` — Edit text
    - `lorecraft.holo.delete` — Delete
- `lorecraft.effect.*` — All effects _(default: false)_
    - `lorecraft.effect.particle` — Play particle presets
    - `lorecraft.effect.sound` — Play sounds

### Modules & Maintenance
- `lorecraft.module.*` — View & toggle modules _(default: op)_
    - `lorecraft.module.view` — List modules
    - `lorecraft.module.toggle` — Enable/disable modules
- `lorecraft.placeholder.*` — Placeholder tools _(default: false)_
    - `lorecraft.placeholder.list` — List placeholders
    - `lorecraft.placeholder.test` — Test resolution
- `lorecraft.export` — Export packs _(default: op)_
- `lorecraft.import` — Import packs _(default: op)_

---

## 👥 Suggested Role Bundles (example)
- **Admin:** `lorecraft.admin.*`, `lorecraft.module.*`, `lorecraft.export`, `lorecraft.import`
- **Creator/Builder:** `lorecraft.gui.open`, `lorecraft.book.*`, `lorecraft.npc.*`, `lorecraft.scene.*`, `lorecraft.holo.*`, `lorecraft.effect.*`, `lorecraft.placeholder.*`
- **Player/Testers:** `lorecraft.help`, `lorecraft.scene.play` (if allowed), `lorecraft.book.open`

---

## 🧩 Notes on PlaceholderAPI
If PlaceholderAPI is present, LoreCraft registers an expansion (e.g., `lc_*`). Example placeholders:
- `%lc_story_active%` → `true/false`
- `%lc_dialogue_id%` → current dialogue id (during play)
- `%lc_scene_name%` → current scene display name
- `%lc_flag_<name>%` → flag/variable value for the player

Use these in titles, holograms, scoreboard plugins, or other systems that read PlaceholderAPI.

---

## ⚙️ Module Keys (for `config.yml`)
- `modules.books: true|false`
- `modules.dialogues: true|false`
- `modules.scenes: true|false`
- `modules.holograms: true|false`
- `modules.effects: true|false`
- `modules.placeholders: true|false`
- `modules.gui: true|false`

_Disable what you don’t need to keep things lean on older servers._
