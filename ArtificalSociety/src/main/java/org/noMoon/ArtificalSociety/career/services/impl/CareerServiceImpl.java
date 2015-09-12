package org.noMoon.ArtificalSociety.career.services.impl;

import org.noMoon.ArtificalSociety.NetworkGenerator.Configuration;
import org.noMoon.ArtificalSociety.career.DAO.CareerMapper;
import org.noMoon.ArtificalSociety.career.DO.Career;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;
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

    public void insertNewCareer(CareerDTO careerDTO) {
        Career careerDO = careerDTO.convertToCareerDO();
        careerMapper.insertSelective(careerDO);
        careerDTO.setId(careerDO.getId());
    }

    public void loadCareerAndWorkplace(String careerConfigFilepath, String workplaceConfigFilepath) {
        try{
            loadCareers(careerConfigFilepath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadWorkplace(String filepath) throws Exception{

    }

    public void loadCareers (String filepath) throws Exception {
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

            CareerDTO careerDTO=new CareerDTO();

            careerDTO.setSocietyId(Configuration.Society_Id);

            // --------------------------------------------------
            // Get section root.
            // --------------------------------------------------
            Node demoNode = demoList.item(d);										// Get demographics element as Node.
            Element demoElement = (Element)demoNode;								// Convert to Element.

            // ----- ID -----
            CarInfo = demoElement.getElementsByTagName("id");			// Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setCareerId(nodeList.item(0).getNodeValue());
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Title -----
            CarInfo = demoElement.getElementsByTagName("title");		// Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setTitle(nodeList.item(0).getNodeValue());
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Salary Mean -----
            CarInfo = demoElement.getElementsByTagName("salary_mean");	// Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setSalaryMean(Double.parseDouble(nodeList.item(0).getNodeValue()));
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Num Percent -----
            CarInfo = demoElement.getElementsByTagName("num_percent");	// Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setNumPercnet(Double.parseDouble(nodeList.item(0).getNodeValue()));
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Required Education -----
            CarInfo = demoElement.getElementsByTagName("education");	// Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setEducation(nodeList.item(0).getNodeValue());
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Required Num Years In P.S. School -----
            CarInfo = demoElement.getElementsByTagName("num_year_post_secondary");	// Extract career component as NodeList.
            for (i = 0; i < CarInfo.getLength(); i++) {
                demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
                nodeList = demoRaceCatElement.getChildNodes();
                careerDTO.setNumYearPostSecondary(Integer.parseInt(nodeList.item(0).getNodeValue()));
//                Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
            } // end i (loop through race elements)

            // ----- Traits -----
            NodeList traitList = demoElement.getElementsByTagName("trait");			// Extract component as NodeList.
            HashMap<String,List<String>> traitMap=new HashMap<String, List<String>>();

            for (i = 0; i < traitList.getLength(); i++) {
                Element trait = (Element)traitList.item(i);
                String traitType = trait.getAttribute("type");						// Get attribute "type".
                String traitTypeValue = trait.getFirstChild().getNodeValue();		// Get value.
                String traitReq="|";
                if (trait.hasAttribute("req")) {
                    traitReq += trait.getAttribute("req");					// Get attribute "req".
                } else{
                    traitReq="";
                }// end if (check if club has "req" attribute)
                // Add both the type and value to a string array and add that array to the career's arraylist.
                if(traitMap.containsKey(traitType)){
                    List<String> traitValueList=traitMap.get(traitType);
                    traitValueList.add(traitTypeValue+traitReq);
                    traitMap.put(traitType,traitValueList);
                }else{
                    List<String> traitValueList=new ArrayList<String>();
                    traitValueList.add(traitType+traitReq);
                    traitMap.put(traitType,traitValueList);
                }
            } // end i (loop through elements)
            careerDTO.setTrait(traitMap);

            careerMapper.insertSelective(careerDTO.convertToCareerDO());

        } // end d (demographics loop)

    } // end loadCareers()

    public void setCareerMapper(CareerMapper careerMapper) {
        this.careerMapper = careerMapper;
    }
}
