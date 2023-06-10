package com.collab.project.service.impl;

import com.collab.project.exception.ContestRequestException;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artwork.UploadFile;
import com.collab.project.model.contest.Contest;
import com.collab.project.model.contest.ContestSubmission;
import com.collab.project.model.contest.ContestSubmissionResponse;
import com.collab.project.model.contest.ContestSubmissionVote;
import com.collab.project.model.inputs.ContestSubmissionInput;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ContestSubmissionRepository;
import com.collab.project.repositories.ContestSubmissionVoteRepository;
import com.collab.project.service.ContestSubmissionService;
import com.collab.project.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private ContestSubmissionVoteRepository contestSubmissionVoteRepository;

    int totalVotes = 0;

    @Override
    public ContestSubmission addContestSubmission(ContestSubmissionInput contestSubmissionInput) {
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
            ContestSubmissionResponse response = createContestSubmissionResponse(contestSubmission);
            responses.add(response);
        });

        responses.forEach(response -> {
            response.setVotes((response.getVotes() * 100) / totalVotes);
        });
        return responses;
    }

    private ContestSubmissionResponse createContestSubmissionResponse(ContestSubmission contestSubmission) {
        String artistId = contestSubmission.getArtistId();
        Artist artist = artistRepository.findByArtistId(artistId);
        List<ContestSubmissionVote> votes =
                contestSubmissionVoteRepository.findBySubmissionId(contestSubmission.getId());
        int positive = (int) (votes.stream().filter(ContestSubmissionVote::getVote)).count();
        synchronized(this) {
            totalVotes += positive;
        }
        return new ContestSubmissionResponse(contestSubmission, positive, artist.getFirstName(),
                artist.getLastName());
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
