package com.example.invitationservice.controller;

import com.example.invitationservice.service.InvitationService;
import com.example.invitationservice.model.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping("/create")
    public ResponseEntity<Invitation> createInvitation(@RequestBody Invitation invitation) {
        Invitation createdInvitation = invitationService.createInvitation(invitation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<Invitation> getInvitationById(@PathVariable Long invitationId) {
        Invitation invitation = invitationService.getInvitationById(invitationId);
        return ResponseEntity.ok(invitation);
    }
    @PutMapping("/{invitationId}")
    public ResponseEntity<Invitation> updateInvitation(@PathVariable Long invitationId, @RequestBody Invitation invitation) {
        invitation.setId(invitationId);
        Invitation updatedInvitation = invitationService.updateInvitation(invitation);
        return ResponseEntity.ok(updatedInvitation);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Invitation>> getAllUserInvitations(@RequestParam String email) {
        List<Invitation> userInvitations = invitationService.getAllUserInvitations(email);
        return ResponseEntity.ok(userInvitations);
    }
}
