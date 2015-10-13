package org.noMoon.ArtificalSociety.career.services.impl;

import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.career.DAO.CareerMapper;
import org.noMoon.ArtificalSociety.career.DAO.WorkplaceMapper;
import org.noMoon.ArtificalSociety.career.DO.Career;
import org.noMoon.ArtificalSociety.career.DO.Workplace;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noMoon on 2015-09-10.
 */
public class CareerServiceImpl implements CareerService {

    CareerMapper careerMapper;
    WorkplaceMapper workplaceMapper;
    InstitutionService institutionService;

    public void insertNewCareer(CareerDTO careerDTO) {
        Career careerDO = careerDTO.convertToCareerDO();
        careerMapper.insertSelective(careerDO);
        careerDTO.setId(careerDO.getId());
    }

    public void loadCareerAndWorkplace(String careerConfigFilepath, String workplaceConfigFilepath, String societyId) {
        try {
            loadCareers(careerConfigFilepath, societyId);
            loadWorkplace(workplaceConfigFilepath,societyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadWorkplace(String filepath, String societyId) throws Exception {
        // Load the list of workplaces and related info (careers involved, city, etc.) from the XML file.
        // param filepath: The string of the file name, assuming the file is in the root directory of this project.
        // returns: arraylist containing arraylists for each of the workplace's information
        File file = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbFactory.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        // ----------------------------------------------------------------------------------------------------
        // Get all workplace nodes.
        // ----------------------------------------------------------------------------------------------------
        NodeList demoList = doc.getElementsByTagName("place");

        int i, d;
        String tmp;
        String tmpTitle = "";
        String workplaceInstitution = "";

        for (d = 0; d < demoList.getLength(); d++) {

            Workplace workplace = new Workplace();

            workplace.setSocietyId(societyId);

            // --------------------------------------------------
            // Get section root.
            // --------------------------------------------------
            Node demoNode = demoList.item(d);                                        // Get demographics element as Node.
            Element demoElement = (Element) demoNode;                                // Convert to Element.

            NodeList CarInfo;

            // ----- Title -----
            CarInfo = demoElement.getElementsByTagName("title");        // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                workplaceInstitution = demoRaceCatElement.getAttribute("inst");        // Get attribute for institution referencing (which is performed below).
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                tmpTitle = nodeList.item(0).getNodeValue(); // String of workplace title.
                workplace.setTitle(tmpTitle);            // Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- City -----
            CarInfo = demoElement.getElementsByTagName("city");            // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                workplace.setCity(nodeList.item(0).getNodeValue());        // Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Career IDs In This Workplace -----
            CarInfo = demoElement.getElementsByTagName("careers");    // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();


                workplace.setCareers(nodeList.item(0).getNodeValue());        // Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Year Workplace Started -----
            CarInfo = demoElement.getElementsByTagName("yearStarted");    // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                workplace.setStartingYear(Integer.parseInt(nodeList.item(0).getNodeValue()));            // Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Year Workplace Ended -----
            CarInfo = demoElement.getElementsByTagName("yearEnded");    // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                tmp = nodeList.item(0).getNodeValue();

                // Determine actual endYear
                if (tmp.equals("-")) {
                    // If a "-" is given in file, then school is currently running, so set endYear as society's current year.
                    workplace.setEndingYear(Configuration.MaxYear);    // Add this school info to the array for this school.
                } else {
                    // Parse int year given in file.
                    workplace.setEndingYear(Integer.parseInt(nodeList.item(0).getNodeValue()));            // Add this school info to the array for this school.
                } // end if (checking if endYear == "-")
            } // end i (loop through race elements)

            // Link workplace to institution.
            if (!workplaceInstitution.isEmpty()) {
                linkWorkplaceToInstitution(workplace, tmpTitle, societyId);
            } // end if (workplace is linked to an institution (school, temple, etc.))

            // ----- Institution Ref (it will be an ID reference to the institution it represents, or "" if not applicable) -----

            workplaceMapper.insertSelective(workplace);
            // Add this career array to large array.
            //DebugTools.printArray(Career);
            updateCareerWorkplace(workplace);

        } // end d (demographics loop)

    }

    public void updateCareerWorkplace(Workplace workplace) {
        String[] careerIds = workplace.getCareers().split(",");
        for (String careerId : careerIds) {
            Career query = new Career();
            query.setSocietyId(workplace.getSocietyId());
            query.setCareerId(careerId);
            List<Career> result = careerMapper.selectByCareerId(query);
            if (result.size() == 1) {
                Career updateDO = result.get(0);
                updateDO.setWorkplaceId(workplace.getId());
                careerMapper.updateByPrimaryKeySelective(updateDO);
            } else {
                System.err.println("Society id:" + workplace.getSocietyId() + " and careerId:" + careerId + "return duplicate records");
            }
        }
    }

    public void linkWorkplaceToInstitution(Workplace workplace, String title, String societyId) {
        List<Institution> result = institutionService.selectInstitutionByTitle(societyId, title);
        String instIds = "";
        for (Institution inst : result) {
            instIds += String.valueOf(inst.getId()) + ",";
        }
        if (instIds.length() > 0) {
            instIds=instIds.substring(0, instIds.length() - 1);
        }
        workplace.setInstitutionRef(instIds);
    }

    public void loadCareers(String filepath, String societyId) throws Exception {
        // Load the list of careers and related info (salary, etc.) from the XML file.
        // param filepath: The string of the file name, assuming the file is in the root directory of this project.
        // returns: arraylist containing arraylists for each of the career's information
        File file = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbFactory.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        // ----------------------------------------------------------------------------------------------------
        // Get all career nodes.
        // ----------------------------------------------------------------------------------------------------
        NodeList demoList = doc.getElementsByTagName("career");

        int i, d;

        NodeList CarInfo;
        Element demoRaceCatElement;
        NodeList nodeList;

        for (d = 0; d < demoList.getLength(); d++) {

            CareerDTO careerDTO = new CareerDTO();

            careerDTO.setSocietyId(societyId);

            // --------------------------------------------------
            // Get section root.
            // --------------------------------------------------
            Node demoNode = demoList.item(d);                                        // Get demographics element as Node.
            Element demoElement = (Element) demoNode;                                // Convert to Element.

            // ----- ID -----
            CarInfo = demoElement.getElementsByTagName("id");            // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setCareerId(nodeList.item(0).getNodeValue());
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Title -----
            CarInfo = demoElement.getElementsByTagName("title");        // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setTitle(nodeList.item(0).getNodeValue());
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Salary Mean -----
            CarInfo = demoElement.getElementsByTagName("salary_mean");    // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setSalaryMean(Double.parseDouble(nodeList.item(0).getNodeValue()));
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Num Percent -----
            CarInfo = demoElement.getElementsByTagName("num_percent");    // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setNumPercnet(Double.parseDouble(nodeList.item(0).getNodeValue()));
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Required Education -----
            CarInfo = demoElement.getElementsByTagName("education");    // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setEducation(nodeList.item(0).getNodeValue());
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Required Num Years In P.S. School -----
            CarInfo = demoElement.getElementsByTagName("num_year_post_secondary");    // Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element) CarInfo.item(i);    // Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setNumYearPostSecondary(Integer.parseInt(nodeList.item(0).getNodeValue()));
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Traits -----
            NodeList traitList = demoElement.getElementsByTagName("trait");            // Extract component as NodeList.
            HashMap<String, List<String>> traitMap = new HashMap<String, List<String>>();

            for (i = 0; i < traitList.getLength(); i++) {
                Element trait = (Element) traitList.item(i);
                String traitType = trait.getAttribute("type");                        // Get attribute "type".
                String traitTypeValue = trait.getFirstChild().getNodeValue();        // Get value.
                String traitReq = "|";
                if (trait.hasAttribute("req")) {
                    traitReq += trait.getAttribute("req");                    // Get attribute "req".
                } else {
                    traitReq = "";
                }// end if (check if club has "req" attribute)
                // Add both the type and value to a string array and add that array to the career's arraylist.
                if (traitMap.containsKey(traitType)) {
                    List<String> traitValueList = traitMap.get(traitType);
                    traitValueList.add(traitTypeValue + traitReq);
                    traitMap.put(traitType, traitValueList);
                } else {
                    List<String> traitValueList = new ArrayList<String>();
                    traitValueList.add(traitType + traitReq);
                    traitMap.put(traitType, traitValueList);
                }
            } // end i (loop through elements)
            careerDTO.setTrait(traitMap);

            careerMapper.insertSelective(careerDTO.convertToCareerDO());

        } // end d (demographics loop)

    } // end loadCareers()

    public List<Workplace> listWorkplaceWithSocietyId(String societyId) {
        Workplace query=new Workplace();
        query.setSocietyId(societyId);
        return workplaceMapper.listWorkplaceWithSocietyId(query);
    }

    public void setCareerMapper(CareerMapper careerMapper) {
        this.careerMapper = careerMapper;
    }

    public void setWorkplaceMapper(WorkplaceMapper workplaceMapper) {
        this.workplaceMapper = workplaceMapper;
    }

    public void setInstitutionService(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }
}
