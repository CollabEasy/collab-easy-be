package com.collab.project.service.impl;

import com.amazonaws.services.mq.model.BadRequestException;
import com.collab.project.email.EmailService;
import com.collab.project.helpers.Constants;
import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.collab.CollabRequestResponse;
import com.collab.project.model.contest.ContestSubmissionResponse;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.ProposalAnswerInput;
import com.collab.project.model.inputs.ProposalInput;
import com.collab.project.model.inputs.ProposalQuestionInput;
import com.collab.project.model.proposal.*;
import com.collab.project.repositories.*;
import com.collab.project.service.CollabService;
import com.collab.project.service.ProposalService;
import com.collab.project.util.Utils;
import com.collab.project.util.emailTemplates.CompleteProfileEmail;
import com.collab.project.util.emailTemplates.InterestReject;
import com.collab.project.util.emailTemplates.InterestShownEmail;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProposalServiceImpl implements ProposalService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    ProposalRepository proposalRepository;

    @Autowired
    CollabService collabService;

    @Autowired
    ProposalQuestionsRepository proposalQuestionsRepository;

    @Autowired
    ProposalInterestRepository proposalInterestRepository;

    @Autowired
    CategoryToProposalRepository categoryToProposalRepository;

    @Autowired
    ArtCategoryRepository artCategoryRepository;

    private Map<Long, String> getProposalCategoriesByIds(List<Long> categoryIds) {
        List<ArtCategory> finalList = artCategoryRepository.findByIdIn(categoryIds);
        Map<Long, String> finalCategoryMap = new HashMap<>();
        for (ArtCategory category : finalList) {
            finalCategoryMap.put(category.getId(), category.getArtName());
        }
        return finalCategoryMap;
    }

    private Map<Long, String> getProposalCategoriesByObject(List<CategoryToProposal> byProposalId) {
        if (byProposalId == null) {
            return new HashMap<>();
        }
        return getProposalCategoriesByIds(byProposalId.stream().map(CategoryToProposal::getCategoryId).collect(Collectors.toList()));
    }

    private Map<Long, String> updateCategories(String proposalId, List<Long> categoryIds) {
        Set<Long> newCategories = new HashSet<>(categoryIds);
        List<CategoryToProposal> oldCategories = categoryToProposalRepository.findByProposalId(proposalId);
        if (oldCategories == null) {
            oldCategories = new ArrayList<>();
        }

        for (CategoryToProposal oldCategory : oldCategories) {
            // Delete the combination if it doesn't exist in the new categories
            if (!newCategories.contains(oldCategory.getCategoryId())) {
                categoryToProposalRepository.delete(oldCategory);
            } else {
                // If exists, remove from newCategories set, as we do not want to perform write to DB again
                newCategories.remove(oldCategory.getCategoryId());
            }
        }

        for (Long category: newCategories) {
            categoryToProposalRepository.save(new CategoryToProposal(Constants.FALLBACK_ID, category, proposalId));
        }
        return this.getProposalCategoriesByIds(categoryIds);
    }

    private List<ProposalQuestion> getProposalQuestions(String proposalId) {
        return proposalQuestionsRepository.findByProposalId(proposalId);
    }

    private List<ProposalInterest> getProposalInterests(String proposalId) {
        return proposalInterestRepository.findByProposalId(proposalId);
    }

    private void validateProposal(ProposalInput input) {
        try {
            Enums.CollabTypes collabType = Enums.CollabTypes.valueOf(input.getCollabType());
        } catch (Exception e) {
            throw new BadRequestException("Collab type is not valid");
        }

        try {
            Enums.ProposalStatus proposalStatus = Enums.ProposalStatus.valueOf(input.getProposalStatus());
        } catch (Exception e) {
            throw new BadRequestException("Proposal status is not valid");
        }
        if (input.getCategoryIds().isEmpty()) {
            throw new BadRequestException("Category IDs cannot be empty");
        }

    }
    private Proposal updateProposal(Proposal proposal, ProposalInput proposalInput) {
        validateProposal(proposalInput);
        proposal.setTitle(proposalInput.getProposalTitle());
        proposal.setDescription(proposalInput.getProposalDescription());
        proposal.setCategories(updateCategories(proposal.getProposalId(), proposalInput.getCategoryIds()));
        proposal.setUpdatedAt(Timestamp.from(Instant.now()));
        proposal.setCollabType(Enums.CollabTypes.valueOf(proposalInput.getCollabType()));
        proposal.setProposalStatus(Enums.ProposalStatus.valueOf(proposalInput.getProposalStatus()));
        return proposalRepository.save(proposal);
    }
    @Override
    @SneakyThrows
    public ProposalResponse createProposal(String artistId, ProposalInput proposalInput) {
        Proposal proposal = new Proposal();
        proposal.setId(Constants.FALLBACK_ID);
        proposal.setProposalId(Utils.getSHA256(UUID.randomUUID().toString()).substring(0, 6));
        proposal.setCreatedBy(artistId);
        proposal.setCreatedAt(Timestamp.from(Instant.now()));
        Proposal updatedProposal  = updateProposal(proposal, proposalInput);

        Artist artist = artistRepository.findByArtistId(proposal.getCreatedBy());

        ProposalResponse response = new ProposalResponse(proposal, artist.getFirstName(),
                artist.getLastName(), artist.getSlug(), artist.getProfilePicUrl());
        return response;
    }

    @Override
    public Proposal updateProposal(String artistId, String proposalId, ProposalInput proposalInput) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        if (!artistId.equalsIgnoreCase(proposal.getCreatedBy())) {
            throw new IllegalStateException("You cannot edit a proposal created by someone else.");
        }
        return updateProposal(proposal, proposalInput);
    }

    @Override
    public ArtistProposals getArtistProposals(String artistSlug) {
        List<Artist> artistList = artistRepository.findBySlug(artistSlug);
        if (artistList.isEmpty()) {
            return new ArtistProposals();
        }
        Artist artist = artistList.get(0);
        String artistId = artist.getArtistId();
        List<Proposal> proposals = proposalRepository.findByCreatedBy(artistId);
        if (proposals == null) {
            return new ArtistProposals();
        }
        ArtistProposals responses = new ArtistProposals();
        List<ProposalResponse> createdProposals = new ArrayList<>();
        for (Proposal proposal : proposals) {
            proposal.setCategories(getProposalCategoriesByObject(categoryToProposalRepository.findByProposalId(proposal.getProposalId())));
            ProposalResponse response = new ProposalResponse();
            response.setProposal(proposal);
            response.setCreatorLastName(artist.getLastName());
            response.setCreatorFirstName(artist.getFirstName());
            response.setCreatorProfilePicUrl(artist.getProfilePicUrl());
            response.setCreatorSlug(artistSlug);
            createdProposals.add(response);
        }
        responses.setCreated(createdProposals);

        List<ProposalResponse> interestedProposals = new ArrayList<>();
        List<ProposalInterest> interested = proposalInterestRepository.findByArtistId(artistId);
        for (ProposalInterest proposalInterest : interested) {
            Proposal proposal = proposalRepository.findByProposalId(proposalInterest.getProposalId());
            ProposalResponse response = new ProposalResponse();
            response.setProposal(proposal);
            interestedProposals.add(response);
            // Think about how to populate logic for populating
            // proposal creator detail.
        }
        responses.setInterested(interestedProposals);
        return responses;
    }

    @Override
    public ProposalResponse getProposal(String proposalId) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        if (proposal == null) {
            return null;
        }
        proposal.setCategories(getProposalCategoriesByObject(categoryToProposalRepository.findByProposalId(proposal.getProposalId())));

        Artist artist = artistRepository.findByArtistId(proposal.getCreatedBy());
        ProposalResponse response =  new ProposalResponse(proposal, artist.getFirstName(),
                    artist.getLastName(), artist.getSlug(), artist.getProfilePicUrl());

        return response;
    }

    @Override
    public List<ProposalResponse> getProposalByCategories(List<Long> categoryIds) {
        List<Proposal> proposals = new ArrayList<>();
        if (categoryIds.isEmpty()) {
            proposals = proposalRepository.findAll();
        } else {
            List<CategoryToProposal> categoryToProposals = categoryToProposalRepository.findByCategoryIdIn(categoryIds);
            if (categoryToProposals == null) {
                return new ArrayList<>();
            }
            List<String> proposalIds = categoryToProposals.stream().map(CategoryToProposal::getProposalId).collect(Collectors.toList());
            proposals = proposalRepository.findByProposalIdIn(proposalIds);
        }
        if (proposals == null) {
            return new ArrayList<>();
        }

        proposals.stream().parallel().forEach(proposal -> {
            proposal.setCategories(getProposalCategoriesByObject(categoryToProposalRepository.findByProposalId(proposal.getProposalId())));
        });

        List<ProposalResponse> responses = new ArrayList<>();
        for (Proposal proposal : proposals) {
            Artist artist = artistRepository.findByArtistId(proposal.getCreatedBy());

            responses.add(new ProposalResponse(proposal, artist.getFirstName(),
                    artist.getLastName(), artist.getSlug(), artist.getProfilePicUrl()));
        }
        return responses;
    }

    @Override
    @SneakyThrows
    public List<ProposalQuestion> askQuestion(String artistId, String proposalId, ProposalQuestionInput questionInput) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        if (proposal.getCreatedBy().equals(artistId)) {
            throw  new IllegalStateException("You cannot ask questions in your own proposals.");
        }

        ProposalQuestion proposalQuestion = new ProposalQuestion();
        proposalQuestion.setId(Constants.FALLBACK_ID);
        proposalQuestion.setProposalId(proposal.getProposalId());
        proposalQuestion.setQuestionId(Utils.getSHA256(UUID.randomUUID().toString()).substring(0, 5));
        proposalQuestion.setQuestion(questionInput.getQuestion());
        proposalQuestion.setAskedBy(artistId);
        proposalQuestion.setCreatedAt(Timestamp.from(Instant.now()));
        proposalQuestion.setUpdatedAt(Timestamp.from(Instant.now()));

        proposalQuestionsRepository.save(proposalQuestion);
        List<ProposalQuestion> questions = proposalQuestionsRepository.findByProposalId(proposalId);
        questions.stream().parallel().forEach(question -> {
            Artist artist = artistRepository.findByArtistId(question.getAskedBy());
            question.setAskedByFirstName(artist.getFirstName());
            question.setAskedBySlug(artist.getSlug());
            question.setAskedByLastName(artist.getLastName());
            question.setAskedByProfilePic(artist.getProfilePicUrl());
        });
        return questions;
    }

    @Override
    public List<ProposalQuestion> answerQuestion(String artistId, String proposalId, ProposalAnswerInput answerInput) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        if (!proposal.getCreatedBy().equals(artistId)) {
            throw new IllegalStateException("You cannot answer questions in the proposals created by someone else.");
        }

        ProposalQuestion proposalQuestion = proposalQuestionsRepository.findByProposalIdAndQuestionId(proposalId, answerInput.getQuestionId());
        if (proposalQuestion == null) {
            throw new IllegalStateException("Invalid proposal or question.");
        }

        proposalQuestion.setAnswer(answerInput.getAnswer());
        proposalQuestion.setUpdatedAt(Timestamp.from(Instant.now()));
        proposalRepository.save(proposal);
        return proposalQuestionsRepository.findByProposalId(proposalId);
    }

    @Override
    @SneakyThrows
    public ProposalInterest updateInterest(String artistId, String proposalId, boolean interested, String message) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        if (proposal.getCreatedBy().equals(artistId)) {
            throw  new IllegalStateException("You cannot show interest in your own proposals.");
        }

        if (!interested) {
            // If removing interest, the entry should be present
            ProposalInterest interest = proposalInterestRepository.findByProposalIdAndArtistId(proposalId, artistId);
            if (interest != null) {
                proposalInterestRepository.delete(interest);
            }
            return null;
        }

        ProposalInterest proposalInterest = new ProposalInterest();
        proposalInterest.setId(Constants.FALLBACK_ID);
        proposalInterest.setProposalId(proposalId);
        proposalInterest.setUserId(artistId);
        proposalInterest.setMessage(message == null ? "" : message);
        proposalInterest.setAccepted(false);
        proposalInterest.setRejected(false);
        proposalInterest.setCreatedAt(Timestamp.from(Instant.now()));
        proposalInterestRepository.save(proposalInterest);
        Artist creator = artistRepository.findByArtistId(proposal.getCreatedBy());
        Artist interstedUser = artistRepository.findByArtistId(artistId);
        emailService.sendEmailFromStringFinal("Your proposal has caught someone's attention. Kindly take a moment to evaluate it.", interstedUser.getArtistId(), creator.getEmail(), InterestShownEmail.getContent(creator.getFirstName(), proposal.getTitle(), interstedUser.getFirstName(), proposal.getProposalId()), false);
        return proposalInterestRepository.findByProposalIdAndArtistId(proposalId, artistId);
    }

    @Override
    @SneakyThrows
    public CollabRequestResponse acceptInterest(String artistId, String proposalId, String acceptedArtistId, CollabRequestInput requestInput) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        if (!proposal.getCreatedBy().equals(artistId)) {
            throw  new IllegalStateException("You cannot accept interests for proposals created by someone else.");
        }
        ProposalInterest interest = proposalInterestRepository.findByProposalIdAndArtistId(proposalId, acceptedArtistId);
        if (interest == null) {
            throw new IllegalStateException("You cannot accept as there is no interest shown by the artist for this proposal.");
        }

        CollabRequestResponse response = collabService.sendCollabRequest(artistId, requestInput);
        Artist artist = artistRepository.findByArtistId(artistId);
        Artist acceptedArtist = artistRepository.findByArtistId(artistId);
        interest.setAccepted(true);
        interest.setCollabId(response.getId());

        proposalInterestRepository.save(interest);
        emailService.sendEmailFromStringFinal("A creative update regarding your interest in a proposal", acceptedArtist.getFirstName(), acceptedArtist.getEmail(), InterestReject.getContent(acceptedArtist.getFirstName(), proposal.getTitle(), artist.getFirstName()), false);
        return response;
    }

    @Override
    @SneakyThrows
    public List<ProposalInterest> rejectInterest(String artistId, String proposalId, List<String> rejectedArtistIds) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        if (!proposal.getCreatedBy().equals(artistId)) {
            throw  new IllegalStateException("You cannot reject interests for proposals created by someone else.");
        }

        Artist artist = artistRepository.findByArtistId(artistId);
        for (String rejectedArtistId : rejectedArtistIds) {
            ProposalInterest interest = proposalInterestRepository.findByProposalIdAndArtistId(proposalId, rejectedArtistId);
            if (interest == null) continue;

            interest.setRejected(true);
            proposalInterestRepository.save(interest);
            Artist rejectedArtist = artistRepository.findByArtistId(rejectedArtistId);
            emailService.sendEmailFromStringFinal("A creative update regarding your interest in a proposal", rejectedArtist.getFirstName(), rejectedArtist.getEmail(), InterestReject.getContent(rejectedArtist.getFirstName(), proposal.getTitle(), artist.getFirstName()), false);

        }
        return proposalInterestRepository.findByProposalId(proposalId);
    }

    @Override
    public List<ProposalQuestion> getQuestionsOnProposals(String proposalId) {
        List<ProposalQuestion> questions = proposalQuestionsRepository.findByProposalId(proposalId);
        if (questions == null) {
            return new ArrayList<>();
        }
        questions.stream().parallel().forEach(question -> {
            String askedBy = question.getAskedBy();
            Artist artist = artistRepository.findByArtistId(askedBy);
            question.setAskedByFirstName(artist.getFirstName());
            question.setAskedByLastName(artist.getLastName());
            question.setAskedBySlug(artist.getSlug());
            question.setAskedByProfilePic(artist.getProfilePicUrl());
        });
        return questions;
    }

    @Override
    @SneakyThrows
    public List<ProposalInterest> getAllInterests(String proposalId) {
        Proposal proposal = proposalRepository.findByProposalId(proposalId);
        List<ProposalInterest> interests = proposalInterestRepository.findByProposalId(proposalId);
        if (interests == null) {
            return new ArrayList<>();
        }
        interests.stream().parallel().forEach(interest -> {
            String interestedUser = interest.getUserId();
            Artist artist = artistRepository.findByArtistId(interestedUser);
            interest.setAskedByFirstName(artist.getFirstName());
            interest.setAskedByLastName(artist.getLastName());
            interest.setAskedBySlug(artist.getSlug());
            interest.setAskedByProfilePic(artist.getProfilePicUrl());
        });
        return interests;
    }

    @Override
    public void removeCollabIdFromProposal(String proposalId, String interestedArtist) {
        List<ProposalInterest> interests = proposalInterestRepository.findByProposalId(proposalId);
        for (ProposalInterest interest : interests) {
            if (interest.getUserId().equalsIgnoreCase(interestedArtist)) {
                interest.setCollabId(null);
                interest.setAccepted(false);
                proposalInterestRepository.save(interest);
                return;
            }
        }
    }
}
