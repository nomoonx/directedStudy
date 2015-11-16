package org.noMoon.ArtificalSociety.person.services;

import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;

import java.util.List;

/**
 * Created by noMoon on 2015-10-17.
 */
public interface PersonService {
    void generatePerson(int singleNumber,int coupleNumber,int datingNumber,String societyId);

    List<String> getAllIds(String societyId);

    PersonDTO selectPerosonDTOById(String id);


}
