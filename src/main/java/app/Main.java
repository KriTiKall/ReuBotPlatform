package app;

import view.bot.TgListener;
public class Main {
    public static void main(String[] args){
        Init init = () -> {
            TgListener.start();
        };
        init.init();
    }
}
interface Init{
    void init();
}
