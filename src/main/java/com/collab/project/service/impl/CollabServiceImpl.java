package com.collab.project.service.impl;

import com.amazonaws.services.mq.model.UnauthorizedException;
import com.collab.project.exception.CollabRequestException;
import com.collab.project.exception.CollabRequestLimitReachedException;
import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.collab.*;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.proposal.ProposalInterest;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.CollabConversationReadStatusRepository;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.search.SpecificationBuilder;
import com.collab.project.service.ArtistPreferencesService;
import com.collab.project.service.CollabService;
import com.collab.project.service.NotificationService;
import com.collab.project.service.ProposalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CollabServiceImpl implements CollabService {

    @Autowired
    private ArtistPreferencesService artistPreferencesService;
    
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CollabRequestRepository collabRequestRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CollabConversationReadStatusRepository collabConversationReadStatusRepository;

    @Autowired
    private ProposalService proposalService;

    @Override
    public CollabRequestResponse sendRequest(String artistId, CollabRequestInput collabRequestInput) throws JsonProcessingException {
        // Add validation on receiver id and handle idempotency on requestId
        // If there is any active collab request present, there should not be a new request.
        if (collabRequestInput.getProposalId() != null && !collabRequestInput.getProposalId().equals("")) {
            return proposalService.acceptInterest(artistId, collabRequestInput.getProposalId(), collabRequestInput.getReceiverId(), collabRequestInput);
        }
        return sendCollabRequest(artistId, collabRequestInput);
    }

    public CollabRequestResponse sendCollabRequest(String artistId, CollabRequestInput collabRequestInput) throws JsonProcessingException {
        if (getCollabRequestsBetweenUsers(artistId, collabRequestInput.getReceiverId()).size() == Constants.ALLOWED_COLLAB_REQUEST_PER_USER) {
            throw new CollabRequestLimitReachedException();
        }

        Artist sender = artistRepository.getOne(artistId);
        Artist receiver = artistRepository.getOne(collabRequestInput.getReceiverId());

        CollabRequest saveCollabRequest = CollabRequest.builder()
                .id(UUID.randomUUID().toString())
                .senderId(artistId)
                .receiverId(collabRequestInput.getReceiverId())
                .collabDate(Timestamp.valueOf(collabRequestInput.getCollabDate()))
                .status(Enums.CollabStatus.PENDING.toString())
                .requestData(collabRequestInput.getRequestData())
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .proposalId(collabRequestInput.getProposalId())
                .build();

        collabRequestRepository.save(saveCollabRequest);
        artistPreferencesService.updateArtistPreferences(artistId, new HashMap<String, Object>() {{
            put("upForCollaboration", true);
        }});
        // TODO : Uncomment this when notification service will be added
//        notificationService.addNotification(Notification.builder()
//                                                    .artistId(artistId)
//                                                    .redirectId(collabRequestInput.getReceiverId())
//                                                    .notifType("collabRequest")
//                                                    .notifRead(false)
//                                                    .notificationData(String.format("%s send you a collab request", artistId))
//                                                    .build());


        return new CollabRequestResponse(saveCollabRequest, sender, receiver);
    }

    @Override
    public CollabRequestResponse updateRequest(String artistId, CollabRequest collabRequestInput) {
        Optional<CollabRequest> collabRequestOptional = collabRequestRepository.findById(collabRequestInput.getId());
        if (collabRequestOptional.isPresent()) {
            CollabRequest collabRequest = collabRequestOptional.get();
            collabRequestInput.setUpdatedAt(Timestamp.from(Instant.now()));
            collabRequestRepository.save(collabRequestInput);
            Artist sender = artistRepository.getOne(collabRequest.getSenderId());
            Artist receiver = artistRepository.getOne(collabRequest.getReceiverId());
            return new CollabRequestResponse(collabRequestInput, sender, receiver);
        }
        return null;
    }

    @Override
    public void cancelRequest(String artistId, String requestId) {
        Optional<CollabRequest> collabRequest = collabRequestRepository.findById(requestId);

        if (collabRequest.isPresent()) {
            CollabRequest request = collabRequest.get();
            if (request.getProposalId() != null && !request.getProposalId().equals("")) {
                proposalService.removeCollabIdFromProposal(request.getProposalId(), request.getReceiverId());
            }
            if (request.getSenderId().equalsIgnoreCase(artistId)) {
                collabRequestRepository.delete(collabRequest.get());
            } else {
                throw new UnauthorizedException("You are not authorized to perform this action.");
            }
        }
    }

    @Override
    public CollabRequestResponse completeRequest(String artistId, String completeRequestId) {
        Optional<CollabRequest> collabCompletedByReceiver = collabRequestRepository.findById(completeRequestId);
        if(collabCompletedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabCompletedByReceiver.get();
            Artist sender = artistRepository.getOne(collabRequest.getSenderId());
            Artist receiver = artistRepository.getOne(collabRequest.getReceiverId());
            if((collabRequest.getReceiverId().equals(artistId) || collabRequest.getSenderId().equals(artistId)) && collabRequest.getStatus().equals(Enums.CollabStatus.ACTIVE.toString())) {
                collabRequest.setStatus(Enums.CollabStatus.COMPLETED.toString());
                collabRequest = collabRequestRepository.save(collabRequest);
                return new CollabRequestResponse(collabRequest, sender, receiver);
            } else {
                if(!collabRequest.getReceiverId().equals(artistId) && !collabRequest.getSenderId().equals(artistId)) {
                    throw new CollabRequestException(String.format("user not authorized to complete request"));
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.ACTIVE.toString())) {
                    throw new CollabRequestException(String.format("collab request status is not in active state"));
                }
                throw new CollabRequestException(String.format("error while completing request"));
            }
        } else {
            throw new CollabRequestException(String.format("collab request id is not present"));
        }
    }
    @Override
    public CollabRequestResponse rejectRequest(String artistId, String rejectRequestId) {
        Optional<CollabRequest> collabRejectedByReceiver = collabRequestRepository.findById(rejectRequestId);
        if(collabRejectedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabRejectedByReceiver.get();
            Artist sender = artistRepository.getOne(collabRequest.getSenderId());
            Artist receiver = artistRepository.getOne(collabRequest.getReceiverId());
            if( collabRequest.getReceiverId().equals(artistId) && collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                collabRequest.setStatus(Enums.CollabStatus.REJECTED.toString());
                collabRequest = collabRequestRepository.save(collabRequest);
                return new CollabRequestResponse(collabRequest, sender, receiver);
            } else {
                if(!collabRequest.getReceiverId().equals(artistId)) {
                    throw new CollabRequestException(String.format("user not authorized to reject request"));
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                    throw new CollabRequestException(String.format("collab request status is not in pending state"));
                }
                throw new CollabRequestException(String.format("error while rejecting request"));
            }
        } else {
            throw new CollabRequestException(String.format("collab request id is not present"));
        }
    }

    @Override
    public CollabRequestResponse acceptRequest(String artistId, String acceptRequestId) {
        Optional<CollabRequest> collabAcceptedByReceiver = collabRequestRepository.findById(acceptRequestId);
        if(collabAcceptedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabAcceptedByReceiver.get();
            Artist sender = artistRepository.getOne(collabRequest.getSenderId());
            Artist receiver = artistRepository.getOne(collabRequest.getReceiverId());
            if( collabRequest.getReceiverId().equals(artistId) && collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                collabRequest.setStatus(Enums.CollabStatus.ACTIVE.toString());
                collabRequest = collabRequestRepository.save(collabRequest);
                return new CollabRequestResponse(collabRequest, sender, receiver);
            } else {
                if(!collabRequest.getReceiverId().equals(artistId)) {
                    throw new CollabRequestException(String.format("user not authorized to accept request"));
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                    throw new CollabRequestException(String.format("collab request status is not in pending state"));
                }
                throw new CollabRequestException(String.format("error while accepting request"));
            }
        } else {
            throw new CollabRequestException(String.format("collab request id is not present"));
        }
    }

    private CollabRequestOutput createOutput(List<CollabRequest> requests, String loggedInArtistId, Artist loggedInArtist) {
        requests.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        CollabRequestOutput collabRequestOutput = new CollabRequestOutput();
        collabRequestOutput.setSent(new CollabRequestsStatus());
        collabRequestOutput.setReceived(new CollabRequestsStatus());
        for (CollabRequest collabRequest : requests) {
            Artist sender = artistRepository.getOne(collabRequest.getSenderId());
            Artist receiver = artistRepository.getOne(collabRequest.getReceiverId());

            CollabRequestResponse request = new CollabRequestResponse(collabRequest, sender, receiver);


                    List<CollabConversationReadStatus> readStatusList =
                    collabConversationReadStatusRepository.findByCollabId(request.getId());
            boolean isNewComment = !readStatusList.isEmpty()
                    && (readStatusList.get(0).getArtistId().equals(loggedInArtistId)
                    || (readStatusList.size() > 1 && readStatusList.get(1).getArtistId().equals(loggedInArtistId)));

            request.setNewComment(isNewComment);
            CollabRequestsStatus collabRequestsStatus = null;
            if (request.getSenderId().equals(loggedInArtistId)) {
                collabRequestsStatus = collabRequestOutput.getSent();
            } else {
                collabRequestsStatus = collabRequestOutput.getReceived();
            }

            collabRequestsStatus.getAll().add(request);
            if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.ACTIVE.toString())) {
                collabRequestsStatus.getActive().add(request);
            } else if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.PENDING.toString())) {
                collabRequestsStatus.getPending().add(request);
            } else if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.REJECTED.toString())) {
                collabRequestsStatus.getRejected().add(request);
            } else if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.COMPLETED.toString())) {
                collabRequestsStatus.getCompleted().add(request);
            }
        }
        return collabRequestOutput;
    }

    @Override
    @SneakyThrows
    public CollabRequestOutput collabRequestsSearch(String loggedInArtistId, CollabRequestSearch collabRequestSearch) {
        SpecificationBuilder<CollabRequest> builder =
                new SpecificationBuilder();
        Artist loggedInArtist = artistRepository.findByArtistId(loggedInArtistId);
        if (collabRequestSearch.getCollabRequestId() != null) {
            Optional<CollabRequest> request = collabRequestRepository.findById(collabRequestSearch.getCollabRequestId());
            if (request.isPresent()) {
                CollabRequest collabRequest = request.get();
                if (collabRequest.getSenderId().equals(loggedInArtistId) || collabRequest.getReceiverId().equals(loggedInArtistId)) {
                    List<CollabRequest> result = new ArrayList<>();
                    result.add(collabRequest);
                    return createOutput(result, loggedInArtistId, loggedInArtist);
                }
            } else {
                throw new IllegalStateException("Invalid collaboration request id.");
            }
        }


        builder.with("senderId", ":", loggedInArtistId);
        if (collabRequestSearch.getOtherUserId() != null) {
            builder.with("receiverId", ":", collabRequestSearch.getOtherUserId());
        }
        if (!Strings.isNullOrEmpty(collabRequestSearch.getStatus())) {
            builder.with("status", ":", collabRequestSearch.getStatus());
        }

        Specification<CollabRequest> specification = builder.build();
        List<CollabRequest> collabRequests = collabRequestRepository.findAll(specification);
        updateCollabRequestStatus(collabRequests);

        builder = new SpecificationBuilder();
        builder.with("receiverId", ":", loggedInArtistId);
        if (collabRequestSearch.getOtherUserId() != null) {
            builder.with("senderId", ":", collabRequestSearch.getOtherUserId());
        }
        if (!Strings.isNullOrEmpty(collabRequestSearch.getStatus())) {
            builder.with("status", ":", collabRequestSearch.getStatus());
        }
        updateCollabRequestStatus(collabRequests);
        specification = builder.build();
        collabRequests.addAll(collabRequestRepository.findAll(specification));

        return createOutput(collabRequests, loggedInArtistId, loggedInArtist);
    }

    private List<CollabRequest> getCollabRequestsBetweenUsers(String user1, String user2) {
        List<String> status =  Arrays.asList(Enums.CollabStatus.PENDING.toString(),
                Enums.CollabStatus.ACTIVE.toString());
        List<CollabRequest> requests1 = collabRequestRepository.findBySenderIdAndReceiverIdAndStatusIn(user1, user2, status);
        List<CollabRequest> requests2 = collabRequestRepository.findBySenderIdAndReceiverIdAndStatusIn(user2, user1, status);
        List<CollabRequest> requests = new ArrayList<>();
        requests.addAll(requests1);
        requests.addAll(requests2);
        return requests;
    }
    @Override
    public CollabEligibilityOutput canCreateNewCollabRequest(String user1, String user2slug) {
        Artist artist1 = artistRepository.findByArtistId(user1);
        List<Artist> artistSlugResult = artistRepository.findBySlug(user2slug);
        Artist artist2 = artistSlugResult.get(0);
        List<CollabRequest> requests = getCollabRequestsBetweenUsers(user1, artist2.getArtistId());
        List<CollabRequestResponse> result = new ArrayList<>();
        requests.forEach(request -> {
            Artist sender = request.getSenderId().equals(artist1.getArtistId()) ? artist1 : artist2;
            Artist receiver = request.getSenderId().equals(artist1.getArtistId()) ? artist2 : artist1;
            result.add(new CollabRequestResponse(request, sender, receiver));
        });
        return new CollabEligibilityOutput(result, artist2.getArtistId(), result.size() < Constants.ALLOWED_COLLAB_REQUEST_PER_USER, Constants.ALLOWED_COLLAB_REQUEST_PER_USER);
    }

    @Override
    public Map<String, List<CollabRequestResponse>> fetchCollabsByDate(String artistId, boolean fetchAll) {
        List<String> allStatus =  Stream.of(Enums.CollabStatus.values())
                .map(Enums.CollabStatus::name)
                .collect(Collectors.toList());
        List<String> activeStatus = new ArrayList<String>(
                Arrays.asList(Enums.CollabStatus.PENDING.toString(),
                        Enums.CollabStatus.ACTIVE.toString()));

        List<String> statusToFetch = fetchAll ? allStatus : activeStatus;
        List<CollabRequest> requests = collabRequestRepository.findBySenderIdAndStatusIn(
                artistId,
                statusToFetch);
        System.out.println("fetched collab list : " + requests.size());

        requests.addAll(collabRequestRepository.findByReceiverIdAndStatusIn(artistId, statusToFetch));
        System.out.println("fetched collab list : " + requests.size());
        return breakCollabByData(artistId, requests);
    }

    @SneakyThrows
    private Map<String, List<CollabRequestResponse>> breakCollabByData(String artistId, List<CollabRequest> requests) {
        TreeMap<String, List<CollabRequestResponse>> requestsByDate = new TreeMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM YYY");
        Artist artist = artistRepository.findByArtistId(artistId);
        for (CollabRequest request : requests) {
            String dateString = format.format(request.getCollabDate());
            String receiverId = request.getSenderId().equals(artistId) ? request.getReceiverId() : request.getSenderId();
            Artist receiver = artistRepository.findByArtistId(receiverId);
            if (!requestsByDate.containsKey(dateString)) {
                requestsByDate.put(dateString, new ArrayList<>());
            }
            requestsByDate.getOrDefault(dateString, new ArrayList<>()).add(
                    new CollabRequestResponse(request,
                            request.getSenderId().equals(artistId) ? artist : receiver,
                            request.getReceiverId().equals(artistId) ? receiver : artist)
            );
        }
        return requestsByDate.descendingMap();
    }

    private void updateCollabRequestStatus(List<CollabRequest> collabRequests) {
        for (CollabRequest request : collabRequests) {
            if (request.getCollabDate().before(Timestamp.from(Instant.now())) &&
                    !request.getStatus().equalsIgnoreCase(String.valueOf(Enums.CollabStatus.COMPLETED)) &&
                            !request.getStatus().equalsIgnoreCase(String.valueOf(Enums.CollabStatus.EXPIRED))) {
                if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.ACTIVE.toString())) {
                    request.setStatus(String.valueOf(Enums.CollabStatus.COMPLETED));
                } else {
                    request.setStatus(String.valueOf(Enums.CollabStatus.EXPIRED));
                }
                collabRequestRepository.save(request);
            }
        }
    }


}
