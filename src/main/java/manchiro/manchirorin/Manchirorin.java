package manchiro.manchirorin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jdk.nashorn.internal.runtime.JSType.isNumber;

public final class Manchirorin extends JavaPlugin {

    boolean mch = false;
    boolean game = false;
    List<Player> kolist;
    VaultManager vault;
    String prefix = "§f[§d§lマ§a§lン§f§lチロ§r]";
    double bet;
    int hito;
    double oyabal;
    Long totalbet;
    double jackpot;
    int oyayaku;
    Player oya;
    Timer timer;

    FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("起動しました");
        kolist = new ArrayList<>();
        vault = new VaultManager(this);
        timer = new Timer(this);
        saveDefaultConfig();
        config = getConfig();
        jackpot = config.getDouble("jackpot");
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
                if(oya == null){
                    p.sendMessage("§4§l現在マンチロは行われていません");
                }else {
                    p.sendMessage("§e§lマンチロが行われています！！ §a§l親: " + oya.getDisplayName());
                    p.sendMessage("§eベット金額: " + bet + " 円" + " 必要金額: " + bet * 5 + " 円");
                    p.sendMessage("§a§l募集人数: §e" + hito + "人 §2§l参加人数: §e" + kolist.size() + "人 §e§l合計賭け金: " + totalbet + " 円");
                }
                p.sendMessage("§a§l/mch new §e§l[金額] [人数]§r: §f§l親としてマンチロを開始します");
                p.sendMessage("§a§l/mch join §r: §f§l子として開催中のマンチロに参加します");
                p.sendMessage("§a§l/mch rule §r: §f§lマンチロのルールを表示します");
                p.sendMessage("§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§f§l: §e§l"+ getJackpot() +"円");
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
                if (args.length != 3) {
                    p.sendMessage(prefix + " 引数の数が違っています");
                    return true;
                }
                if (game) {
                    p.sendMessage(prefix + " 現在マンチロが開始されています");
                    return true;
                }
                try {
                    bet = Double.parseDouble(args[1]);
                    hito = Integer.parseInt(args[2]);
                }catch (NumberFormatException e) {
                    p.sendMessage(prefix + " §c金額と人数は数字で入力してください");
                    return true;
                }
                if (vault.getBalance(p.getUniqueId()) < bet * hito * 5) {
                    if (hito <= 0 || hito >= 11) {
                        p.sendMessage(prefix + " 募集人数は1人以上10人以下で入力してください");
                        return true;
                    }
                    p.sendMessage(prefix + " §c必要金額を持っていません §r必要金額: " + "§r" + bet * 5 * hito + "円");
                    return true;
                }
                if (hito <= 0 || hito >= 11) {
                    p.sendMessage(prefix + " §c募集人数は1人以上10人以下で入力してください");
                    return true;
                }
                MCHData.gameStart(p,bet,hito);
                return true;
                //join マンチロのゲームに参加↓
            } if (args[0].equals("join")) {
                    if (!game) {
                        p.sendMessage(prefix + " 現在マンチロは開催されていません");
                        return true;
                    }
                    if (kolist.contains(p)) {
                        p.sendMessage(prefix + " §cあなたは既に参加しています");
                        return true;
                    }
                    if (vault.getBalance(p.getUniqueId()) < bet * 5 ) {
                        p.sendMessage(prefix + " §c所持金が足りません §r必要金額: " + bet * 5 + "円");
                        return true;
                    }
                    kolist.add(p.getPlayer());
                    Bukkit.broadcastMessage(p.getDisplayName() + "さんがマンチロに参加しました！");
                    if (kolist.size() == hito) {
                        MCHData.gamePush1();
                    }
                    return true;
            } if (args[0].equals("off")) {
                if (!p.hasPermission("manchiro.op")) {
                    p.sendMessage(prefix + " §cあなたには権限がありません");
                    return true;
                }
                if (mch) {
                    p.sendMessage(prefix + " オフにしました");
                    mch = false;
                    return true;
                } else {
                    p.sendMessage(prefix + " 既にオフになっています");
                    return true;
                }
            } else {
                    p.sendMessage(prefix + " 使い方が間違っています");
                    p.sendMessage(prefix + " /mch と入力するとコマンド一覧が見れます");
                    return true;
            }
        } return true;
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(oya == p){
            Bukkit.broadcastMessage(prefix+"§4§l親("+p.getDisplayName()+"§4§l)がサーバーから退出したためキャンセルします");
            MCHData.cancel();
        }else if(kolist.contains(p)){
            Bukkit.broadcastMessage(prefix+"§4§l子("+p.getDisplayName()+"§4§l)がサーバーから退出したためキャンセルします");
            MCHData.cancel();
        }
    }
    public double getJackpot(){
        return jackpot;
    }

    public void addJackpot(Double d){
        config.set("jackpot",jackpot+d);
        saveConfig();
        jackpot = jackpot + d;
    }

    public void takeJackpot(Double d){
        config.set("jackpot",jackpot-d);
        saveConfig();
        jackpot = jackpot - d;
    }
}