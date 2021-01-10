package manchiro.manchirorin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Manchirorin extends JavaPlugin {

    private List<Player> list;
    private VaultManager vault;
    Random r1 = new Random();
    Random r2 = new Random();
    Random r3 = new Random();
    String prefix = "§f[§d§lマ§a§lン§f§lチロ§r]";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("起動しました。");
        list = new ArrayList<>();
        vault = new VaultManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mcr")){
            if (args.length == 0) {
                sender.sendMessage("§f==========" + prefix + "§f==========");
                sender.sendMessage("§a§l/mcr start §e§l[金額] §r: §f§l親としてマンチロを開始します");
                sender.sendMessage("§a§l/mcr join §r: §f§l子として開催中のマンチロに参加します");
                sender.sendMessage("§a§l/mcr rule §r: §f§lマンチロんもルールを表示します");
                return true;
            }
            if (args[0].equals("rule")) {
                sender.sendMessage("§f==========" + prefix + "§f==========");
                sender.sendMessage("§6役一覧: [1:1:1 jackpotチャンス] [それ以外の三つ揃い ゾロメ] [出目合計10 man10] [1・2・3 ワンツースリ] [出目合計5 dan5]");
                sender.sendMessage("§6役一覧: [二つそろって残りが・・ その数字が強さ]");
                sender.sendMessage("");
                sender.sendMessage("§e配当率: 『ワンツースリー:4倍勝(親のみ)』『ゾロメ:3倍勝』『man10:2倍勝』『dan5:2倍負』 通常:1倍負/勝");
                sender.sendMessage("§ejackpotの払い出し金額: 賭け金x10 or jackpotすべて のどちらか金額が低いほう");
            }
        }
     return true;
    }
}
