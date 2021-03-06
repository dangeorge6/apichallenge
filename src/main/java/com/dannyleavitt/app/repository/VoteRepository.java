package com.dannyleavitt.app.repository;

import com.dannyleavitt.app.domain.Client;
import com.dannyleavitt.app.domain.DogPhoto;
import com.dannyleavitt.app.domain.Vote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vote entity.
 */
@SuppressWarnings("unused")
public interface VoteRepository extends JpaRepository<Vote,Long> {

	Vote findByDogPhotoAndClient(DogPhoto dp, Client client);

}
