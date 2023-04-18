package com.collab.project.service.impl;

import com.collab.project.exception.ContestRequestException;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.contest.ContestSubmission;
import com.collab.project.model.inputs.ContestSubmissionInput;
import com.collab.project.repositories.ContestSubmissionRepository;
import com.collab.project.service.ContestSubmissionService;
import com.collab.project.util.AuthUtils;
import com.collab.project.util.FileUtils;
import com.collab.project.util.S3Utils;
import com.collab.project.util.Utils;
import io.jsonwebtoken.lang.Strings;
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

    @Autowired
    S3Utils s3Utils;

    @Autowired
    private ContestSubmissionRepository contestSubmissionRepository;

    @Override
    public ContestSubmission addContestSubmission(ContestSubmissionInput contestSubmissionInput) {
        // Search by artist first because one artist can submit only one entry.
        Optional<ContestSubmission> contestSubmission = contestSubmissionRepository.findByArtistId(contestSubmissionInput.getArtistId());
        if (contestSubmission.isPresent()) {
            throw new ContestRequestException(
                    "You have already submitted an entry to this contest");
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
        Optional<ContestSubmission> contestSubmission = contestSubmissionRepository.findById(inputContestSubmission.getId());
        if (contestSubmission.isPresent()) {
            inputContestSubmission.setUpdatedAt(Timestamp.from(Instant.now()));
            contestSubmissionRepository.save(inputContestSubmission);
        }
        return inputContestSubmission;
    }

    @Override
    public List<ContestSubmission> getContestSubmissions(String contestSlug) {
        return contestSubmissionRepository.findByContestSlug(contestSlug);
    }
    @Override
    public List<ContestSubmission> getContestSubmission(String contestSlug, String artistId) {
        ContestSubmission contestSubmission =  contestSubmissionRepository.findByContestSlugAndArtistId(contestSlug, artistId);
        List<ContestSubmission> submissions = new ArrayList<ContestSubmission>();
        if (contestSubmission == null) {
            return submissions;
        } else {
            submissions.add(contestSubmission);
        }
        return submissions;
    }

    @Override
    public String addArtwork(String artistId, MultipartFile filename) throws NoSuchAlgorithmException, IOException {
        String time = String.valueOf(System.currentTimeMillis());
        String artistFileName = Utils.getSHA256(artistId).substring(0, 15);
        String[] filenameSplit = Strings.split(filename.getOriginalFilename(), ".");
        String fileExtension = "jpeg";
        if (filenameSplit != null && filenameSplit.length != 0) {
            fileExtension = filenameSplit[filenameSplit.length - 1];
        }

        FileUtils.createThumbnail(filename, artistFileName + "_thumb." + fileExtension);
        File thumbFile = new File(artistFileName + "_thumb." + fileExtension);

        File file = FileUtils.convertMultiPartFileToFile(filename, artistFileName + "_thumb." + fileExtension);
        String picUrl = s3Utils.uploadFileToS3Bucket("wondor-profile-pictures", file, "", (artistFileName + "_thumb." + fileExtension));
        return picUrl;
    }
}
