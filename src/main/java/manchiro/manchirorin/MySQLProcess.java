package manchiro.manchirorin;

import org.bukkit.entity.Player;

public class MySQLProcess {
    Player p;
    Manchirorin pl;
    public MySQLProcess(Player p, Manchirorin pl){
        this.p = p;
        this.pl= pl;
    }
    public void run(){
        String names = "";
        for (Player p : pl.kolist) {
            names = names+"`"+p.getName();
        }
        MySQLManager mysql = new MySQLManager(pl, "Manchirorin");
        mysql.execute("INSERT INTO manchiro_log (oya,ko,kakekin) values ('" + pl.oya.getName() + "','" + names + "','" + pl.bet*5 + "');");
        mysql.close();
    }
}
