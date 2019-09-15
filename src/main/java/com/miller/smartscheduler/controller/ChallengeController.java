package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.model.Challenge;
import com.miller.smartscheduler.model.type.ChallengeStatus;
import com.miller.smartscheduler.service.ChallengeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

  private final ChallengeService challengeService;

  @GetMapping
  public List<Challenge> getUserChallenges(@RequestHeader("userId") String userId) {

    return challengeService.findAllByUserId(userId);
  }

  @PostMapping
  public ResponseEntity createChallenge(@RequestBody Challenge challenge, @RequestHeader("userId") String userId) {

    challenge.setUserId(userId);
    challengeService.save(challenge);

    return new ResponseEntity(HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity removeChallenge(@PathVariable("id") String id) {

    challengeService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity removeChallenge(@PathVariable("id") String id, @RequestParam("status") ChallengeStatus challengeStatus) {

    challengeService.updateStatus(id, challengeStatus);

    return new ResponseEntity(HttpStatus.OK);
  }
}
