package org.noMoon.ArtificalSociety.career.services.impl;

import org.noMoon.ArtificalSociety.career.DAO.CareerMapper;
import org.noMoon.ArtificalSociety.career.DO.Career;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;

/**
 * Created by noMoon on 2015-09-10.
 */
public class CareerServiceImpl implements CareerService {
    CareerMapper careerMapper;
    public void insertNewCareer(CareerDTO careerDTO) {
        Career careerDO=careerDTO.convertToCareerDO();
        careerMapper.insertSelective(careerDO);
        careerDTO.setId(careerDO.getId());
    }

    public void setCareerMapper(CareerMapper careerMapper) {
        this.careerMapper = careerMapper;
    }
}
