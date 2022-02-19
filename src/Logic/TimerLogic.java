package Logic;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerLogic extends TimerTask {
    @Override
    public void run() {
        System.out.println("TimerTask начал свое выполнение в:" + new Date());
        completeTask();
        System.out.println("TimerTask закончил свое выполнение в:" + new Date());
    }

    private void completeTask() {
        try {
            // допустим, выполнение займет 20 секунд
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        TimerTask timerTask = new TimerLogic();
        Timer timer = new Timer(true);  // стартуем TimerTask в виде демона
        timer.scheduleAtFixedRate(timerTask, 0, 10*1000);// будем запускать каждых 10 секунд (10 * 1000 миллисекунд)
        System.out.println("TimerTask начал выполнение");
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel(); // вызываем cancel() через какое-то время
        System.out.println("TimerTask прекращена");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
