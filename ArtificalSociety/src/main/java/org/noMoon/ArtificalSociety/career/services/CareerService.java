package org.noMoon.ArtificalSociety.career.services;

import org.noMoon.ArtificalSociety.career.DO.Career;
import org.noMoon.ArtificalSociety.career.DO.Workplace;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;

import java.util.List;

/**
 * Created by noMoon on 2015-09-10.
 */
public interface CareerService {

    void insertNewCareer(CareerDTO careerDTO);

    void loadCareerAndWorkplace(String careerConfigFilepath, String workplaceConfigFilepath,String societyId);

    List<Workplace> listWorkplaceWithSocietyId(String societyId);

    List<Career> listCareerWithSocietyId(String societyId);

    Career selectCareerById(Long id);

    Workplace selectWorkplaceById(Long id);
}
