package app;

import data_access.FileUserDataAccessObject;
import entity.CommonUserFactory;
import use_case.leaderboard.view.LeaderboardView;
import use_case.login.view.LoginView;
import use_case.menu.interface_adapter.MenuViewModel;
import use_case.menu.view.MenuView;
import use_case.signup.view.SignupView;
import use_case.view_team.interface_adapter.ViewTeamViewModel;
import use_case.view_team.view.ViewTeamView;
import view.ViewManagerModel;
import use_case.leaderboard.interface_adapter.LeaderboardViewModel;
import view.LoggedInViewModel;
import use_case.login.interface_adapter.LoginViewModel;
import use_case.signup.interface_adapter.SignupViewModel;
import view.*;
import data_access.APIDataAccessObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Build the main program window, the main panel containing the
        // various cards, and the layout, and stitch them together.

        // The main application window.
        JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        CardLayout cardLayout = new CardLayout();

        // ToDo: Changed Menu View to grid so options appear down a column?
//        int columns = 3;
//        int rows = 0; // You can adjust the number of rows based on your needs
//        GridLayout gridLayout = new GridLayout(rows, columns);

        // The various View objects. Only one view is visible at a time.
        JPanel views = new JPanel(cardLayout);
        application.add(views, BorderLayout.CENTER);

        // This keeps track of and manages which view is currently showing.
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // The data for the views, such as username and password, are in the ViewModels.
        // This information will be changed by a presenter object that is reporting the
        // results from the use case. The ViewModels are observable, and will
        // be observed by the Views.
        MenuViewModel menuViewModel = new MenuViewModel();
        LeaderboardViewModel leaderboardViewModel = new LeaderboardViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();
        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        SignupViewModel signupViewModel = new SignupViewModel();
        ViewTeamViewModel viewTeamViewModel = new ViewTeamViewModel();

        FileUserDataAccessObject userDataAccessObject;
        try {
            File csvfile = new File("./users.csv");
            assert csvfile.exists();
            userDataAccessObject = new FileUserDataAccessObject("./users.csv", new CommonUserFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        APIDataAccessObject apiDataAccessObject;
        apiDataAccessObject = new APIDataAccessObject();

        SignupView signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel, signupViewModel,
                userDataAccessObject);
        views.add(signupView, signupView.viewName);

        LoginView loginView = LoginUseCaseFactory.create(viewManagerModel, loginViewModel, loggedInViewModel, userDataAccessObject);
        views.add(loginView, loginView.viewName);

        LoggedInView loggedInView = LoggedInViewFactory.create(loggedInViewModel, viewManagerModel, viewTeamViewModel,
                userDataAccessObject, apiDataAccessObject);
        views.add(loggedInView, loggedInView.viewName);

        MenuView menuView = MenuUseCaseFactory.create(menuViewModel, viewManagerModel, signupViewModel, leaderboardViewModel, loginViewModel, userDataAccessObject);
        views.add(menuView, menuView.viewName);

        LeaderboardView leaderboardView = LeaderboardUseCaseFactory.create(menuViewModel, viewManagerModel, leaderboardViewModel, userDataAccessObject);
        views.add(leaderboardView, leaderboardView.viewName);

        ViewTeamView viewTeamView = ViewTeamUseCaseFactory.create(viewTeamViewModel, viewManagerModel);
        views.add(viewTeamView, viewTeamView.viewName);


        viewManagerModel.setActiveView(menuView.viewName);
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setVisible(true);
    }
}