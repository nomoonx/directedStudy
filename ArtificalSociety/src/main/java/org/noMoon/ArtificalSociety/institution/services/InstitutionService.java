package org.noMoon.ArtificalSociety.institution.services;

import org.noMoon.ArtificalSociety.institution.DO.Institution;

/**
 * Created by noMoon on 2015-09-02.
 */
public interface InstitutionService {
    int insertNewInstitution(Institution institution);

    void loadAllInstitutions(String elemSchoolConfFilepath,String psSchoolConfFilepath, String templeConfFilepath);
}
