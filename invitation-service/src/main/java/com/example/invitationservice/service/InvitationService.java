package com.example.invitationservice.service;

import com.example.invitationservice.client.UserClient;
import com.example.invitationservice.exception.InvitationNotFoundException;
import com.example.invitationservice.model.Invitation;
import com.example.invitationservice.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserClient userClient;


    public Invitation createInvitation(Invitation invitation) {
        String currentUserEmail = userClient.getCurrentUser();

        if (!currentUserEmail.equals(invitation.getSenderEmail())) {
            log.warn("Attempted to create invitation with invalid senderEmail. User: {}", currentUserEmail);
            throw new SecurityException("Invalid senderEmail for creating invitation");
        }

        invitation.setSenderEmail(currentUserEmail);

        return invitationRepository.save(invitation);
    }

    public Invitation getInvitationById(Long invitationId) {
        String currentUserEmail = userClient.getCurrentUser();

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(
                        "Invitation not found with id: "+ invitationId
                                + " for user: " + currentUserEmail));
        
        if (invitation == null || (!currentUserEmail.equals(invitation.getSenderEmail()))) {
            log.warn("Attempted to access invalid invitation. User: {}, InvitationId: {}", currentUserEmail, invitationId);
            throw new SecurityException("Invalid invitation for user");
        }

        return invitation;
    }
    public List<Invitation> getAllUserInvitations(String email) {
        String currentUserEmail = userClient.getCurrentUser();

        if (!currentUserEmail.equals(email)) {
            log.warn("Attempted to create invitation with invalid senderId. User: {}", currentUserEmail);
            throw new SecurityException("Invalid senderId for creating invitation");
        }
        return invitationRepository.findAllInvitationsBySenderEmail(email);
    }

    public Invitation updateInvitation(Invitation invitation) {
        String currentUserEmail = userClient.getCurrentUser();

        if (!currentUserEmail.equals(invitation.getSenderEmail())) {
            log.warn("Attempted to update invitation with invalid senderId. User: {}", currentUserEmail);
            throw new SecurityException("Invalid senderId for updating invitation");
        }
        return invitationRepository.save(invitation);
    }
}
