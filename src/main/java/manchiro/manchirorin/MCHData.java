package manchiro.manchirorin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public class MCHData {
    static Manchirorin plugin;
    public static void loadEnable(Manchirorin plugin){
        MCHData.plugin = plugin;
    }

    public static boolean gameStart(Player p, double bet, int hito){
        if(plugin.oya != null){
            return false;
        }
        plugin.hito = hito;
        plugin.bet = bet;
        plugin.oya = p;
        plugin.oyabal = bet * 5 * hito;
        for(Player player:Bukkit.getOnlinePlayers()){
            Bukkit.broadcastMessage(plugin.prefix+"§a§l"+player.getDisplayName()+"§f§lさんにより§d§l"+plugin.hito +"§f§l人募集の§e§l"+plugin.bet+"円§f§lマンチロが開始されました！§a§l: /mcr"+"§e参加する(必要: "+ plugin.bet * 5+")" + "/mcr join");
        }
        plugin.timer.betTime();
        return true;
    }

    public static void reset(){
        for(Player p:plugin.kolist){
            plugin.totalBet.transferBalanceToPlayer(p,plugin.bet*5,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr cancel: "+Bukkit.getPlayer(uuid).getName());
        }
        plugin.kolist.clear();
        plugin.bet = -1;
        plugin.oya = null;
        plugin.oyayaku = -1;
        plugin.oyabal = -1;
        plugin.hito = -1;
        plugin.game = false;
        plugin.vault.giveCountyMoney(plugin.totalBet.getBalance(),TransactionType.FEE,"add jackpot bal");
        plugin.vault.takeMoneyPoolMoney(plugin.totalBets,plugin.totalBet.getBalance(),TransactionType.FEE,"take jackpot bal");
        Bukkit.broadcastMessage(plugin.prefix+"§a§lマンチロが終了しました。");
    }

    public static void timeEnd(){
        Bukkit.broadcastMessage(plugin.prefix+"§4§l子が集まらなかったため中止しました。");
        reset();
    }

    public static void cancel(){
        Bukkit.broadcastMessage(plugin.prefix+"§4§lキャンセルされました。");
        reset();
    }

    public static void gamePush1(){
        plugin.gametime = true;
        Bukkit.broadcastMessage(plugin.prefix+"§a§lマンチロがスタートしました！");
        sendKankeisya("§a§l"+Bukkit.getPlayer(plugin.parent).getDisplayName()+"§f§lさん(親)がサイコロを振っています…§e§l§kaaa");
        new BukkitRunnable(){
            @Override
            public void run() {
                cancel();
                if(!plugin.gametime){
                    return;
                }
                Random rnd1 = new Random();
                Random rnd2 = new Random();
                Random rnd3 = new Random();
                int dice1 = rnd1.nextInt(6)+1;
                int dice2 = rnd2.nextInt(6)+1;
                int dice3 = rnd3.nextInt(6)+1;
                sendKankeisya("§a§lﾊﾟｶｯ！  §f§l "+dice1+"・"+dice2+"・"+dice3+" ！！");
                String result = hantei.mainhantei(dice1,dice2,dice3);
                if(result.equalsIgnoreCase("ナシ")){
                    sendKankeisya("§a§l役無し (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    plugin.oyayaku = 0;
                    childturn();
                }else if(result.equalsIgnoreCase("dan5")){
                    sendKankeisya("§6§lダンゴ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                    oyaLose(2.0);
                }else if(result.equalsIgnoreCase("イチ")){
                    sendKankeisya("§f§lイチが役に決まりました！");
                    plugin.oyayaku = 1;
                    childturn();
                }else if(result.equalsIgnoreCase("ニ")){
                    sendKankeisya("§f§lニが役に決まりました！");
                    plugin.oyayaku = 2;
                    childturn();
                }else if(result.equalsIgnoreCase("サン")){
                    sendKankeisya("§f§lサンが役に決まりました！");
                    plugin.oyayaku = 3;
                    childturn();
                }else if(result.equalsIgnoreCase("シ")){
                    sendKankeisya("§f§lシが役に決まりました！");
                    plugin.oyayaku = 4;
                    childturn();
                }else if(result.equalsIgnoreCase("ゴ")){
                    sendKankeisya("§f§lゴが役に決まりました！");
                    plugin.oyayaku = 5;
                    childturn();
                }else if(result.equalsIgnoreCase("ロ")){
                    sendKankeisya("§f§lロが役に決まりました！");
                    plugin.oyayaku = 6;
                    childturn();
                }else if(result.equalsIgnoreCase("man10")){
                    sendKankeisya("§c§lマンジュウ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    oyaWin(2.0);
                }else if(result.equalsIgnoreCase("ゾロメ")){
                    sendKankeisya("§4§lゾロメ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                    oyaWin(3.0);
                }else if(result.equalsIgnoreCase("ピンゾロ")){
                    Bukkit.broadcastMessage(plugin.prefix+"§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa§6§l§n§o ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                    Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §a§l親の勝利！！");
                    double jack = -1;
                    if(plugin.getJackpot() < plugin.bet * 10){
                        jack = plugin.getJackpot();
                    }else {
                        jack = plugin.bet * 10;
                    }
                    plugin.takeJackpot(jack);
                    plugin.vault.givePlayerMoney(plugin.parent,jack,TransactionType.DEPOSIT,"mcr jackpot!! user: "+Bukkit.getPlayer(plugin.parent).getName());
                    sendTitle("§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa","§e§l当選者: §f§l"+Bukkit.getPlayer(plugin.parent).getName()+" §6§l当選金額: §f§l"+new JPYBalanceFormat(jack).getString()+"円",100);
                    plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal,TransactionCategory.GAMBLE,TransactionType.WIN,"mcr jackpot!! user: "+Bukkit.getPlayer(plugin.parent).getName());
                    Bukkit.broadcastMessage(plugin.prefix+"§a§l"+Bukkit.getPlayer(plugin.parent).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5*plugin.maxplayers).getString()+"円 → "+new JPYBalanceFormat(plugin.parentbal + jack).getString()+"円");
                    reset();
                }else if(result.equalsIgnoreCase("サイコー")){
                    sendKankeisya("§a§l§nサイコー (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    oyaWin(4);
                }else if(result.equalsIgnoreCase("オマンコロ")){
                    sendKankeisya("§4§l§nマンコロリン 来ちゃった(∀｀*ゞ)ﾃﾍｯ");
                    gotoJackpot();
                }else if(result.equalsIgnoreCase("ヒフミ")){
                    sendKankeisya("§a§lﾀﾞｰ!!");
                    oyaWin(2.5);
                }else{
                    Bukkit.broadcastMessage(plugin.prefix+"§4エラー発生。未知の目です。");
                    reset();
                }
            }
        }.runTaskTimer(plugin,100,20);
    }

    public static boolean jackskip = false;

    public static void childturn(){
        sendKankeisya("§c§l子のターンが開始されました！");
        new BukkitRunnable(){
            @Override
            public void run() {
                if(plugin.kolist.size()==0){
                    cancel();
                    if(!plugin.game){
                        return;
                    }
                    plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal - (plugin.parentbal/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr parent deposit: "+Bukkit.getPlayer(plugin.parent).getName());
                    Bukkit.broadcastMessage(plugin.prefix+"§a§l"+Bukkit.getPlayer(plugin.oya).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5*plugin.maxplayers).getString()+"円 → "+new JPYBalanceFormat(plugin.parentbal).getString()+"円§e(うち手数料"+new JPYBalanceFormat((plugin.parentbal/100)).getString()+"円)");
                    plugin.addJackpot(plugin.parentbal/100);
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
        sendKankeisya("§c§l"+p.getName()+"§f§lさん(子)がサイコロを振っています…§e§l§kaaa");
        new BukkitRunnable(){
            @Override
            public void run() {
                cancel();
                if(!plugin.game){
                    return;
                }
                Random rnd1 = new Random();
                Random rnd2 = new Random();
                Random rnd3 = new Random();
                int dice1 = rnd1.nextInt(6)+1;
                int dice2 = rnd2.nextInt(6)+1;
                int dice3 = rnd3.nextInt(6)+1;
                sendKankeisya("§a§lﾊﾟｶｯ！  §f§l "+dice1+"・"+dice2+"・"+dice3+" ！！");
                String result = hantei.mainhantei(dice1,dice2,dice3);
                if(result.equalsIgnoreCase("ナシ")){
                    sendKankeisya("§a§l役無し (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    if(plugin.oyayaku == 0){
                        draw(uuid);
                    }else {
                        vsOya(true,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("dan5")){
                    sendKankeisya("§6§lダンゴ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                    vsOya(true,uuid,2.0);
                }else if(result.equalsIgnoreCase("イチ")){
                    sendKankeisya("§e§lイチ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    if(plugin.oyayaku == 1){
                        draw(uuid);
                    }else if(plugin.oyayaku > 1){
                        vsOya(true,uuid,1.0);
                    }else {
                        vsOya(false,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("ニ")){
                    sendKankeisya("§f§lニが役に決まりました！");
                    if(plugin.oyayaku == 2){
                        draw(uuid);
                    }else if(plugin.oyayaku > 2){
                        vsOya(true,uuid,1.0);
                    }else {
                        vsOya(false,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("サン")){
                    sendKankeisya("§f§lサンが役に決まりました！");
                    if(plugin.oyayaku == 3){
                        draw(uuid);
                    }else if(plugin.oyayaku > 3){
                        vsOya(true,uuid,1.0);
                    }else {
                        vsOya(false,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("シ")){
                    sendKankeisya("§f§lシが役に決まりました！");
                    if(plugin.oyayaku == 4){
                        draw(uuid);
                    }else if(plugin.oyayaku > 4){
                        vsOya(true,uuid,1.0);
                    }else {
                        vsOya(false,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("ゴ")){
                    sendKankeisya("§f§lゴが役に決まりました！");
                    if(plugin.oyayaku == 5){
                        draw(uuid);
                    }else if(plugin.oyayaku > 5){
                        vsOya(true,uuid,1.0);
                    }else {
                        vsOya(false,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("ロ")){
                    sendKankeisya("§c§lロ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    if(plugin.oyayaku == 6){
                        draw(uuid);
                    }else if(plugin.oyayaku > 6){
                        vsOya(true,uuid,1.0);
                    }else {
                        vsOya(false,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("man10")){
                    sendKankeisya("§c§lマンジュウ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    vsOya(false,uuid,2.0);
                }else if(result.equalsIgnoreCase("ゾロメ")){
                    sendKankeisya("§4§lゾロメ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                    vsOya(false,uuid,3.0);
                }else if(result.equalsIgnoreCase("ピンゾロ")){
                    Bukkit.broadcastMessage(plugin.prefix+"§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa§6§l§n§o ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                    Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §a§l子の勝利！！");
                    double jack = -1;
                    if(plugin.getJackpot() < plugin.bet * 10){
                        jack = plugin.getJackpot();
                    }else {
                        jack = plugin.bet * 10;
                    }
                    plugin.takeJackpot(jack);
                    plugin.vault.givePlayerMoney(uuid,jack,TransactionType.WIN,"mcr jackpot!! deposit: "+Bukkit.getPlayer(uuid).getName());
                    sendTitle("§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa","§e§l当選者: §f§l"+Bukkit.getPlayer(uuid).getName()+" §6§l当選金額: §f§l"+new JPYBalanceFormat(jack).getString()+"円",100);
                    plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,plugin.onebet*5,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr jackpot!! deposit: "+Bukkit.getPlayer(uuid).getName());
                    Bukkit.broadcastMessage(plugin.prefix+"§a§l"+Bukkit.getPlayer(uuid).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5).getString()+"円 → "+new JPYBalanceFormat(plugin.onebet*5 + jack).getString()+"円");
                    plugin.kolist.remove(uuid);
                }else if(result.equalsIgnoreCase("サイコー")){
                    sendKankeisya("§a§l役無し (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                    if(plugin.oyayaku == 0){
                        draw(uuid);
                    }else {
                        vsOya(true,uuid,1.0);
                    }
                }else if(result.equalsIgnoreCase("オマンコロ")){
                    sendKankeisya("§4§l§nマンコロリン 来ちゃった(∀｀*ゞ)ﾃﾍｯ");
                    gotoJackpot();
                }else if(result.equalsIgnoreCase("ヒフミ")){
                    sendKankeisya("§a§lﾀﾞｰ!!");
                    vsOya(false,uuid,2.5);
                }else{
                    Bukkit.broadcastMessage(plugin.prefix+"§4エラー発生。未知の目です。");
                    reset();
                }
            }
        }.runTaskTimer(plugin,100,20);
    }

    public static void oyaWin(double bairitu){
        Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §a§l親の勝利！！");
        double with = plugin.bet * bairitu * plugin.hito;
        plugin.oyabal = with + plugin.oyabal;
        double retn = (plugin.bet * 5) - (plugin.bet * bairitu) - (plugin.bet/100);
        for(Player p:plugin.kolist){
            plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,retn,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr lose return: "+Bukkit.getPlayer(uuid).getName());
            Bukkit.broadcastMessage(plugin.prefix+"§c§l"+p.getName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.bet*5).getString()+"円 → "+new JPYBalanceFormat(retn+(plugin.onebet/100)).getString()+"円§e(うち手数料"+new JPYBalanceFormat((plugin.onebet/100)).getString()+"円)");
            plugin.addJackpot(plugin.bet/100);
        }
        plugin.kolist.clear();
        plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal - (plugin.parentbal/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr parent deposit: "+Bukkit.getPlayer(plugin.parent).getName());
        Bukkit.broadcastMessage(plugin.prefix+"§a§l"+Bukkit.getPlayer(plugin.parent).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5*plugin.maxplayers).getString()+"円 → "+new JPYBalanceFormat(plugin.parentbal).getString()+"円§e(うち手数料"+new JPYBalanceFormat((plugin.parentbal/100)).getString()+"円)");
        plugin.addJackpot(plugin.oyabal/100);
        reset();
    }
    public static void oyaLose(double bairitu){
        Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §c§l子の勝利！！");
        double with = plugin.bet * bairitu ;
        plugin.oyabal = plugin.oyabal - (with*plugin.maxplayers);
        for(Player p:plugin.kolist){
            Bukkit.broadcastMessage(plugin.prefix+"§c§l"+p.getName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5).getString()+"円 → "+new JPYBalanceFormat(with + (plugin.onebet * 5)).getString()+"円§e(うち手数料"+new JPYBalanceFormat((plugin.onebet/100)).getString()+"円)");
            plugin.addJackpot(plugin.bet/100);
            plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,with + (plugin.onebet * 5) - (plugin.onebet/100) ,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr win: "+Bukkit.getPlayer(uuid).getName());
        }
        plugin.kolist.clear();
        plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal - (plugin.parentbal/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr parent deposit: "+Bukkit.getPlayer(plugin.parent).getName());
        Bukkit.broadcastMessage(plugin.prefix+"§a§l"+Bukkit.getPlayer(plugin.parent).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5*plugin.maxplayers).getString()+"円 → "+new JPYBalanceFormat(plugin.parentbal).getString()+"円§e(うち手数料"+new JPYBalanceFormat((plugin.parentbal/100)).getString()+"円)");
        plugin.addJackpot(plugin.parentbal/100);
        reset();
    }

    public static void vsOya(boolean Oyawin,Player p,double bairitu){
        if(Oyawin){
            Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §a§l親の勝利！！");
            double with = plugin.bet * bairitu;
            plugin.oyabal = plugin.oyabal + with;
            double retn = (plugin.bet * 5) - (plugin.bet * bairitu);
            Bukkit.broadcastMessage(plugin.prefix+"§c§l"+Bukkit.getPlayer(uuid).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5).getString()+"円 → "+new JPYBalanceFormat(retn).getString()+"円§e(うち手数料"+new JPYBalanceFormat((plugin.onebet/100)).getString()+"円)");
            plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,retn - (plugin.onebet/100),TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr lose return: "+Bukkit.getPlayer(uuid).getName());
            plugin.addJackpot(plugin.onebet/100);
        }else{
            Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §c§l子の勝利！！");
            double with = plugin.onebet * bairitu;
            plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,with + (plugin.onebet * 5) - (plugin.onebet/100),TransactionCategory.GAMBLE,TransactionType.WIN,"mcr win: "+Bukkit.getPlayer(uuid).getName());
            Bukkit.broadcastMessage(plugin.prefix+"§c§l"+Bukkit.getPlayer(uuid).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5).getString()+"円 → "+new JPYBalanceFormat(with + (plugin.onebet * 5)).getString()+"円§e(うち手数料"+new JPYBalanceFormat((plugin.onebet/100)).getString()+"円)");
            plugin.addJackpot(plugin.onebet/100);
            plugin.parentbal = plugin.parentbal - (with);
        }
        plugin.kolist.remove(p);
    }

    public static void draw(Player p){
        Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §a§l引き分け！！");
        Bukkit.broadcastMessage(plugin.prefix+"§c§l"+p.getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5).getString()+"円 → "+new JPYBalanceFormat(plugin.onebet*5).getString()+"円");
        plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,plugin.onebet*5,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr draw: "+Bukkit.getPlayer(uuid).getName());
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
        Bukkit.getPlayer(plugin.oya).sendMessage(plugin.prefix+message);
    }

    public static void gotoJackpot(){
        Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §4§l全 員 敗 北");
        double with = plugin.bet * 5;
        for(Player p:plugin.kolist){
            Bukkit.broadcastMessage(plugin.prefix+"§c§l"+p.getName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5).getString()+"円 → "+new JPYBalanceFormat(0).getString()+"円§e(全額手数料"+new JPYBalanceFormat(with).getString()+"円)");
            plugin.addJackpot(with);
        }
        plugin.kolist.clear();
        Bukkit.broadcastMessage(plugin.prefix+"§a§l"+Bukkit.getPlayer(plugin.oya).getDisplayName()+"§f§l: §e§l"+new JPYBalanceFormat(plugin.onebet*5*plugin.maxplayers).getString()+"円 → "+new JPYBalanceFormat(0).getString()+"円§e(全額手数料"+new JPYBalanceFormat(plugin.parentbal).getString()+"円)");
        plugin.addJackpot(plugin.oyabal);
        plugin.oyabal = 0;
        reset();
    }

}
