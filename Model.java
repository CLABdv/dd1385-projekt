import java.util.*;

public class Model {
    private Random r;
    public int width;
    public int height;
    public double l;
    public HashSet<Position> stopped; // we only store rounded values here
    public Particle[] particles;

    public Model(int width, int height, double l, int n) {
        r = new Random();
        this.width = width;
        this.height = height;
        this.l = l;
        stopped = new HashSet<Position>();
        particles = new Particle[n];
        for (int i = 0; i < n; i++)
            particles[i] = new Particle();

        // borders
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // assumes l is small relative to window size
                // which imho is a fair assumption, kinda useless to simulate particle moving
                // around
                // if one step is huge
                if (i <= l || i >= width - 1 - l || j <= l || j >= height - 1 - l)
                    stopped.add(new Position(i, j));
            }
        }

    }

    public void update() {
        for (int i = 0; i < particles.length; i++) {
            particles[i].updatePosition();
        }
    }

    class Position {
        public double x;
        public double y;

        Position(double x, double y) {
            this.x = x;
            this.y = y;
        }

        Position() {
            x = r.nextDouble(l,width-l);
            y = r.nextDouble(l,height-l);
        }

        // to be able to use position as key in hashmap
        @Override
        public int hashCode() {
            return (int)y * width+(int)x;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Position other = (Position) obj;
            if (other.x == this.x && other.y == this.y) {
                return true;
            }
            return false;
        }
    }

    class Particle {
        public Position pos;
        public boolean moving = true;

        Particle() {
            pos = new Position();
        }

        Particle(double x, double y) {
            pos = new Position(x, y);
        }

        // NOTE
        // if we change l, like make it much bigger, then the particles may jump over the border
        void updatePosition() {
            if (moving) {
                double phi = r.nextDouble(2 * Math.PI);
                pos.x += l * Math.cos(phi);
                pos.y += l * Math.sin(phi);
                int checkx = (int) Math.round(pos.x);
                int checky = (int) Math.round(pos.y);
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (stopped.contains(new Position(checkx + i, checky + j))) {
                            stopped.add(new Position(checkx, checky));
                            moving = false;
                        }
                    }
                }
                // dont need to add to set cause its outside so wont see anything either way
                if (checkx < 0 || checkx > width || checky < 0 || checky > height) moving = false;
            }
        }
    }
}
