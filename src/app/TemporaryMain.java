package app;

import data_access.*;
import entity.CommonUserFactory;
import interface_adapter.MainMenu.MainMenuController;
import interface_adapter.Guest.GuestController;
import interface_adapter.LoggedIn.LoggedInController;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.MainMenu.MainMenuViewModel;
import interface_adapter.Guest.GuestViewModel;
import interface_adapter.LoggedIn.LoggedInViewModel;
import interface_adapter.Signup.SignupViewModel;
import interface_adapter.Game.GameViewModel;
import interface_adapter.ViewManagerModel;
import use_case.Guest.GuestInteractor;
import use_case.Guest.GuestUserDataAccessInterface;
import use_case.Game.GameDataAccessInterface;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;
import view.GuestView;
import view.MainMenuView;
import view.GameView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TemporaryMain {

    private static String apiKey; // Replace with your actual API key
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions"; // Adjust as needed
    public static void main(String[] args) throws IOException{
        // Build the main program window, the main panel containing the
        // various cards, and the layout, and stitch them together.

        // The main application window.
        JFrame application = new JFrame("our game demo");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();

        JPanel views = new JPanel(cardLayout);
        application.add(views);

        ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        LoginViewModel loginViewModel = new LoginViewModel();
        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        SignupViewModel signupViewModel = new SignupViewModel();
        GuestViewModel guestViewModel = new GuestViewModel();
        MainMenuViewModel mainMenuViewModel = new MainMenuViewModel();
        GameViewModel gameViewModel = new GameViewModel();

        LoggedInController loggedInController = new LoggedInController(loggedInViewModel, viewManagerModel);
        MainMenuController mainMenuController = new MainMenuController(viewManagerModel, signupViewModel, loginViewModel,
                guestViewModel);
        FileUserDataAccessObject userDataAccessObject;
        try {
            userDataAccessObject = new FileUserDataAccessObject("./users.csv", new CommonUserFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Not yet set up
        GuestUserDataAccessInterface guestUserDataAccessInterface;

        ConfigLoader config = new ConfigLoader();
        String apiKey = config.getApiKey();
        OpenAI api = new OpenAI(ENDPOINT, apiKey); // Assuming OpenAI constructor takes apiKey

//        // Data access object for game
        GameDataAccessInterface data = new GameData();

        SignupView signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel, signupViewModel, userDataAccessObject, userDataAccessObject, loggedInViewModel);
        views.add(signupView, signupView.viewName);

        LoginView loginView = LoginUseCaseFactory.create(viewManagerModel, loginViewModel, loggedInViewModel, userDataAccessObject);
        views.add(loginView, loginView.viewName);

        LoggedInView loggedInView = new LoggedInView(loggedInViewModel, loggedInController);
        views.add(loggedInView, loggedInView.viewName);

        GuestView guestView = GuestUseCaseFactory.create(viewManagerModel,guestViewModel,userDataAccessObject);
        views.add(guestView, guestView.viewName);

        MainMenuView mainMenuView = new MainMenuView(mainMenuViewModel, mainMenuController, signupViewModel,
                loginViewModel, guestViewModel);
        views.add(mainMenuView, mainMenuView.gameName);

        JPanel[] gameViews = GameUseCaseFactory.create(viewManagerModel, data, api);
        for (JPanel gameView : gameViews) {
            views.add(gameView, gameView.getName());
            System.out.println(gameView.getName());
        }

        viewManagerModel.setActiveView(mainMenuView.gameName);
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setVisible(true);
    }
}