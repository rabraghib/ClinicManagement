import views.WelcomeView;
import views.LoginView;
import views.MainMenuView;

public class Main {
    public static void main(String[] args) {
        while (true) {
            WelcomeView.show();
            LoginView.ensureLoggedIn();
            new MainMenuView().show();
        }
    }
}