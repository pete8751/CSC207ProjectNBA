package entity.dummys;

import entity.Team;
import entity.entity_helpers.TeamComparator;
import entity.entity_helpers.TeamEvaluator;

public class TeamComparatorDummy extends TeamComparator {

    public TeamComparatorDummy(TeamEvaluator teamEvaluator) {
        super(teamEvaluator);
    }

    @Override
    public Team compareTeams(Team t1, Team t2) {
        return null;
    }
}
