import javax.swing.*;

import java.awt.*;
import java.awt.image.*;

public class View extends JFrame {
    public Model model;
    public GameWindow game;
    public JButton pauseButton;
    public JSlider speedSlider;
    public JSlider lengthSlider;


    public View(Model model) {
        this.model = model;
        setSize(new Dimension(model.width, model.height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        game = new GameWindow();
        add(game, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        pauseButton = new JButton("PAUSE/PLAY");

        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new BorderLayout());
        speedSlider = new JSlider(0, 1000, 50);
        speedSlider.setMinorTickSpacing(50);
        speedSlider.setMajorTickSpacing(200);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedPanel.add(new JLabel("Delay (MS)"), BorderLayout.NORTH);
        speedPanel.add(speedSlider, BorderLayout.SOUTH);

        lengthSlider = new JSlider(1, 50, 2);
        lengthSlider.setMinorTickSpacing(5);
        lengthSlider.setMajorTickSpacing(10);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setPaintLabels(true);
        JPanel lengthPanel = new JPanel();
        lengthPanel.setLayout(new BorderLayout());
        lengthPanel.add(new JLabel("Step length (pixels)"), BorderLayout.NORTH);
        lengthPanel.add(lengthSlider, BorderLayout.SOUTH);

        controlPanel.add(pauseButton);
        controlPanel.add(speedPanel);
        controlPanel.add(lengthPanel);

        add(controlPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    class GameWindow extends JComponent {
        private final int STOPPEDCOL = 0xff0000;
        private final int MOVINGCOL = 0xffffff;
        private final int BACKGROUND = 0xff;
        BufferedImage[] imbuf = new BufferedImage[2]; // to make it so that super fast draws arent glitchy
        int currentBuffer = 0; // aka currently not rendered buffer, aka buffer to draw to

        public GameWindow() {
            for (int i = 0; i < 2; i++) {
                imbuf[i] = new BufferedImage(model.width, model.height, BufferedImage.TYPE_INT_RGB);
            }
        }

        public void updateBuf() {
            Graphics2D clr = imbuf[currentBuffer].createGraphics();
            clr.setColor(new Color(BACKGROUND));
            clr.fillRect(0, 0, model.width, model.height);
            for (int i = 0; i < model.particles.length; i++) {
                int x = (int) Math.round(model.particles[i].pos.x);
                int y = (int) Math.round(model.particles[i].pos.y);
                if (0 <= x && x < model.width && 0 <= y && y < model.height) {
                    int color = model.particles[i].moving ? MOVINGCOL : STOPPEDCOL;
                    imbuf[currentBuffer].setRGB(x, y, color);
                }
            }
            currentBuffer^=1;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.drawRenderedImage(imbuf[currentBuffer^1], null);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(model.width, model.height);
        }
    }
}
