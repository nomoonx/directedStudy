package org.noMoon.ArtificalSociety.career.services;

import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;

/**
 * Created by noMoon on 2015-09-10.
 */
public interface CareerService {

    void insertNewCareer(CareerDTO careerDTO);

    void loadCareerAndWorkplace(String careerConfigFilepath, String workplaceConfigFilepath,String societyId);
}
