package com.collab.project.repositories;

import com.collab.project.model.contest.Contest;
import com.collab.project.model.contest.ContestSubmissionVote;
import com.collab.project.model.email.EmailEnumHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailEnumHistoryRepository extends JpaRepository<EmailEnumHistory, String> {
    public Optional<EmailEnumHistory> findByEmailEnum(String emailEnum);
}
