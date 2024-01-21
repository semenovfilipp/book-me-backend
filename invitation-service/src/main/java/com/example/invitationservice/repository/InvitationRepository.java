package com.example.invitationservice.repository;

import com.example.invitationservice.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    List<Invitation> findAllInvitationsBySenderEmail(String email);
}
