package com.collab.project.exception;

import com.collab.project.helpers.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class CollabRequestLimitReachedException extends RuntimeException {

    public CollabRequestLimitReachedException() {
        super(String.format("You can create a maximum of %s collab requests per artist. Please complete existing collaboration by marking them Completed. Contact admin@wondor.art for more details", Constants.ALLOWED_COLLAB_REQUEST_PER_USER));
    }
}
