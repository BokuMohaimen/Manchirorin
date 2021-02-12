package manchiro.manchirorin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.runtime.JSType.isNumber;

public final class Manchirorin extends JavaPlugin {

    boolean mch = false;
    private List<Player> list;
    private VaultManager vault;
    String prefix = "§f[§d§lマ§a§lン§f§lチロ§r]";
    int jp;

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
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("mch")) {
            if (args.length == 0) {
                p.sendMessage("§f==========" + prefix + "§f==========");
                p.sendMessage("§a§l/mch new §e§l[金額] [人数]§r: §f§l親としてマンチロを開始します");
                p.sendMessage("§a§l/mch join §r: §f§l子として開催中のマンチロに参加します");
                p.sendMessage("§a§l/mch rule §r: §f§lマンチロのルールを表示します");
                p.sendMessage("§d§lCreated by Mohaimen_Ksr");
                return true;
            }
            if (args[0].equals("rule")) {
                p.sendMessage("§f==========" + prefix + "§f==========");
                p.sendMessage("§6役一覧: [1:1:1 jackpotチャンス] [それ以外の三つ揃い ゾロメ]");
                p.sendMessage("§6[出目合計10 man10] [1・2・3 イチ・ニ・サン・ﾀﾞｰ!!] [出目合計5 dan5]");
                p.sendMessage("§6役一覧: [二つそろって残りが・・ その数字が強さ]");
                p.sendMessage("");
                p.sendMessage("§e配当率: 『イチ・ニ・サン・ﾀﾞｰ!!:4倍勝(親のみ)』『ゾロメ:3倍勝』");
                p.sendMessage("§e『man10:2倍勝』『dan5:2倍負』 通常:1倍負/勝");
                p.sendMessage("§ejackpotの払い出し金額: 賭け金x10 or jackpotすべて のどちらか金額が低いほう");
                return true;
            }
            //new マンチロのゲームを開始↓
            if (args[0].equals("new")) {
                int bet = Integer.parseInt(args[1]);
                int hito = Integer.parseInt(args[2]);
                if (args.length != 3) {
                    p.sendMessage(prefix + " 引数の数が違っています");
                    return true;
                }
                if (mch) {
                    p.sendMessage(prefix + " 現在マンチロが開始されています");
                    return true;
                }
                if (!isNumber(args[1])) {
                    p.sendMessage(prefix + " §c金額を数字で入力してください");
                    return true;
                }
                if (!isNumber(args[2])) {
                    p.sendMessage(prefix + "§c人数を数字で入力してください");
                    return true;
                }
                if (vault.getBalance(p.getUniqueId()) < bet * 5) {
                    p.sendMessage(prefix + " §c必要金額に持っていません" + "§r" + bet * 5 + "円");
                    return true;
                }
                if (hito <= 0 || hito >= 11) {
                    p.sendMessage(prefix + " 募集人数は1人以上10人未満で入力してください");
                    return true;
                }
                Bukkit.broadcastMessage(prefix + " " + sender.getName() + "が§e§l" + bet + "円§lの§d§lマ§a§lン§f§lチロ§r§lを募集しました！");
                Bukkit.broadcastMessage(prefix + " §l募集人数:§e§l " + hito + "人");
                return true;
                //join マンチロのゲームに参加↓
            } if (args[0].equals("join")) {
                    if (!mch) {
                        p.sendMessage(prefix + " 現在マンチロは開催されていません");
                        return true;
                    }
                    return true;
            } if (args[0].equals("off")) {
                if (mch) {
                    p.sendMessage(prefix + "オフにしました");
                    mch = false;
                    return true;
                } else {
                    p.sendMessage(prefix + "既にオフになっています");
                    return true;
                }
            } else {
                    p.sendMessage(prefix + " 使い方が間違っています");
                    p.sendMessage(prefix + " /mch と入力するとコマンド一覧が見れます");
                    return true;
            }
        } return true;
    }
}
