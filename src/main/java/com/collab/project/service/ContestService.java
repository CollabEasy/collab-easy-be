package com.collab.project.service;

import com.collab.project.model.contest.Contest;
import com.collab.project.model.inputs.ContestInput;
import java.util.List;

public interface ContestService {

    public Contest addContest(ContestInput contestInput);

    public Contest updateContest(Contest contest);

    public List<Contest> getAllContests();

    public Contest getContestBySlug(String contestSlug);
}