package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.EventLocation;
import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.model.dto.EventMemberDTO;

public interface InvitationService {

  void sendEventInvitation(EventMemberDTO eventMemberDTO, Event event, EventLocation eventLocation, User eventOwner);
}
