package com.questo.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.questo.android.common.Constants;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Companionship;
import com.questo.android.model.Place;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentMembership;
import com.questo.android.model.TournamentRequest;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.TournamentTaskDone;
import com.questo.android.model.Trophy;
import com.questo.android.model.Trophy.Type;
import com.questo.android.model.TrophyForUser;
import com.questo.android.model.User;

public class ModelManager {

    private App app;
    private static final String TAG = "ModelManager";

    public ModelManager(Context ctx) {
        this.app = (App) ctx;
    }

    private DBHelper db() {
        return app.getDB();
    }

    private void handleException(SQLException e) {
        Log.e(TAG, "Problem with the database!");
        e.printStackTrace();
    }

    public <T> T create(T o, Class<T> clazz) {
        try {
            int result = db().getCachedDao(clazz).create(o);
            if (result == 1)
                return o;
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T delete(T o, Class<T> clazz) {
        try {
            int result = db().getCachedDao(clazz).delete(o);
            if (result == 1)
                return o;
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T update(T o, Class<T> clazz) {
        try {
            int result = db().getCachedDao(clazz).update(o);
            if (result == 1)
                return o;
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T refresh(T o, Class<T> clazz) {
        try {
            int result = db().getCachedDao(clazz).refresh(o);
            if (result == 1)
                return o;
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    public User getUserByEmail(String email) {
        try {
            QueryBuilder<User, Integer> userQBuilder = queryBuilder(User.class);
            userQBuilder.where().eq(User.EMAIL, email);
            PreparedQuery<User> preparedQuery = userQBuilder.prepare();
            List<User> users = db().getCachedDao(User.class).query(preparedQuery);
            if (users.size() > 0)
                return users.get(0);
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T getGenericObjectById(Integer id, Class<T> clazz) {
        try {
            QueryBuilder<T, Integer> genericQBuilder = queryBuilder(clazz);
            genericQBuilder.where().eq("id", id);
            PreparedQuery<T> preparedQuery = genericQBuilder.prepare();
            List<T> objects = db().getCachedDao(clazz).query(preparedQuery);
            if (objects.size() > 0) {
                return objects.get(0);
            }
        } catch (SQLException e) {
            handleException(e);
        }

        return null;
    }

    public List<User> getCompanionsForUser(User user) {
        try {

            QueryBuilder<Companionship, Integer> initiator = queryBuilder(Companionship.class);
            initiator.selectColumns(Companionship.CONFIRMER_UUID).where().eq(Companionship.CONFIRMED, true).and().eq(Companionship.INITIATOR_UUID, user.getUuid());
            
            QueryBuilder<Companionship, Integer> confirmer = queryBuilder(Companionship.class);
            initiator.selectColumns(Companionship.INITIATOR_UUID).where().eq(Companionship.CONFIRMED, true).and().eq(Companionship.CONFIRMER_UUID, user.getUuid());

            QueryBuilder<User, Integer> companions = queryBuilder(User.class);
            companions.where().in(User.UUID, initiator).or().in(User.UUID, confirmer).prepare();

            return db().getCachedDao(User.class).query(companions.prepare());
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<User>();
    }

    public List<Place> getPlacesNearby(Double latitude, Double longitude) {
        double lowLatitude = latitude - Constants.MAP_PLACES_NEARBY;
        double highLatitude = latitude + Constants.MAP_PLACES_NEARBY;
        double lowLongitude = longitude - Constants.MAP_PLACES_NEARBY;
        double highLongitude = longitude + Constants.MAP_PLACES_NEARBY;

        try {
            QueryBuilder<Place, Integer> placeQBuilder = db().getCachedDao(Place.class).queryBuilder();
            placeQBuilder.where().between("latitude", lowLatitude, highLatitude).and()
                    .between("longitude", lowLongitude, highLongitude);
            PreparedQuery<Place> preparedQuery = placeQBuilder.prepare();
            List<Place> places = db().getCachedDao(Place.class).query(preparedQuery);
            return places;
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<Place>();
    }

    public <T> T getGenericObjectByUuid(String uuid, Class<T> clazz) {
        try {
            QueryBuilder<T, Integer> genericQBuilder = queryBuilder(clazz);
            genericQBuilder.where().eq("UUID", uuid);
            PreparedQuery<T> preparedQuery = genericQBuilder.prepare();
            List<T> objects = db().getCachedDao(clazz).query(preparedQuery);
            if (objects.size() > 0) {
                return objects.get(0);
            }
        } catch (SQLException e) {
            handleException(e);
        }

        return null;
    }

    public List<Trophy> getTrophyForUser(User user) {
        try {

            QueryBuilder<TrophyForUser, Integer> tfu = queryBuilder(TrophyForUser.class);
            tfu.selectColumns(TrophyForUser.TROPHY_UUID).where().eq(TrophyForUser.USER, user).prepare();

            QueryBuilder<Trophy, Integer> trophy = queryBuilder(Trophy.class);
            trophy.where().in(Trophy.UUID, tfu);

            return db().getCachedDao(Trophy.class).query(trophy.prepare());
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<Trophy>();
    }

    public List<Trophy> getTrophyForType(User user, Type type) {
        try {

            QueryBuilder<TrophyForUser, Integer> tfu = queryBuilder(TrophyForUser.class);
            tfu.selectColumns(TrophyForUser.TROPHY_UUID).where().eq(TrophyForUser.USER, user).prepare();

            QueryBuilder<Trophy, Integer> trophies = queryBuilder(Trophy.class);
            trophies.where().in(Trophy.UUID, tfu).and().eq(Trophy.TYPE, type);

            return db().getCachedDao(Trophy.class).query(trophies.prepare());
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<Trophy>();
    }

    public List<Tournament> getTournamentsForUser(User user, boolean initializeObjects) {
        try {
            QueryBuilder<TournamentMembership, Integer> tournamentUUIDs = queryBuilder(TournamentMembership.class);
            tournamentUUIDs.selectColumns(TournamentMembership.TOURNAMENT_UUID);
            tournamentUUIDs.distinct();
            tournamentUUIDs.where().eq(TournamentMembership.USER_UUID, user.getUuid()).prepare();

            QueryBuilder<Tournament, Integer> tournaments = queryBuilder(Tournament.class);
            tournaments.where().in(Tournament.UUID, tournamentUUIDs);

            return db().getCachedDao(Tournament.class).query(tournaments.prepare());
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<Tournament>();
    }

    public List<Place> getPlacesForTournament(Tournament tournament) {
        try {
            QueryBuilder<TournamentTask, Integer> placeUUIDs = queryBuilder(TournamentTask.class);
            placeUUIDs.selectColumns(TournamentTask.PLACE_UUID);
            placeUUIDs.where().eq(TournamentTask.TOURNAMENT, tournament);

            QueryBuilder<Place, Integer> places = queryBuilder(Place.class);
            places.where().in(Place.UUID, placeUUIDs);

            return db().getCachedDao(Place.class).query(places.prepare());
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<Place>();
    }

    public List<User> getContestantsForTournament(Tournament tournament) {
        try {
            QueryBuilder<TournamentMembership, Integer> userUUIDs = queryBuilder(TournamentMembership.class);
            userUUIDs.selectColumns(TournamentMembership.USER_UUID);
            userUUIDs.where().eq(TournamentMembership.TOURNAMENT_UUID, tournament.getUuid());

            QueryBuilder<User, Integer> users = queryBuilder(User.class);
            users.where().in(User.UUID, userUUIDs);

            return db().getCachedDao(User.class).query(users.prepare());
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<User>();
    }

    public boolean cancelTournamentMembershipForLoggedinUser(Tournament tournament) {
        try {
            User user = app.getLoggedinUser();
            QueryBuilder<TournamentMembership, Integer> membership = queryBuilder(TournamentMembership.class);
            membership.where().eq(TournamentMembership.USER_UUID, user.getUuid()).and()
                    .eq(TournamentMembership.TOURNAMENT_UUID, tournament.getUuid());
            List<TournamentMembership> membershipList = db().getCachedDao(TournamentMembership.class).query(
                    membership.prepare());
            if (membershipList.size() > 0) {
                delete(membershipList.get(0), TournamentMembership.class);
                return true;
            } else
                return false;
        } catch (SQLException e) {
            handleException(e);
        }
        return false;
    }

    /**
     * Returns a two-slot array of lists: [0] = tournament tasks already done
     * [1] = remaining tournament tasks (not already done)
     * 
     * @param user
     * @param tournament
     * @return
     */
    public List<TournamentTask>[] getTournamentTasksDoneAndRemaining(User user, Tournament tournament) {
        long time = System.currentTimeMillis();
        try {
            if (user.getUuid() == null)
                refresh(user, User.class);
            if (tournament.getUuid() == null)
                refresh(tournament, Tournament.class);
            List<TournamentTask>[] tasksResult = new List[2];
            // most-inner query: getting tournament-tasks for a spec.
            // tournament:
            QueryBuilder<TournamentTask, Integer> tournamentTasks = queryBuilder(TournamentTask.class);
            tournamentTasks.selectColumns(TournamentTask.UUID);
            tournamentTasks.where().eq(TournamentTask.TOURNAMENT, tournament);
            // middle query: getting tournament-task-dones for the prev. gotten
            // tournament-tasks:
            QueryBuilder<TournamentTaskDone, Integer> tournamentTaskDones = queryBuilder(TournamentTaskDone.class);
            tournamentTaskDones.selectColumns(TournamentTaskDone.TOURNAMENT_TASK_UUID);
            tournamentTaskDones.where().eq(TournamentTaskDone.USER, user).and()
                    .in(TournamentTaskDone.TOURNAMENT_TASK_UUID, tournamentTasks);
            // outmoust query: getting tournament-tasks for the prev. gotten
            // tournament-task-dones:
            QueryBuilder<TournamentTask, Integer> tournamentTaskDoneOuterTasks = queryBuilder(TournamentTask.class);
            tournamentTaskDoneOuterTasks.where().in(TournamentTask.UUID, tournamentTaskDones);
            tasksResult[0] = db().getCachedDao(TournamentTask.class).query(tournamentTaskDoneOuterTasks.prepare());

            tasksResult[1] = new ArrayList<TournamentTask>();
            QueryBuilder<TournamentTask, Integer> tournamentTasksAll = queryBuilder(TournamentTask.class);
            tournamentTasksAll.where().eq(TournamentTask.TOURNAMENT, tournament);
            for (TournamentTask task : db().getCachedDao(TournamentTask.class).query(tournamentTasksAll.prepare())) {
                if (!tasksResult[0].contains(task))
                    tasksResult[1].add(task);
            }
            long timeTaken = System.currentTimeMillis() - time;
            System.out.println("This query took: " + timeTaken + " ms.");
            return tasksResult;
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList[] { new ArrayList<TournamentTaskDone>(), new ArrayList<TournamentTaskDone>() };
    }

    public List<TournamentRequest> getTournamentRequestsForUser(User user) {
        try {
            QueryBuilder<TournamentRequest, Integer> tournamentRequests = queryBuilder(TournamentRequest.class);
            tournamentRequests.where().eq(TournamentRequest.USER, user);
            return db().getCachedDao(TournamentRequest.class).query(tournamentRequests.prepare());
        } catch (SQLException e) {
            handleException(e);
        }
        return new ArrayList<TournamentRequest>();
    }

    public void addTournamentMembership(Tournament tournament, User user) {
        refresh(tournament, Tournament.class);
        refresh(user, User.class);
        TournamentMembership membership = new TournamentMembership(UUIDgen.getUUID(), user.getUuid(),
                tournament.getUuid(), new Date());
        create(membership, TournamentMembership.class);
    }

    public boolean acceptTournamentRequest(final TournamentRequest request) {
        try {
            db().executeInTransaction(new Callable<Void>() {
                public Void call() throws Exception {
                    addTournamentMembership(request.getTournament(), request.getUser());
                    delete(request, TournamentRequest.class);
                    return null;
                }
            });
            return true;
        } catch (SQLException e) {
            handleException(e);
        }
        return false;
    }

    private <T> QueryBuilder<T, Integer> queryBuilder(Class<T> queryClass) throws SQLException {
        return db().getCachedDao(queryClass).queryBuilder();
    }

}
