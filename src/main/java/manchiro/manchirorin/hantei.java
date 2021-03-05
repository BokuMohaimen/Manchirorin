package manchiro.manchirorin;

public class hantei {
    public static void loadEnable(Manchirorin plugin){
        MCHData.plugin = plugin;
    }
    public static String mainhantei(int i,int ii,int iii){
        if (i == ii && ii == iii) {
            if (i == 1) {
                return "ピンゾロ";
            }
            if (i == 6) {
                return "オマンコロ";
            }
            return "ゾロメ";
        }
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        if (i == 1 || ii == 1 || iii == 1) {
            i1++;
        }
        if (i == 2 || ii == 2 || iii == 2) {
            i2++;
        }
        if (i == 3 || ii == 3 || iii == 3) {
            i3++;
        }
        if (i1 == i && i1 == i2 && i1 == i3) {
            return "ヒフミ";
        }
        if (i == 3 && ii == 1 && iii == 5) {
            return "サイコー";
        }
        if (i + ii + iii == 10) {
            return "man10";
        }
        if (i + ii + iii == 5) {
            return "dan5";
        }
        if (i == ii) {
            switch (iii) {
                case 1:
                    return "イチ";
                case 2:
                    return "ニ";
                case 3:
                    return "サン";
                case 4:
                    return "シ";
                case 5:
                    return "ゴ";
                case 6:
                    return "ロ";
            }
        }
        if (i == iii) {
            switch (ii) {
                case 1:
                    return "イチ";
                case 2:
                    return "ニ";
                case 3:
                    return "サン";
                case 4:
                    return "シ";
                case 5:
                    return "ゴ";
                case 6:
                    return "ロ";
            }
        }
        if (ii == iii) {
            switch (i) {
                case 1:
                    return "イチ";
                case 2:
                    return "ニ";
                case 3:
                    return "サン";
                case 4:
                    return "シ";
                case 5:
                    return "ゴ";
                case 6:
                    return "ロ";
            }
        }
        return "ナシ";
    }
}