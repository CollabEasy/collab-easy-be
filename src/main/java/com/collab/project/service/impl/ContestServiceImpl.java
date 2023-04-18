package com.collab.project.service.impl;

import com.collab.project.exception.ContestRequestException;
import com.collab.project.model.contest.Contest;
import com.collab.project.model.inputs.ContestInput;
import com.collab.project.repositories.ContestRepository;
import com.collab.project.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Override
    public Contest addContest(ContestInput contestInput) {
        Optional<Contest> contest = contestRepository.findByContestSlug(contestInput.getContestSlug());
        if(contest.isPresent()) {
            throw new ContestRequestException(
                    "Contest already exists with id: "+ contestInput.getContestSlug());
        }
        Contest new_contest = new Contest();
        new_contest.setId(FALLBACK_ID);
        new_contest.setContestSlug(contestInput.getContestSlug());
        new_contest.setTitle(contestInput.getTitle());
        new_contest.setDescription(contestInput.getDescription());
        new_contest.setStartDate(Timestamp.valueOf(contestInput.getStartDate()));
        new_contest.setEndDate(Timestamp.valueOf(contestInput.getEndDate()));

        contestRepository.save(new_contest);
        return new_contest;
    }

    @Override
    public Contest updateContest(Contest inputContest) {
        Optional<Contest> contest = contestRepository.findByContestSlug(inputContest.getContestSlug());
        if (contest.isPresent()) {
            inputContest.setUpdatedAt(Timestamp.from(Instant.now()));
            contestRepository.save(inputContest);
        }
        return inputContest;
    }

    @Override
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    @Override
    public Contest getContestBySlug(String contestSlug) {
        Optional<Contest> contest = contestRepository.findByContestSlug(contestSlug);
        if (contest.isPresent()) {
            return contest.get();
        } else {
            throw new ContestRequestException(
                    "Contest doesn't exists for slug: " + contestSlug);
        }
    }
}
