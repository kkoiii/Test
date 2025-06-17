import Controller.MenuController;
import View.MenuView;

public class MainProgram {
    public static void main(String[] args) {
        MenuController menuController = new MenuController();
        
        while (true) {
            MenuView.displayMenu();
            int choice = MenuView.getUserChoice();
            MenuView.processUserChoice(choice);
        }
    }
}