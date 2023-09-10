package com.collab.project.service.impl;

import com.collab.project.exception.ContestRequestException;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artwork.UploadFile;
import com.collab.project.model.contest.*;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.ContestSubmissionInput;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ContestRepository;
import com.collab.project.repositories.ContestSubmissionRepository;
import com.collab.project.repositories.ContestSubmissionVoteRepository;
import com.collab.project.service.ContestSubmissionService;
import com.collab.project.service.RewardsService;
import com.collab.project.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class ContestSubmissionServiceImpl implements ContestSubmissionService {

    String bucketName = "contest-submission";

    @Autowired
    S3Utils s3Utils;

    @Autowired
    private ContestSubmissionRepository contestSubmissionRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestSubmissionVoteRepository contestSubmissionVoteRepository;

    @Autowired
    private RewardsService rewardsService;

    @Override
    public ContestSubmission addContestSubmission(ContestSubmissionInput contestSubmissionInput) throws JsonProcessingException {
        // Search by artist first because one artist can submit only one entry.
        Optional<ContestSubmission> contestSubmission =
                contestSubmissionRepository.findByArtistId(contestSubmissionInput.getArtistId());
        if (contestSubmission.isPresent()) {
            throw new ContestRequestException("You have already submitted an entry to this contest");
        }
        ContestSubmission newContestSubmission = new ContestSubmission();
        newContestSubmission.setId(FALLBACK_ID);
        newContestSubmission.setArtistId(AuthUtils.getArtistId());
        newContestSubmission.setContestSlug(contestSubmissionInput.getContestSlug());
        newContestSubmission.setArtworkUrl(contestSubmissionInput.getArtworkUrl());
        newContestSubmission.setDescription(contestSubmissionInput.getDescription());
        contestSubmissionRepository.save(newContestSubmission);

        Artist artist = artistRepository.getOne(AuthUtils.getArtistId());
        Map<String, Object> details = new HashMap();
        details.put("contest_slug", contestSubmissionInput.getContestSlug());
        rewardsService.addPointsToUser(artist.getSlug(), Enums.RewardTypes.MONTHLY_CONTEST.toString(), details);
        return newContestSubmission;
    }

    @Override
    public ContestSubmission updateContestSubmission(ContestSubmission inputContestSubmission) {
        Optional<ContestSubmission> contestSubmission =
                contestSubmissionRepository.findById(inputContestSubmission.getId());
        if (contestSubmission.isPresent()) {
            inputContestSubmission.setUpdatedAt(Timestamp.from(Instant.now()));
            contestSubmissionRepository.save(inputContestSubmission);
        }
        return inputContestSubmission;
    }

    @Override
    public List<ContestSubmissionResponse> getContestSubmissions(String contestSlug) {
        List<ContestSubmission> submissions = contestSubmissionRepository.findByContestSlug(contestSlug);
        List<ContestSubmissionResponse> responses = new ArrayList<>();

        submissions.stream().parallel().forEach(contestSubmission -> {
            Artist artist = artistRepository.findByArtistId(contestSubmission.getArtistId());
            responses.add(new ContestSubmissionResponse(contestSubmission, artist.getFirstName(),
                    artist.getLastName(), artist.getSlug()));
        });
        return responses;
    }

    @Override
    public ContestSubmissionResultResponse getContestSubmissionsResults(String contestSlug) {
        List<ContestSubmission> submissions = contestSubmissionRepository.findByContestSlug(contestSlug);
        List<ContestSubmissionStats> responses = new ArrayList<>();

        submissions.stream().parallel().forEach(contestSubmission -> {
            ContestSubmissionStats stats = createContestSubmissionResponse(contestSubmission);
            responses.add(stats);
        });

        Collections.sort(responses, (r1, r2) -> Integer.compare(r2.getVotes(), r1.getVotes()));
        int totalVotes = responses.stream().mapToInt(ContestSubmissionStats::getVotes).sum();

        ContestSubmissionResultResponse resultResponse = new ContestSubmissionResultResponse();
        final int[] winners = {2};

        responses.forEach(response -> {
            if (winners[0] > 0) {
                int votePercent = (response.getVotes() * 100) / totalVotes;
                response.setVotes(votePercent);
                resultResponse.getWinners().add(response);
                winners[0]--;
            } else {
                resultResponse.getOtherSubmissions().add(new ContestSubmissionResponse(response.getSubmission(),
                        response.getFirstName(), response.getLastName(), response.getSlug()));
            }
        });

        return resultResponse;
    }

    private ContestSubmissionStats createContestSubmissionResponse(ContestSubmission contestSubmission) {
        String artistId = contestSubmission.getArtistId();
        Artist artist = artistRepository.findByArtistId(artistId);
        List<ContestSubmissionVote> votes =
                contestSubmissionVoteRepository.findBySubmissionId(contestSubmission.getId());
        int positive = (int) (votes.stream().filter(ContestSubmissionVote::getVote)).count();
        return new ContestSubmissionStats(contestSubmission, artist.getFirstName(),
                artist.getLastName(), artist.getSlug(), positive);
    }

    @Override
    public List<ContestSubmission> getContestSubmission(String contestSlug, String artistId) {
        ContestSubmission contestSubmission = contestSubmissionRepository.findByContestSlugAndArtistId(contestSlug,
                artistId);
        List<ContestSubmission> submissions = new ArrayList<ContestSubmission>();
        if (contestSubmission == null) {
            return submissions;
        } else {
            submissions.add(contestSubmission);
        }
        return submissions;
    }

    @Override
    public ContestSubmission addArtwork(String artistId, MultipartFile fileToUpload, String fileType,
                              String description, String contestId) throws NoSuchAlgorithmException, IOException {
        FileUpload fileUploadBuilder =
                FileUpload.builder()
                        .fileToUpload(fileToUpload)
                        .artistId(artistId)
                        .s3BucketName(bucketName)
                        .fileType(fileType)
                        .s3Utils(s3Utils)
                        .s3Path(contestId).build();

        UploadFile uploadedFile = fileUploadBuilder.checkFileTypeAndGetUploadURL();

        ContestSubmission submission = new ContestSubmission(FALLBACK_ID, contestId, artistId,
                uploadedFile.getOriginalURL(), uploadedFile.getThumbnailURL(), description,
                Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));


        contestSubmissionRepository.save(submission);

        return submission;
    }
}
