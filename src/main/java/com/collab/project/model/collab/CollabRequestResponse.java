package com.collab.project.model.collab;

import com.collab.project.model.artist.Artist;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
public class CollabRequestResponse {
    private String id;

    private String senderId;

    private String receiverId;

    private String senderName;

    private String receiverName;

    private String senderSlug;

    private String receiverSlug;

    private String senderProfilePicUrl;

    private String receiverProfilePicUrl;

    private Timestamp collabDate;

    private RequestData requestData;

    private String status;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private boolean newComment;

    private String proposalId;

    public CollabRequestResponse(CollabRequest request, Artist sender, Artist receiver) {
        this.id = request.getId();
        this.collabDate = request.getCollabDate();
        this.senderId = request.getSenderId();
        this.receiverId = request.getReceiverId();
        this.requestData = request.getRequestData();
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
        this.updatedAt = request.getUpdatedAt();

        this.senderName = sender.getFirstName();
        this.receiverName = receiver.getFirstName();
        this.senderSlug = sender.getSlug();
        this.receiverSlug = receiver.getSlug();
        this.senderProfilePicUrl = sender.getProfilePicUrl();
        this.receiverProfilePicUrl = receiver.getProfilePicUrl();
        this.proposalId = request.getProposalId();
    }
}
