package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.email.EmailEnumHistory;
import com.collab.project.repositories.EmailEnumHistoryRepository;
import com.collab.project.service.EmailHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmailHistoryServiceImpl implements EmailHistoryService {


    @Autowired
    EmailEnumHistoryRepository emailEnumHistoryRepository;

    @Override
    public List<String> getAllEmailEnums() {
        return (List<String>) Constants.EmailGroups;
    }

    @Override
    public List<EmailEnumHistory> getAllEmailEnumsHistory() {
        List<EmailEnumHistory> details = new ArrayList<>();
        for (String emailEnum : Constants.EmailGroups) {
            Optional<EmailEnumHistory> value = emailEnumHistoryRepository.findByEmailEnum(emailEnum);
            if (value.isPresent()) {
                details.add(value.get());
            } else {
                details.add(new EmailEnumHistory(emailEnum, null));
            }
        }
        return details;
    }
}
