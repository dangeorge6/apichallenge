package com.dannyleavitt.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dannyleavitt.app.domain.Client;
import com.dannyleavitt.app.domain.Vote;
import com.dannyleavitt.app.service.ClientService;
import com.dannyleavitt.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Client.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);
        
    @Inject
    private ClientService clientService;
    
    
    
   //up vote
    @RequestMapping(value = "/clients/{clientId}/dog-photo/{dogId}/voteup",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vote> upVotePhoto(@PathVariable Long clientId,@PathVariable Long dogId) throws URISyntaxException {
        log.debug("REST request to Client upVotePhoto : {}", clientId);
        Vote result = clientService.upVotePhoto(clientId,dogId);
        return Optional.ofNullable(result)
                .map(u -> new ResponseEntity<>(
                        u,
                        HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    //down vote
    @RequestMapping(value = "/clients/{clientId}/dog-photo/{dogId}/votedown",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Vote> downVotePhoto(@PathVariable Long clientId,@PathVariable Long dogId) throws URISyntaxException {
            log.debug("REST request to Client upVotePhoto : {}", clientId);
            Vote result = clientService.downVotePhoto(clientId,dogId);
            return Optional.ofNullable(result)
                    .map(u -> new ResponseEntity<>(
                            u,
                            HttpStatus.OK))
                        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    
    
    
    
    
    
    
    
    /**
     * POST  /clients : Create a new client.
     *
     * @param client the client to create
     * @return the ResponseEntity with status 201 (Created) and with body the new client, or with status 400 (Bad Request) if the client has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to save Client : {}", client);
        if (client.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("client", "idexists", "A new client cannot already have an ID")).body(null);
        }
        Client result = clientService.save(client);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("client", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clients : Updates an existing client.
     *
     * @param client the client to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated client,
     * or with status 400 (Bad Request) if the client is not valid,
     * or with status 500 (Internal Server Error) if the client couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to update Client : {}", client);
        if (client.getId() == null) {
            return createClient(client);
        }
        Client result = clientService.save(client);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("client", client.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clients : get all the clients.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clients in body
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Client> getAllClients() {
        log.debug("REST request to get all Clients");
        return clientService.findAll();
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the client to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the client, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/clients/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Client client = clientService.findOne(id);
        return Optional.ofNullable(client)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clients/:id : delete the "id" client.
     *
     * @param id the id of the client to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/clients/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("client", id.toString())).build();
    }

}
