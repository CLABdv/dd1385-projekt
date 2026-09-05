public class Main {
    public static void main(String[] args) {
        Model model = new Model(1000, 1000, 5, 100000);
        View view = new View(model);
        Controller controller = new Controller(model, view, 10);
        
    }
}
