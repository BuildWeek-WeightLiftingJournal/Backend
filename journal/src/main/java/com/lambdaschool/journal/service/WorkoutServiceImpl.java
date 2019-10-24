package com.lambdaschool.journal.service;

import com.lambdaschool.journal.exceptions.ResourceFoundException;
import com.lambdaschool.journal.exceptions.ResourceNotFoundException;
import com.lambdaschool.journal.models.User;
import com.lambdaschool.journal.models.Workout;
import com.lambdaschool.journal.repository.UserRepository;
import com.lambdaschool.journal.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "workoutService")
public class WorkoutServiceImpl implements WorkoutService
{
    @Autowired
    private WorkoutRepository workoutrepos;

    @Autowired
    private UserRepository userrepos;

    @Override
    public ArrayList<Workout> findAll()
    {
        ArrayList<Workout> list = new ArrayList<>();
        workoutrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Workout save(Workout workout)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userrepos.findByUsername(authentication.getName());
        Workout newWorkout = new Workout();

        newWorkout.setTitle(workout.getTitle());
        newWorkout.setExerciseName(workout.getExerciseName());
        newWorkout.setDay(workout.getDay());
        newWorkout.setWeight(workout.getWeight());
        newWorkout.setSets(workout.getSets());
        newWorkout.setReps(workout.getReps());
        newWorkout.setMuscleGroup(workout.getMuscleGroup());
        List<Workout> currentUserWorkouts = currentUser.getWorkouts();
        currentUserWorkouts.add(newWorkout);
        currentUser.setWorkouts(currentUserWorkouts);
        newWorkout.setUser(currentUser);

        return workoutrepos.save(newWorkout);
    }

    @Transactional
    @Override
    public Workout update(Workout workout, long id)
    {
        Workout currentWorkout = workoutrepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book id " + id + " not found"));

        if (workout.getTitle() != null)
        {
            currentWorkout.setTitle(workout.getTitle());
        }

        if (workout.getDay() != null)
        {
            currentWorkout.setDay(workout.getDay());
        }
        if (workout.getExerciseName() != null)
        {
            currentWorkout.setExerciseName(workout.getExerciseName());
        }
        if (workout.getWeight() != 0)
        {
            currentWorkout.setWeight(workout.getWeight());
        }
        if (workout.getSets() != 0)
        {
            currentWorkout.setSets(workout.getSets());
        }
        if (workout.getReps() != 0)
        {
            currentWorkout.setReps(workout.getReps());
        }
        if (workout.getMuscleGroup() != null)
        {
            currentWorkout.setMuscleGroup(workout.getMuscleGroup());
        }

        return workoutrepos.save(currentWorkout);
    }

    @Override
    public void addWorkoutToUser(long workoutid, long userid)
    {
        workoutrepos.findById(workoutid).orElseThrow(() -> new ResourceNotFoundException("workout id " + workoutid + " not found"));
        userrepos.findById(userid).orElseThrow(() -> new ResourceNotFoundException("user id " + userid + " not found"));

        if (workoutrepos.checkExerciseCombo(workoutid, userid)
                .getCount() <= 0)
        {
            workoutrepos.insertExercise(workoutid, userid);
        } else
        {
            throw new ResourceFoundException("Book and Author Combination Already Exists");
        }
    }


    @Transactional
    @Override
    public void delete(long id)
    {
        workoutrepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workout id " + id + " not found"));

        workoutrepos.deleteById(id);
    }
}
