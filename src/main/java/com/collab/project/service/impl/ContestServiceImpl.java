package com.collab.project.service.impl;

import com.collab.project.exception.ContestRequestException;
import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.contest.Contest;
import com.collab.project.model.contest.Winner;
import com.collab.project.model.inputs.ContestInput;
import com.collab.project.repositories.ArtCategoryRepository;
import com.collab.project.repositories.ArtistCategoryRepository;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ContestRepository;
import com.collab.project.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;
import static java.util.stream.Collectors.toList;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtCategoryRepository artCategoryRepository;

    @Autowired
    private ArtistCategoryRepository artistCategoryRepository;

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
        new_contest.setCategories(String.join(",", contestInput.getCategories()));

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
        List<Contest> contests = contestRepository.findAll();
        contests.stream().parallel().forEach(contest -> {
            String winnerId = contest.getWinnerArtistId();
            if (winnerId != null) {
                Artist artist = artistRepository.findByArtistId(winnerId);
                List<Long> categories = artistCategoryRepository.findByArtistId(winnerId).
                        stream().map(ArtistCategory::getArtId).collect(toList());

                List<ArtCategory> arts = artCategoryRepository.findByIdIn(categories);

                contest.setWinner(new Winner(artist.getArtistId(), artist.getFirstName() + " " + artist.getLastName(), artist.getSlug(), artist.getProfilePicUrl(), arts));
            }

        });
        return contests;
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
