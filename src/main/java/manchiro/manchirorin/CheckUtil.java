package manchiro.manchirorin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static manchiro.manchirorin.MCHData.*;

public class CheckUtil {

    public static void checkGamePush1(String result) {

        switch (result) {

            case "ナシ":
                sendKankeisya("§a§l役無し (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                plugin.oyayaku = 0;
                childturn();
                break;

            case "dan5":
                sendKankeisya("§6§lダンゴ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                oyaLose(2.0);
                break;

            case "イチ":
                sendKankeisya("§f§lイチが役に決まりました！");
                plugin.oyayaku = 1;
                childturn();
                break;

            case "ニ":
                sendKankeisya("§f§lニが役に決まりました！");
                plugin.oyayaku = 2;
                childturn();
                break;

            case "サン":
                sendKankeisya("§f§lサンが役に決まりました！");
                plugin.oyayaku = 3;
                childturn();
                break;

            case "シ":
                sendKankeisya("§f§lシが役に決まりました！");
                plugin.oyayaku = 4;
                childturn();
                break;

            case "ゴ":
                sendKankeisya("§f§lゴが役に決まりました！");
                plugin.oyayaku = 5;
                childturn();
                break;

            case "ロ":
                sendKankeisya("§f§lロが役に決まりました！");
                plugin.oyayaku = 6;
                childturn();
                break;

            case "mam10":
                sendKankeisya("§c§lマンジュウ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                oyaWin(2.0);
                break;

            case "ゾロメ":
                sendKankeisya("§4§lゾロメ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                oyaWin(3.0);
                break;

            case "ピンゾロ":
                Bukkit.broadcastMessage(plugin.prefix+"§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa§6§l§n§o ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                Bukkit.broadcastMessage(plugin.prefix+"§e§l結果: §a§l親の勝利！！");
                double jack = -1;
                if(plugin.getJackpot() < plugin.bet * 10){
                    jack = plugin.getJackpot();
                }else {
                    jack = plugin.bet * 10;
                }
                plugin.takeJackpot(jack);
                plugin.vault.deposit(plugin.oya, jack);
                //plugin.vault.givePlayerMoney(plugin.parent,jack,TransactionType.DEPOSIT,"mcr jackpot!! user: "+Bukkit.getPlayer(plugin.parent).getName());
                sendTitle("§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa","§e§l当選者: §f§l"+Bukkit.getPlayer(plugin.parent).getName()+" §6§l当選金額: §f§l"+new JPYBalanceFormat(jack).getString()+"円",100);
                plugin.vault.deposit(plugin.oya, plugin.oyabal);
                //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),plugin.parent,plugin.parentbal,TransactionCategory.GAMBLE,TransactionType.WIN,"mcr jackpot!! user: "+Bukkit.getPlayer(plugin.parent).getName());
                Bukkit.broadcastMessage(plugin.prefix+"§a§l"+plugin.oya.getDisplayName()+"§f§l: §e§l"+plugin.bet*5*plugin.hito+" 円 → "+plugin.oyabal + jack+" 円");
                reset();
                break;

            case "サイコー":
                sendKankeisya("§a§l§nサイコー (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                oyaWin(4);
                break;

            case "オマンコロ":
                sendKankeisya("§4§l§nマンコロリン 来ちゃった(∀｀*ゞ)ﾃﾍｯ");
                gotoJackpot();
                break;

            case "ヒフミ":
                sendKankeisya("§a§lﾀﾞｰ!!");
                oyaWin(2.5);
                break;

            default:
                Bukkit.broadcastMessage(plugin.prefix+"§4エラー発生。未知の目です。");
                reset();
                break;
        }

    }

    public static void checkChildrenBattle(String result, Player p) {

        switch (result) {

            case "ナシ":

            case "サイコー":
                sendKankeisya("§a§l役無し (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                if (plugin.oyayaku == 0) {
                    draw(p);
                } else {
                    vsOya(true, p, 1.0);
                }
                break;

            case "dan5":
                sendKankeisya("§6§lダンゴ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                vsOya(true, p, 2.0);
                break;

            case "イチ":
                sendKankeisya("§e§lイチ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                if (plugin.oyayaku == 1) {
                    draw(p);
                } else if (plugin.oyayaku > 1) {
                    vsOya(true, p, 1.0);
                } else {
                    vsOya(false, p, 1.0);
                }
                break;

            case "ニ":
                sendKankeisya("§f§lニが役に決まりました！");
                if (plugin.oyayaku == 2) {
                    draw(p);
                } else if (plugin.oyayaku > 2) {
                    vsOya(true, p, 1.0);
                } else {
                    vsOya(false, p, 1.0);
                }
                break;

            case "サン":
                sendKankeisya("§f§lサンが役に決まりました！");
                if (plugin.oyayaku == 3) {
                    draw(p);
                } else if (plugin.oyayaku > 3) {
                    vsOya(true, p, 1.0);
                } else {
                    vsOya(false, p, 1.0);
                }
                break;

            case "シ":
                sendKankeisya("§f§lシが役に決まりました！");
                if (plugin.oyayaku == 4) {
                    draw(p);
                } else if (plugin.oyayaku > 4) {
                    vsOya(true, p, 1.0);
                } else {
                    vsOya(false, p, 1.0);
                }
                break;

            case "ゴ":
                sendKankeisya("§f§lゴが役に決まりました！");
                if (plugin.oyayaku == 5) {
                    draw(p);
                } else if (plugin.oyayaku > 5) {
                    vsOya(true, p, 1.0);
                } else {
                    vsOya(false, p, 1.0);
                }
                break;

            case "ロ":
                sendKankeisya("§c§lロ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                if (plugin.oyayaku == 6) {
                    draw(p);
                } else if (plugin.oyayaku > 6) {
                    vsOya(true, p, 1.0);
                } else {
                    vsOya(false, p, 1.0);
                }
                break;

            case "man10":
                sendKankeisya("§c§lマンジュウ (ﾟ∀ﾟ)ｷﾀｺﾚ!!");
                vsOya(false, p, 2.0);
                break;

            case "ゾロメ":
                sendKankeisya("§4§lゾロメ ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                vsOya(false, p, 3.0);
                break;

            case "ピンゾロ":
                Bukkit.broadcastMessage(plugin.prefix + "§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa§6§l§n§o ｷﾀ━━━━(ﾟ∀ﾟ)━━━━!!");
                Bukkit.broadcastMessage(plugin.prefix + "§e§l結果: §a§l子の勝利！！");
                double jack = -1;
                if (plugin.getJackpot() < plugin.bet * 10) {
                    jack = plugin.getJackpot();
                } else {
                    jack = plugin.bet * 10;
                }
                plugin.takeJackpot(jack);
                plugin.vault.deposit(p.getPlayer(), jack);
                //plugin.vault.givePlayerMoney(uuid,jack,TransactionType.WIN,"mcr jackpot!! deposit: "+Bukkit.getPlayer(uuid).getName());
                sendTitle("§0§l§kaaaaa§4§lJ§6§lA§e§lC§a§lK§2§lP§b§lO§3§lT§0§l§kaaaaa", "§e§l当選者: §f§l" + Bukkit.getPlayer(uuid).getName() + " §6§l当選金額: §f§l" + new JPYBalanceFormat(jack).getString() + "円", 100);
                plugin.vault.deposit(p.getPlayer(), plugin.bet * 5);
                //plugin.vault.transferMoneyPoolToPlayer(plugin.totalBet.getId(),uuid,plugin.onebet*5,TransactionCategory.GAMBLE,TransactionType.DEPOSIT,"mcr jackpot!! deposit: "+Bukkit.getPlayer(uuid).getName());
                Bukkit.broadcastMessage(plugin.prefix + "§a§l" + p.getDisplayName() + "§f§l: §e§l" + plugin.bet * 5 + " 円 → " + plugin.bet * 5 + jack + " 円");
                plugin.kolist.remove(p);
                break;

            case "オマンコロ":
                sendKankeisya("§4§l§nマンコロリン 来ちゃった(∀｀*ゞ)ﾃﾍｯ");
                gotoJackpot();
                break;

            case "ヒフミ":
                sendKankeisya("§a§lﾀﾞｰ!!");
                vsOya(false, p, 2.5);
                break;

            default:
                Bukkit.broadcastMessage(plugin.prefix + "§4エラー発生。未知の目です。");
                reset();
                break;
        }

    }

}
