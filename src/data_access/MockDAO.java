package data_access;

import entity.*;
import use_case.leaderboard.LeaderboardDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.make_team.MakeTeamDAI;
import use_case.menu.MenuUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MockDAO implements
        SignupUserDataAccessInterface, LoginUserDataAccessInterface,
        LeaderboardDataAccessInterface, MenuUserDataAccessInterface,
        MakeTeamDAI {

    private final Map<String, Integer> headers = new LinkedHashMap<>();

    private final Map<String, User> accounts = new HashMap<>();

    private User activeUser;

    private UserFactory userFactory;

    public MockDAO() {
        this.userFactory = new CommonUserFactory();

    }

    @Override
    public void save(User user) {
        accounts.put(user.getUserName(), user);
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }



    /**
     * Return whether a user exists with username identifier.
     * @param identifier the username to check.
     * @return whether a user exists with username identifier
     */
    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }


    /**
     * Return the active user.
     * @return the active user
     */
    public User getActiveUser() {
        return activeUser;
    }

    /**
     * Set the active user.
     * @param user the user to set as active
     */
    @Override
    public void setActiveUser(User user) {
        activeUser = user;
    }

    //ToDo: The following method is not implemented properly. It must be completed later, it is a stand-in for now, as we have not yet implemented teams.
    @Override
    public String[] getUserswithTeam() {
        String[] usersWithTeam = new String[accounts.size()];
        int i = 0;
        for (User user : accounts.values()) {
            usersWithTeam[i] = user.getUserName();
            i++;
        }
        return usersWithTeam;
    }

    @Override
    public boolean saveTeam(User user, Team team) {
        user.setTeam(team); // user must already be in accounts
        return true;
    }

    @Override
    public Team getTeamOfUser(String userName) {
        return accounts.get(userName).getUserTeam();
    }

}
