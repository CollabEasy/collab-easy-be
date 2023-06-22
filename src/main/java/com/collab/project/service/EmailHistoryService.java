package com.collab.project.service;

import com.collab.project.model.email.EmailEnumHistory;

import java.util.List;

public interface EmailHistoryService {

    public List<String> getAllEmailEnums();

    public List<EmailEnumHistory> getAllEmailEnumsHistory();
}
