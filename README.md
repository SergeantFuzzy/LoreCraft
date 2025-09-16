# LoreCraft

**Version:** 1.0.0  
**Tagline:** Craft quests, cutscenes, and NPC tales—without leaving the game.  
**Supports:** Paper/Spigot/Bukkit 1.8.x → 1.21.x (best on modern Paper)  
**Soft Dependencies:** PlaceholderAPI (optional)

---

## ✨ Features
- Story Builder GUI — Create & edit stories without restarts.
- Lore Books — Custom readable in-game books.
- NPC Dialogues — Branching conversations with conditions.
- World Cutscenes — Camera paths, timed effects, titles, sounds.
- Holograms + Particles + Sounds — Immersive storytelling effects.
- PlaceholderAPI support for `%lc_*%` placeholders.
- Modular system toggles in `config.yml`.
- Import/Export packs for easy sharing.

---

## 📦 Installation
1. Download the latest `LoreCraft-x.y.z.jar` from [Releases](../../releases).
2. Place it into your server’s `/plugins/` folder.
3. Restart your server.
4. Configure via `/lc gui` or `config.yml`.

---

## 🧭 Commands & Permissions
See [Commands, Permissions, and Features](docs/LoreCraft_COMMANDS_PERMS_FEATURES.md).

---

## 🛠️ Development
This project uses **Maven**.

```bash
git clone https://github.com/<your-username>/LoreCraft.git
cd LoreCraft
mvn clean package
