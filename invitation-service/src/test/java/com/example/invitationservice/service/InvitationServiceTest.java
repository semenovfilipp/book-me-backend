package com.example.invitationservice.service;

import com.example.invitationservice.client.UserClient;
import com.example.invitationservice.exception.InvitationNotFoundException;
import com.example.invitationservice.model.Invitation;
import com.example.invitationservice.repository.InvitationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvitationServiceTest {

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private InvitationService invitationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInvitation() {
        Invitation invitation = new Invitation();
        invitation.setSenderEmail("test@example.com");

        when(userClient.getCurrentUser()).thenReturn("test@example.com");
        when(invitationRepository.save(invitation)).thenReturn(invitation);

        Invitation createdInvitation = invitationService.createInvitation(invitation);

        assertNotNull(createdInvitation);
        assertEquals("test@example.com", createdInvitation.getSenderEmail());

        verify(invitationRepository, times(1)).save(any());
    }

    @Test
    public void testGetInvitationById() {
        Long invitationId = 1L;
        String currentUserEmail = "test@example.com";
        Invitation invitation = new Invitation();
        invitation.setId(invitationId);
        invitation.setSenderEmail(currentUserEmail);

        when(userClient.getCurrentUser()).thenReturn(currentUserEmail);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        Invitation retrievedInvitation = invitationService.getInvitationById(invitationId);

        assertNotNull(retrievedInvitation);
        assertEquals(invitationId, retrievedInvitation.getId());
        assertEquals(currentUserEmail, retrievedInvitation.getSenderEmail());

        verify(invitationRepository, times(1)).findById(invitationId);
    }

    @Test
    public void testGetInvitationByIdNotFound() {
        Long invitationId = 1L;
        when(userClient.getCurrentUser()).thenReturn("test@example.com");
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.empty());

        assertThrows(InvitationNotFoundException.class, () -> invitationService.getInvitationById(invitationId));
    }

    @Test
    public void testGetInvitationByIdInvalidUser() {
        Long invitationId = 1L;
        String currentUserEmail = "test@example.com";
        Invitation invitation = new Invitation();
        invitation.setSenderEmail("other@example.com");

        when(userClient.getCurrentUser()).thenReturn(currentUserEmail);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        assertThrows(SecurityException.class, () -> invitationService.getInvitationById(invitationId));
    }

    @Test
    public void testGetAllUserInvitations() {
        String currentUserEmail = "test@example.com";
        when(userClient.getCurrentUser()).thenReturn(currentUserEmail);
        when(invitationRepository.findAllInvitationsBySenderEmail(currentUserEmail)).thenReturn(Collections.emptyList());

        List<Invitation> invitations = invitationService.getAllUserInvitations(currentUserEmail);

        assertNotNull(invitations);
        assertTrue(invitations.isEmpty());

        verify(invitationRepository, times(1)).findAllInvitationsBySenderEmail(currentUserEmail);
    }

    @Test
    public void testUpdateInvitation() {
        Invitation invitation = new Invitation();
        invitation.setSenderEmail("test@example.com");

        when(userClient.getCurrentUser()).thenReturn("test@example.com");
        when(invitationRepository.save(invitation)).thenReturn(invitation);

        Invitation updatedInvitation = invitationService.updateInvitation(invitation);

        assertNotNull(updatedInvitation);
        assertEquals("test@example.com", updatedInvitation.getSenderEmail());

        verify(invitationRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateInvitationInvalidUser() {
        Invitation invitation = new Invitation();
        invitation.setSenderEmail("other@example.com");

        when(userClient.getCurrentUser()).thenReturn("test@example.com");

        assertThrows(SecurityException.class, () -> invitationService.updateInvitation(invitation));
    }
}
