package manchiro.manchirorin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;
import static manchiro.manchirorin.result.checkChildrenBattle;
import static manchiro.manchirorin.result.checkGamePush1;

public class MCHData {
    static Manchirorin plugin;
    public static void loadEnable(Manchirorin plugin){
        MCHData.plugin = plugin;
    }

    public static boolean gameStart(Player p, double bet, int hito) {
        if (plugin.oya != null) {
            return false;
        }
        plugin.hito = hito;
        plugin.bet = bet;
        plugin.oya = p;
        plugin.oyabal = bet * 5 * hito;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(plugin.prefix + " §a§l" + plugin.oya.getDisplayName() + "§f§lさんにより§d§l" + plugin.hito + "§f§l人募集の§e§l" + plugin.bet + "円§f§lマンチロが開始されました！§a§l: /mch" + "§e参加する(必要: " + plugin.bet * 5 + ")" + "/mch join");
        }
        plugin.mch = false;
        plugin.timer.betTime();
        plugin.game = true;
        return true;
    }

    public static void reset(){
        for(Player p:plugin.kolist){
            plugin.vault.deposit(p.getPlayer(), plugin.bet*5);
            //plugin.totalBet.transferBalanceToPlayer(p,plugin.bet*5,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr cancel: "+Bukkit.getPlayer(uuid).getName());
        }
        plugin.kolist.clear();
        plugin.bet = -1;
        plugin.oya = null;
        plugin.oyayaku = -1;
        plugin.oyabal = -1;
        plugin.hito = -1;
        plugin.game = false;
        plugin.mch = false;
        Bukkit.broadcastMessage(plugin.prefix+" §a§lマンチロが終了しました。");
    }

    public static void timeEnd(){
        plugin.vault.deposit(plugin.oya ,plugin.oyabal);
        Bukkit.broadcastMessage(plugin.prefix+" §4§l子が集まらなかったため中止しました。");
        reset();
    }

    public static void cancel(){
        plugin.vault.deposit(plugin.oya ,plugin.oyabal);
        Bukkit.broadcastMessage(plugin.prefix+" §4§lキャンセルされました。");
        reset();
    }

    public static void gamePush1(){
        plugin.mch = true;
        Bukkit.broadcastMessage(plugin.prefix+" §a§lマンチロがスタートしました！");
        sendKankeisya(" §a§l"+plugin.oya.getDisplayName()+"§f§lさん(親)がサイコロを振っています…§e§l§kaaa");
        new BukkitRunnable(){
            @Override
            public void run() {
                cancel();
                if(!plugin.mch){
                    return;
                }
                Random rnd1 = new Random();
                Random rnd2 = new Random();
                Random rnd3 = new Random();
                int dice1 = rnd1.nextInt(6)+1;
                int dice2 = rnd2.nextInt(6)+1;
                int dice3 = rnd3.nextInt(6)+1;
                sendKankeisya(" §a§lﾊﾟｶｯ！  §f§l "+dice1+"・"+dice2+"・"+dice3+" ！！");
                String result = hantei.mainhantei(dice1,dice2,dice3);
                checkGamePush1(result);
            }
        }.runTaskTimer(plugin,100,20);
    }

    public static boolean jackskip = false;

    public static void childturn(){
        sendKankeisya(" §c§l子のターンが開始されました！");
        new BukkitRunnable(){
            @Override
            public void run() {
                if(plugin.kolist.size()==0){
                    cancel();
                    if(!plugin.game){
                        return;
                    }
                    plugin.vault.deposit(plugin.oya, plugin.oyabal - (plugin.oyabal/100));
                    //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal - (plugin.parentbal/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr parent deposit: "+Bukkit.getPlayer(plugin.parent).getName());
                    Bukkit.broadcastMessage(plugin.prefix+ "§a§l"+plugin.oya.getDisplayName()+"§f§l: §e§l"+plugin.bet*5*plugin.hito+" 円 → "+plugin.oyabal+" 円§e(うち手数料"+plugin.oyabal/100+" 円)");
                    plugin.addJackpot(plugin.oyabal/100);
                    reset();
                    return;
                }
                if(!jackskip) {
                    Player p = plugin.kolist.get(0);
                    childbattle(p);
                }
            }
        }.runTaskTimer(plugin,20,120);
    }

    public static void childbattle(Player p){
        sendKankeisya(" §c§l"+p.getName()+"§f§lさん(子)がサイコロを振っています…§e§l§kaaa");
        new BukkitRunnable(){
            @Override
            public void run() {
                cancel();
                if (!plugin.game) {
                    return;
                }
                Random rnd1 = new Random();
                Random rnd2 = new Random();
                Random rnd3 = new Random();
                int dice1 = rnd1.nextInt(6) + 1;
                int dice2 = rnd2.nextInt(6) + 1;
                int dice3 = rnd3.nextInt(6) + 1;
                sendKankeisya(" §a§lﾊﾟｶｯ！  §f§l " + dice1 + "・" + dice2 + "・" + dice3 + " ！！");
                String result = hantei.mainhantei(dice1, dice2, dice3);
                checkChildrenBattle(result, p);
            }
        }.runTaskTimer(plugin,100,20);
    }

    public static void oyaWin(double bairitu){
        Bukkit.broadcastMessage(plugin.prefix+" §e§l結果: §a§l親の勝利！！");
        double with = plugin.bet * bairitu * plugin.hito;
        plugin.oyabal = with + plugin.oyabal;
        double retn = (plugin.bet * 5) - (plugin.bet * bairitu) - (plugin.bet/100);
        for(Player p:plugin.kolist){
            plugin.vault.deposit(p.getPlayer(), retn);
            //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,retn,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr lose return: "+Bukkit.getPlayer(uuid).getName());
            Bukkit.broadcastMessage(plugin.prefix+" §c§l"+p.getName()+"§f§l: §e§l"+plugin.bet*5+" 円 → "+ (retn + (plugin.bet/100)) +" 円§e(うち手数料"+plugin.bet/100+" 円)");
            plugin.addJackpot(plugin.bet/100);
        }
        plugin.kolist.clear();
        plugin.vault.deposit(plugin.oya, plugin.oyabal - plugin.oyabal/100);
        //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal - (plugin.parentbal/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr parent deposit: "+Bukkit.getPlayer(plugin.parent).getName());
        Bukkit.broadcastMessage(plugin.prefix+" §a§l"+plugin.oya.getDisplayName()+"§f§l: §e§l"+plugin.bet*5*plugin.hito+" 円 → "+plugin.oyabal+" 円§e(うち手数料"+plugin.oyabal/100+" 円)");
        plugin.addJackpot(plugin.oyabal/100);
        reset();
    }
    public static void oyaLose(double bairitu){
        Bukkit.broadcastMessage(plugin.prefix+" §e§l結果: §c§l子の勝利！！");
        double with = (plugin.bet * bairitu);
        plugin.oyabal = plugin.oyabal - (with*plugin.hito);
        for(Player p:plugin.kolist){
            Bukkit.broadcastMessage(plugin.prefix+" §c§l"+p.getName()+"§f§l: §e§l"+plugin.bet*5+ " 円 → "+ (with + (plugin.bet*5)) +" 円§e(うち手数料"+ plugin.bet/100+" 円)");
            plugin.addJackpot(plugin.bet/100);
            plugin.vault.deposit(p.getPlayer(), with + (plugin.bet * 5) - (plugin.bet/100));
            //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,with + (plugin.onebet * 5) - (plugin.onebet/100) ,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr win: "+Bukkit.getPlayer(uuid).getName());
        }
        plugin.kolist.clear();
        plugin.vault.deposit(plugin.oya, plugin.oyabal - (plugin.oyabal/100));
        //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal - (plugin.parentbal/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr parent deposit: "+Bukkit.getPlayer(plugin.parent).getName());
        Bukkit.broadcastMessage(plugin.prefix+" §a§l"+ plugin.oya.getDisplayName() +"§f§l: §e§l"+ plugin.bet*5*plugin.hito+" 円 → "+plugin.oyabal+" 円§e(うち手数料"+plugin.oyabal/100+" 円)");
        plugin.addJackpot(plugin.oyabal/100);
        reset();
    }

    public static void vsOya(boolean Oyawin,Player p,double bairitu){
        if(Oyawin){
            Bukkit.broadcastMessage(plugin.prefix+" §e§l結果: §a§l親の勝利！！");
            double with = plugin.bet * bairitu;
            plugin.oyabal = plugin.oyabal + with;
            double retn = (plugin.bet * 5) - (plugin.bet * bairitu);
            Bukkit.broadcastMessage(plugin.prefix+" §c§l"+p.getDisplayName()+"§f§l: §e§l"+ plugin.bet * 5 +" 円 → "+ retn +" 円§e(うち手数料"+ plugin.bet/100 +" 円)");
            plugin.vault.deposit(p.getPlayer(), retn - (plugin.bet/100));
            //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,retn - (plugin.onebet/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr lose return: "+Bukkit.getPlayer(uuid).getName());
            plugin.addJackpot(plugin.bet/100);
        }else{
            Bukkit.broadcastMessage(plugin.prefix+" §e§l結果: §c§l子の勝利！！");
            double with = plugin.bet * bairitu;
            plugin.vault.deposit(p.getPlayer(), with + (plugin.bet * 5) - (plugin.bet/100));
            //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,with + (plugin.onebet * 5) - (plugin.onebet/100),TransactionCategory.GAMBLE,TransactionType.WIN,"mcr win: "+Bukkit.getPlayer(uuid).getName());
            Bukkit.broadcastMessage(plugin.prefix+" §c§l"+ p.getDisplayName() +"§f§l: §e§l"+ plugin.bet * 5+" 円 → "+ (with + (plugin.bet * 5))+" 円§e(うち手数料"+ plugin.bet/100 + " 円)");
            plugin.addJackpot(plugin.bet/100);
            plugin.oyabal = plugin.oyabal - (with);
        }
        plugin.kolist.remove(p);
    }

    public static void draw(Player p){
        Bukkit.broadcastMessage(plugin.prefix+" §e§l結果: §a§l引き分け！！");
        Bukkit.broadcastMessage(plugin.prefix+" §c§l"+p.getDisplayName()+"§f§l: §e§l"+ plugin.bet * 5 +" 円 → "+ plugin.bet * 5+" 円");
        plugin.vault.deposit(p.getPlayer(), plugin.bet * 5);
        //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,plugin.onebet*5,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr draw: "+Bukkit.getPlayer(uuid).getName());
        plugin.kolist.remove(p);
    }

    public static void sendTitle(String main,String sub,int time){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN,1,1);
            player.sendTitle(main,sub,10, time,10);
        }
    }

    public static void sendKankeisya(String message){
        for(Player p:plugin.kolist){
            p.sendMessage(plugin.prefix+message);
        }
        plugin.oya.sendMessage(plugin.prefix+message);
    }

    public static void gotoJackpot(){
        Bukkit.broadcastMessage(plugin.prefix+" §e§l結果: §4§l全 員 敗 北");
        double with = plugin.bet * 5;
        for(Player p:plugin.kolist){
            Bukkit.broadcastMessage(plugin.prefix+" §c§l"+p.getDisplayName()+"§f§l: §e§l"+plugin.bet*5+"円 → "+"§e(全額手数料" + with + "円)");
            plugin.addJackpot(with);
        }
        plugin.kolist.clear();
        Bukkit.broadcastMessage(plugin.prefix+" §a§l"+plugin.oya.getDisplayName()+"§f§l: §e§l"+plugin.bet*5*plugin.hito+"円 → "+"§e(全額手数料"+ plugin.oyabal +"円)");
        plugin.addJackpot(plugin.oyabal);
        plugin.oyabal = 0;
        reset();
    }
}
