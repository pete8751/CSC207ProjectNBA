package app;

import data_access.APIinterface;
import entity.CommonTeamFactory;
import entity.TeamFactory;
import use_case.make_team.MakeTeamDAI;
import use_case.make_team.create_team.CreateTeamOutputBoundary;
import use_case.make_team.create_team.CreateTeamPresenter;
import use_case.make_team.create_team.CreateTeamView;
import use_case.make_team.create_team.CreateTeamViewModel;
import use_case.make_team.create_team.addPlayer.AddPlayerController;
import use_case.make_team.create_team.addPlayer.AddPlayerInputBoundary;
import use_case.make_team.create_team.addPlayer.AddPlayerUCI;
import use_case.make_team.create_team.search_player.SearchPlayerController;
import use_case.make_team.create_team.search_player.SearchPlayerInputBoundary;
import use_case.make_team.create_team.search_player.SearchPlayerUCI;
import use_case.make_team.save_team.SaveTeamInputBoundary;
import use_case.make_team.save_team.SaveTeamOutputBoundary;
import use_case.make_team.save_team.SaveTeamUCI;
import use_case.make_team.save_team.interface_adapter.SaveTeamController;
import use_case.make_team.save_team.interface_adapter.SaveTeamPresenter;
import use_case.view_team.ViewTeamInputBoundary;
import use_case.view_team.ViewTeamInteractor;
import use_case.view_team.ViewTeamOutputBoundary;
import use_case.view_team.ViewTeamUserDataAccessInterface;
import use_case.view_team.interface_adapter.ViewTeamController;
import use_case.view_team.interface_adapter.ViewTeamPresenter;
import use_case.view_team.interface_adapter.ViewTeamViewModel;
import view.LoggedInViewModel;
import view.ViewManagerModel;

public class MakeTeamUseCaseFactory {
    public static CreateTeamView createCreateTeamView(
            CreateTeamViewModel createTeamViewModel,
            APIinterface apiDAO,
            MakeTeamDAI databaseDAO,
            ViewManagerModel viewManagerModel
    ) {

        SearchPlayerController searchPlayerController = createSearchPlayerController(createTeamViewModel, apiDAO);
        AddPlayerController addPlayerController = createAddPlayerController(createTeamViewModel);
        SaveTeamController saveTeamController = createSaveTeamController(databaseDAO, viewManagerModel);

        return new CreateTeamView(
                searchPlayerController,
                addPlayerController,
                createTeamViewModel,
                saveTeamController
                );

    }

    private static SearchPlayerController createSearchPlayerController(
            CreateTeamViewModel createTeamViewModel,
            APIinterface apiDAO
    ) {
        CreateTeamOutputBoundary createTeamPresenter = new CreateTeamPresenter(createTeamViewModel);
        SearchPlayerInputBoundary searchPlayerInputBoundary = new SearchPlayerUCI(apiDAO,createTeamPresenter);

        return new SearchPlayerController(searchPlayerInputBoundary);
    }

    private static AddPlayerController createAddPlayerController(
            CreateTeamViewModel createTeamViewModel
    ) {
        CreateTeamOutputBoundary createTeamPresenter = new CreateTeamPresenter(createTeamViewModel);
        AddPlayerInputBoundary addPlayerInputBoundary = new AddPlayerUCI(createTeamPresenter);

        return new AddPlayerController(addPlayerInputBoundary);
    }

    private static SaveTeamController createSaveTeamController (
            MakeTeamDAI dao,
            ViewManagerModel viewManagerModel

    ) {
        SaveTeamOutputBoundary saveTeamPresenter = new SaveTeamPresenter(viewManagerModel);
        TeamFactory teamFactory = new CommonTeamFactory();
        SaveTeamInputBoundary saveTeamUCI = new SaveTeamUCI(dao, saveTeamPresenter, teamFactory);

        return new SaveTeamController(saveTeamUCI);
    }
}
