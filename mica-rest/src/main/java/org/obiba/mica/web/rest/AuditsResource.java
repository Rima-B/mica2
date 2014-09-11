package org.obiba.mica.web.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.obiba.mica.core.security.AuthoritiesConstants;
import org.obiba.mica.core.service.AuditEventService;
import org.springframework.boot.actuate.audit.AuditEvent;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * REST controller for getting the audit events.
 */
@Path("/audits")
public class AuditsResource {

  @Inject
  private AuditEventService auditEventService;

  //TODO joda DateTime conversion (ie PropertyEditor in Spring MVC)

  @GET
  @Path("/all")
  @Produces(APPLICATION_JSON)
  @RolesAllowed(AuthoritiesConstants.ADMIN)
  public List<AuditEvent> findAll() {
    return auditEventService.findAll();
  }

//  @GET
//  @Path("/byDates")
//  @RolesAllowed(AuthoritiesConstants.ADMIN)
//  public List<AuditEvent> findByDates(@QueryParam("fromDate") LocalDateTime fromDate,
//      @QueryParam("toDate") LocalDateTime toDate) {
//    return auditEventService.findByDates(fromDate, toDate);
//  }
}
