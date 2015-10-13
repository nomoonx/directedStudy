package org.noMoon.ArtificalSociety.institution.services;

import org.noMoon.ArtificalSociety.institution.DO.Institution;

import java.util.List;

/**
 * Created by noMoon on 2015-09-02.
 */
public interface InstitutionService {
    int insertNewInstitution(Institution institution);

    List<Institution> selectInstitutionByTitle(String societyId, String title);

    void loadAllInstitutions(String elemSchoolConfFilepath,String psSchoolConfFilepath, String templeConfFilepath,String societyId);

    List<Institution> selectInstitutionBySocietyId(String societyId);
}
