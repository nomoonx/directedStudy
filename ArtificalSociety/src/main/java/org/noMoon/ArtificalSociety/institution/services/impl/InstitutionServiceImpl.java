package org.noMoon.ArtificalSociety.institution.services.impl;

import org.noMoon.ArtificalSociety.institution.DAO.InstitutionMapper;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;

/**
 * Created by noMoon on 2015-09-02.
 */
public class InstitutionServiceImpl implements InstitutionService {

    InstitutionMapper institutionMapper;

    public void loadAllInstitutions(String elemSchoolConfFilepath, String psSchoolConfFilepath, String templeConfFilepath) {

    }

    public int insertNewInstitution(Institution institution) {
        int rows=institutionMapper.insertSelective(institution);
        System.out.println(institution.getId());
        return rows;
    }

    public void setInstitutionMapper(InstitutionMapper institutionMapper) {
        this.institutionMapper = institutionMapper;
    }
}
