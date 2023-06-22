package com.collab.project.controller;

import com.collab.project.model.response.SuccessResponse;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.AnalyticsService;
import com.collab.project.service.impl.ScriptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin/v1/script")
public class ScriptController {

    @Autowired
    ScriptServiceImpl scriptService;

    @PostMapping
    @RequestMapping(value = "/run", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> runLogic(@RequestBody Map<String, Object> input) {
        scriptService.updateProfileCompleteStatus();
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
}
