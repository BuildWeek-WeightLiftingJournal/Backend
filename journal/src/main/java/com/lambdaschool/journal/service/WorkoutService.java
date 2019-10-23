package com.lambdaschool.journal.service;

import com.lambdaschool.journal.models.User;
import com.lambdaschool.journal.models.Workout;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public interface WorkoutService
{
    ArrayList<Workout> findAll();

    Workout save(Workout workout, User user);

    Workout update(Workout workout, long id);

    void addWorkoutToUser(long workoutid, long userid);

    void delete(long id);
}
