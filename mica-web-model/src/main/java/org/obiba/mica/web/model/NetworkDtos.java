package org.obiba.mica.web.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.obiba.mica.core.domain.Contact;
import org.obiba.mica.file.Attachment;
import org.obiba.mica.network.domain.Network;
import org.obiba.mica.study.domain.Study;
import org.obiba.mica.study.service.PublishedDatasetVariableService;
import org.obiba.mica.study.service.PublishedStudyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import jersey.repackaged.com.google.common.collect.Lists;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.obiba.mica.web.model.Mica.ContactDto;

@Component
class NetworkDtos {

  private static final Logger log = LoggerFactory.getLogger(NetworkDtos.class);

  @Inject
  private ContactDtos contactDtos;

  @Inject
  private LocalizedStringDtos localizedStringDtos;

  @Inject
  private AttachmentDtos attachmentDtos;

  @Inject
  private StudySummaryDtos studySummaryDtos;

  @Inject
  private PublishedStudyService publishedStudyService;

  @Inject
  private PublishedDatasetVariableService datasetVariableService;

  @Inject
  private AttributeDtos attributeDtos;

  @NotNull
  Mica.NetworkDto.Builder asDtoBuilder(@NotNull Network network) {
    Mica.NetworkDto.Builder builder = Mica.NetworkDto.newBuilder();
    builder.setId(network.getId()) //
        .setTimestamps(TimestampsDtos.asDto(network)) //
        .setPublished(network.isPublished()) //
        .addAllName(localizedStringDtos.asDto(network.getName())) //
        .addAllDescription(localizedStringDtos.asDto(network.getDescription())) //
        .addAllAcronym(localizedStringDtos.asDto(network.getAcronym())) //
        .addAllInfo(localizedStringDtos.asDto(network.getInfos()));

    if(network.getInvestigators() != null) {
      builder.addAllInvestigators(
          network.getInvestigators().stream().map(contactDtos::asDto).collect(Collectors.<ContactDto>toList()));
    }

    if(network.getContacts() != null) {
      builder.addAllContacts(
          network.getContacts().stream().map(contactDtos::asDto).collect(Collectors.<ContactDto>toList()));
    }

    if(!isNullOrEmpty(network.getWebsite())) builder.setWebsite(network.getWebsite());

    List<Study> publishedStudies = publishedStudyService.findByIds(network.getStudyIds());
    Set<String> publishedStudyIds = publishedStudies.stream().map(s -> s.getId()).collect(Collectors.toSet());
    Sets.SetView<String> unpublishedStudyIds = Sets.difference(ImmutableSet.copyOf(network.getStudyIds()),
      publishedStudyIds);

    if (!publishedStudies.isEmpty()) {
      Map<String, Long> datasetVariableCounts = datasetVariableService.getCountByStudyIds(
        Lists.newArrayList(publishedStudyIds));

      publishedStudies.forEach(study -> {
        builder.addStudyIds(study.getId());
        builder.addStudySummaries(studySummaryDtos.asDtoBuilder(study, true, datasetVariableCounts.get(study.getId())));
      });
    }

    unpublishedStudyIds.forEach(studyId -> {
      builder.addStudyIds(studyId);
      builder.addStudySummaries(studySummaryDtos.asDto(studyId));
    });

    network.getAttachments().forEach(attachment -> builder.addAttachments(attachmentDtos.asDto(attachment)));

    if(network.getMaelstromAuthorization() != null) {
      builder.setMaelstromAuthorization(AuthorizationDtos.asDto(network.getMaelstromAuthorization()));
    }

    if (network.getLogo() != null) {
      builder.setLogo(attachmentDtos.asDto(network.getLogo()));
    }

    if(network.getAttributes() != null) {
      network.getAttributes().asAttributeList().forEach(
        attribute -> builder.addAttributes(attributeDtos.asDto(attribute)));
    }

    return builder;
  }


  @NotNull
  Mica.NetworkDto asDto(@NotNull Network network) {
    return asDtoBuilder(network).build();
  }

  @NotNull
  Network fromDto(@NotNull Mica.NetworkDtoOrBuilder dto) {
    Network network = new Network();

    if (dto.hasId()) {
      network.setId(dto.getId());
    }
    
    network.setPublished(dto.getPublished());
    network.setName(localizedStringDtos.fromDto(dto.getNameList()));
    network.setDescription(localizedStringDtos.fromDto(dto.getDescriptionList()));
    network.setAcronym(localizedStringDtos.fromDto(dto.getAcronymList()));
    network.setInfos(localizedStringDtos.fromDto(dto.getInfoList()));
    network.setInvestigators(
        dto.getInvestigatorsList().stream().map(contactDtos::fromDto).collect(Collectors.<Contact>toList()));
    network.setContacts(dto.getContactsList().stream().map(contactDtos::fromDto).collect(Collectors.<Contact>toList()));
    if(dto.hasWebsite()) network.setWebsite(dto.getWebsite());
    if(dto.getStudyIdsCount() > 0) {
      dto.getStudyIdsList().forEach(network::addStudyId);
    }
    if(dto.getAttachmentsCount() > 0) {
      network.setAttachments(
          dto.getAttachmentsList().stream().map(attachmentDtos::fromDto).collect(Collectors.<Attachment>toList()));
    }

    if(dto.hasMaelstromAuthorization())
      network.setMaelstromAuthorization(AuthorizationDtos.fromDto(dto.getMaelstromAuthorization()));

    if(dto.hasLogo()) {
      network.setLogo(attachmentDtos.fromDto(dto.getLogo()));
    }

    if(dto.getAttributesCount() > 0) {
      dto.getAttributesList().forEach(attributeDto -> network.addAttribute(attributeDtos.fromDto(attributeDto)));
    }
    return network;
  }

}
