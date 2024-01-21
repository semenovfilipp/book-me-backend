package com.example.userservice.controller;

import com.example.invitationservice.controller.InvitationController;
import com.example.invitationservice.service.InvitationService;
import com.example.invitationservice.model.Invitation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvitationControllerTest {

    @Mock
    private InvitationService invitationService;

    @InjectMocks
    private InvitationController invitationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInvitation() {
        Invitation invitation = new Invitation();

        when(invitationService.createInvitation(invitation)).thenReturn(invitation);

        ResponseEntity<Invitation> responseEntity = invitationController.createInvitation(invitation);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(invitationService, times(1)).createInvitation(invitation);
    }

    @Test
    public void testGetInvitationById() {
        Long invitationId = 1L;
        Invitation invitation = new Invitation();

        when(invitationService.getInvitationById(invitationId)).thenReturn(invitation);

        ResponseEntity<Invitation> responseEntity = invitationController.getInvitationById(invitationId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(invitationService, times(1)).getInvitationById(invitationId);
    }

    @Test
    public void testUpdateInvitation() {
        Long invitationId = 1L;
        Invitation invitation = new Invitation();

        when(invitationService.updateInvitation(invitation)).thenReturn(invitation);

        ResponseEntity<Invitation> responseEntity = invitationController.updateInvitation(invitationId, invitation);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(invitationService, times(1)).updateInvitation(invitation);
    }

    @Test
    public void testGetAllUserInvitations() {
        String userEmail = "test@example.com";
        List<Invitation> userInvitations = Collections.emptyList();

        when(invitationService.getAllUserInvitations(userEmail)).thenReturn(userInvitations);

        ResponseEntity<List<Invitation>> responseEntity = invitationController.getAllUserInvitations(userEmail);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(invitationService, times(1)).getAllUserInvitations(userEmail);
    }
}
