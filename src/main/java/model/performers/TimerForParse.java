package model.performers;

import java.util.Timer;
import java.util.TimerTask;

public class TimerForParse {
    static void timer() {
        int minutes = 10;
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //хуй хуй хуй хуй хуй хуй хуй хуй хуй
            }
        }, 0, 1000 * 60 * minutes);
    }
}
