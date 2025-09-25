package com.sergeantfuzzy.lorecraft;

import com.sergeantfuzzy.lorecraft.commands.core.CommandRegistry;
import com.sergeantfuzzy.lorecraft.gui.holograms.HologramGui;
import com.sergeantfuzzy.lorecraft.listeners.JoinLeaveListener;
import com.sergeantfuzzy.lorecraft.packs.PacksService;
import com.sergeantfuzzy.lorecraft.service.*;
import com.sergeantfuzzy.lorecraft.util.Mini;
import com.sergeantfuzzy.lorecraft.util.YamlResourceControl;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoreCraft extends JavaPlugin {

    private static LoreCraft INSTANCE;

    // Services
    private DialogueService dialogueService;
    private SceneService sceneService;
    private HologramService hologramService;
    private EffectService effectService;
    private BookService bookService;
    private VarsService varsService;

    private HologramGui hologramGui;
    private PacksService packs;

    // Utilities
    private MiniMessage mm;
    private YamlResourceControl lang;
    private CommandRegistry commands;

    public static LoreCraft instance() {
        return INSTANCE;
    }

    public MiniMessage mm() {
        return mm;
    }

    public YamlResourceControl lang() {
        return lang;
    }

    public CommandRegistry commands() {
        return commands;
    }

    public String prefix() {
        return getConfig().getString(
                "prefix",
                "<gradient:#ffb400:#ff6a00><bold>[LoreCraft]</bold></gradient> "
        );
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        // Config & language
        saveDefaultConfig();
        saveLang("language_en.yml");

        this.mm = Mini.mini(); // your shaded wrapper
        this.lang = new YamlResourceControl(this, "language_en.yml");
        commands = new CommandRegistry(this);

        // --- Initialize services BEFORE using getters (adjust constructors if needed) ---
        this.dialogueService = new DialogueService(this);
        this.sceneService = new SceneService(this);
        this.hologramService = new HologramService(this);
        this.effectService = new EffectService(this);
        this.bookService = new BookService(this);
        this.varsService = new VarsService(); // default/no-args per your note

        this.hologramGui = new HologramGui(this);
        this.packs = new PacksService(this);
        this.packs.reload();

        // /lc root command with sub-dispatch
        PluginCommand pc = getCommand("lc");
        if (pc != null) {
            pc.setExecutor(commands.rootExecutor());
            pc.setTabCompleter(commands.rootCompleter());
        } else {
            getLogger().severe("LoreCraft: command 'lc' not found in plugin.yml");
        }

        // Listeners
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);

        // Pretty ENABLE banner
        consoleBanner(
                "<gold>=================================================</gold>",
                "<gradient:#ffb400:#ff6a00><bold>LoreCraft v" + getDescription().getVersion() + "</bold></gradient>",
                "<yellow>Description:</yellow> <gray><italic>" + getDescription().getDescription() + "</italic></gray>",
                "<yellow>Status:</yellow> <green><bold>ENABLED</bold></green>",
                "<yellow>Authors:</yellow> <white>" + String.join(", ", getDescription().getAuthors()) + "</white>",
                "<yellow>Prefix:</yellow> " + prefix(),
                "<gold>=================================================</gold>"
        );
    }

    @Override
    public void onDisable() {
        consoleBanner(
                "<red>=================================================</red>",
                "<gradient:#ffb400:#ff6a00><bold>LoreCraft</bold></gradient> <red><bold>DISABLED</bold></red>",
                "<red>=================================================</red>"
        );
    }

    private void saveLang(String name) {
        if (getResource(name) != null) saveResource(name, false);
    }

    private void consoleBanner(String... lines) {
        var mm = MiniMessage.miniMessage();
        for (String line : lines) {
            Bukkit.getConsoleSender().sendMessage(mm.deserialize(line));
        }
    }

    // Getters for other classes
    public DialogueService dialogueService() {
        return dialogueService;
    }

    public SceneService sceneService() {
        return sceneService;
    }

    public HologramGui hologramGui() {        // ⬅ add this
        return hologramGui;
    }

    public HologramService hologramService() {
        return hologramService;
    }

    public EffectService effectService() {
        return effectService;
    }

    public BookService bookService() {
        return bookService;
    }

    public VarsService varsService() {
        return varsService;
    }

    public PacksService packs() {  // <-- must be public
        return packs;
    }
}