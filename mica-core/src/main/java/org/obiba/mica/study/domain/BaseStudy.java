/*
 * Copyright (c) 2017 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.mica.study.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.validator.constraints.URL;
import org.obiba.mica.core.domain.*;
import org.obiba.mica.file.Attachment;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Base class for representing all type of studies.
 */
public abstract class BaseStudy extends AbstractModelAware implements PersonAware {

  private Attachment logo;

  @NotNull
  private LocalizedString name;

  private LocalizedString acronym;

  private LocalizedString objectives;

  private Map<String, List<Membership>> memberships = new HashMap<String, List<Membership>>() {
    {
      put(Membership.CONTACT, Lists.newArrayList());
      put(Membership.INVESTIGATOR, Lists.newArrayList());
    }
  };

  @URL
  private String opal;

  //
  // Accessors
  //

  public Attachment getLogo() {
    return logo;
  }

  public boolean hasLogo() {
    return logo != null;
  }

  public void setLogo(Attachment logo) {
    this.logo = logo;
  }

  public LocalizedString getName() {
    return name;
  }

  public void setName(LocalizedString name) {
    this.name = name;
  }

  public LocalizedString getAcronym() {
    return acronym;
  }

  public void setAcronym(LocalizedString acronym) {
    this.acronym = acronym;
  }

  public LocalizedString getObjectives() {
    return objectives;
  }

  public void setObjectives(LocalizedString objectives) {
    this.objectives = objectives;
  }

  public String getOpal() {
    return opal;
  }

  public void setOpal(String opal) {
    this.opal = opal;
  }

  //
  // PersonAware methods
  //

  public Set<String> membershipRoles() {
    return this.memberships.keySet();
  }

  public Map<String, List<Membership>> getMemberships() {
    return memberships;
  }

  @Override
  public List<Person> getAllPersons() {
    return getMemberships().values().stream().flatMap(List::stream).map(Membership::getPerson).distinct()
        .collect(toList());
  }

  @Override
  public List<Membership> getAllMemberships() {
    return getMemberships().values().stream().flatMap(List::stream).collect(toList());
  }

  @Override
  public void addToPerson(Membership membership) {
    membership.getPerson().addStudy(this, membership.getRole());
  }

  @Override
  public void removeFromPerson(Membership membership) {
    membership.getPerson().removeStudy(this, membership.getRole());
  }

  @Override
  public void removeFromPerson(Person person) {
    person.removeStudy(this);
  }

  public void setMemberships(Map<String, List<Membership>> memberships) {
    if (memberships == null) {
      this.memberships.clear();
    } else {
      this.memberships = memberships;
    }
    Map<String, Person> seen = Maps.newHashMap();

    this.memberships.entrySet().forEach(e -> e.getValue().forEach(m -> {
      if (seen.containsKey(m.getPerson().getId())) {
        m.setPerson(seen.get(m.getPerson().getId()));
      } else if (!m.getPerson().isNew()) {
        seen.put(m.getPerson().getId(), m.getPerson());
      }
    }));
  }

  public List<Person> removeRole(String role) {
    List<Membership> members = this.memberships.getOrDefault(role, Lists.newArrayList());
    this.memberships.remove(role);
    return members.stream().map(m -> {
      m.getPerson().removeStudy(this, role);
      return m.getPerson();
    }).collect(toList());
  }

  //
  // Index
  //

  public String getClassName() {
    return getClass().getSimpleName();
  }

  // for JSON deserial
  public void setClassName(String className) {}
}