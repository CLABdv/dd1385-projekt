import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;

public class Controller {
    Model model;
    View view;
    Timer timer;
    TimerTask currenTask = null;
    boolean isPaused = true;
    int delay=50;
    Controller(Model model, View view, long period) {
        this.model = model;
        this.view = view;
        view.model = model;
        timer = new Timer();
        view.game.updateBuf();
        view.repaint();
        view.pauseButton.addActionListener(new Pauser());
        view.speedSlider.addChangeListener(new SleepListener());
        view.lengthSlider.addChangeListener(new LengthListener());
    }
    
    class ViewUpdater extends TimerTask {

        @Override
        public void run() {
            model.update();
            view.game.updateBuf();
            view.repaint();
        }
    }
    
    class SleepListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            delay = Math.max(1, source.getValue());
            if (currenTask != null) {
                currenTask.cancel();
                currenTask = new ViewUpdater();
                timer.schedule(currenTask, delay, delay);
            }
        }

    }

    class LengthListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            model.l = source.getValue();
        }
    }
    
    class Pauser implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currenTask != null) {
                currenTask.cancel();
                currenTask=null;
            } else {
                currenTask = new ViewUpdater(); 
                timer.schedule(currenTask, 0, delay);
            }
        }
        
    }
}
