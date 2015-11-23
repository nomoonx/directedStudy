package org.noMoon.ArtificalSociety.person.services;

import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;

import java.util.List;

/**
 * Created by noMoon on 2015-10-17.
 */
public interface PersonService {
    void generatePerson(int singleNumber,int coupleNumber,int datingNumber,String societyId);

    List<String> getAllIds(String societyId);

    PersonDTO selectPerosonDTOById(String id);

    void checkDeath();

    void removePerson(PersonDTO person);

    void evaluateRelationship_Single(PersonDTO person);
    void evaluateRelationship_Couple (PersonDTO person, PersonDTO partner, RelationStatusEnum relationshipType);

    void evaluatePerson_Hometown (PersonDTO person);

    void evaluatePerson_Student (PersonDTO person);

    void evaluatePerson_Unemployed (PersonDTO person);

    void evaluatePerson_Working(PersonDTO person);

    void evaluatePerson_Groups (PersonDTO person);

    void updatePersonDTOById(PersonDTO person);

}
