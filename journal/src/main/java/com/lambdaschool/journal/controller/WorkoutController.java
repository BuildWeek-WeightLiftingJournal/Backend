package com.lambdaschool.journal.controller;

import com.lambdaschool.journal.models.ErrorDetail;
import com.lambdaschool.journal.models.User;
import com.lambdaschool.journal.models.Workout;
import com.lambdaschool.journal.service.UserService;
import com.lambdaschool.journal.service.WorkoutService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class WorkoutController
{
    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(WorkoutController.class);

    //GET http://localhost:2019/workout/all
    @ApiOperation(value = "lists all workouts", response = Workout.class, responseContainer = "List")
    @GetMapping(value = "/workout/all",
                produces = {"application/json"})
    public ResponseEntity<?> listAllWorkouts()
    {
        return new ResponseEntity<>(workoutService.findAll(), HttpStatus.OK);
    }


//    @ApiOperation(value = "add an existing book to an existing author using ids")
//    @ApiResponses(value = {@ApiResponse(code = 200,
//                                        message = "Book Added to Author"), @ApiResponse(code = 404,
//                                                                                        message = "Book/Author Not Found",
//                                                                                        response = ErrorDetail.class)})
//    // POST /data/books/{bookid}/authors/{authorid} - assigns a book already in the system (bookid) to an author already in the system (authorid)
//    @PostMapping(value = "/data/books/{bookid}/authors/{authorid}")
//    public ResponseEntity<?> addBookToAuthor(@PathVariable long bookid,
//                                             @PathVariable long authorid)
//    {
//        bookService.addBookToAuthor(bookid, authorid);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    //POST http://localhost:2019/workout/new
    @ApiOperation(value = "adds a new workout to logged in user only")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Workout added to User"),
                          })
    @PostMapping(value = "/workout/new", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addWorkout(@Valid
                                           @RequestBody Workout workout, HttpServletRequest request) throws URISyntaxException
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " added");

        workoutService.save(workout);
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newWorkoutURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(workout.getWorkoutid())
                .toUri();
        responseHeaders.setLocation(newWorkoutURI);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    //PUT http://localhost:2019/workout/update/16
    @ApiOperation(value = "updates a workout")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Workout updated ", response = Workout.class),
                          @ApiResponse(code = 404, message = "Workout not found", response = ErrorDetail.class)})
    @PutMapping(value = "workout/update/{id}",
                        consumes = {"application/json"})
    public ResponseEntity<?> updateWorkout(@Valid
                                           @RequestBody Workout updateWorkout,
                                           @ApiParam(value = "Workout id",
                                                     required = true,
                                                     example = "1")
                                           @PathVariable long id, HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " updated " + id);
        workoutService.update(updateWorkout,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //DELETE http://localhost:2019/workout/delete/15
    @ApiOperation(value = "deletes a workout")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Workout deleted", response = Workout.class),
                            @ApiResponse(code = 400, message = "Workout not found", response = ErrorDetail.class)})
    @DeleteMapping(value = "/workout/delete/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable long id, HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " deleted " + id);
        workoutService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
