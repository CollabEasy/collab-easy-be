package com.collab.project.service;

import com.collab.project.model.art.ArtInfo;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.contest.ContestSubmission;
import com.collab.project.model.contest.ContestSubmissionResponse;
import com.collab.project.model.inputs.ContestSubmissionInput;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface ContestSubmissionService {

    public ContestSubmission addContestSubmission(ContestSubmissionInput contestSubmissionInput);

    public ContestSubmission updateContestSubmission(ContestSubmission contestSubmission);

    public List<ContestSubmissionResponse> getContestSubmissions(String contestSlug);

    public  List<ContestSubmission> getContestSubmission(String contestSlug, String artistId);

    public ContestSubmission addArtwork(String artistId, MultipartFile filename, String fileType,
                             String description, String contestSlug) throws NoSuchAlgorithmException, IOException;
}
