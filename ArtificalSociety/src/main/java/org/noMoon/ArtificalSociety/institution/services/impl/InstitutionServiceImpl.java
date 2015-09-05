package org.noMoon.ArtificalSociety.institution.services.impl;

import org.noMoon.ArtificalSociety.NetworkGenerator.Configuration;
import org.noMoon.ArtificalSociety.institution.DAO.InstitutionMapper;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.enums.InstitutionEnum;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by noMoon on 2015-09-02.
 */
public class InstitutionServiceImpl implements InstitutionService {

    InstitutionMapper institutionMapper;

    public void loadAllInstitutions(String elemSchoolConfFilepath, String psSchoolConfFilepath, String templeConfFilepath) {
        try {
            if (!StringUtils.isEmpty(elemSchoolConfFilepath)) {
                System.out.println("elementary school");
                loadFromFile(elemSchoolConfFilepath, InstitutionEnum.ELEMENTARY_SCHOOL);
            }
            if(!StringUtils.isEmpty(psSchoolConfFilepath)){
                loadFromFile(psSchoolConfFilepath,InstitutionEnum.POST_SECONDARY_SCHOOL);
            }
            if(!StringUtils.isEmpty(templeConfFilepath)){
                loadFromFile(templeConfFilepath,InstitutionEnum.TEMPLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadFromFile (String filepath, InstitutionEnum institutionEnum) throws Exception {
        // Load the list of schools and related info (title, p_attend, etc.) from the XML file. Note that p_attend is the probability in [0,1]
        // that a student will attend this school. The sum of all schools' p_attend should equal 1.0.
        // param filepath: The string of the file name, assuming the file is in the root directory of this project.
        // returns: arraylist containing arraylists for each of the career's information
        File file = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbFactory.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        // ----------------------------------------------------------------------------------------------------
        // Get all nodes.
        // ----------------------------------------------------------------------------------------------------
        NodeList demoList = doc.getElementsByTagName(institutionEnum.getElementName());

        int i, d;

        // For each of the main elements within the XML file.
        for (d = 0; d < demoList.getLength(); d++) {

            Institution inst=new Institution();
            inst.setSocietyId(Configuration.Society_Id);

            // --------------------------------------------------
            // Get section root.
            // --------------------------------------------------
            Node demoNode = demoList.item(d);							// Get main element as Node.
            Element demoElement = (Element)demoNode;					// Convert to Element.

            NodeList InstInfo;

            // ----- Title -----
            InstInfo = demoElement.getElementsByTagName("title");		// Extract component as NodeList.
            for (i = 0; i < InstInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                inst.setTitle(nodeList.item(0).getNodeValue());
//                InstitutionInfo.add(Institution_Name, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
            } // end i (loop through elements)

            // ----- Type -----
            inst.setInstitutionType(institutionEnum);
//            InstitutionInfo.add(Institution_Type, treeRootName);

            // ----- Subtype -----
            InstInfo = demoElement.getElementsByTagName("type");			// Extract component as NodeList.
            for (i = 0; i < InstInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                inst.setType(nodeList.item(0).getNodeValue());
//                InstitutionInfo.add(Institution_Subtype, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
            } // end i (loop through elements)

            // ----- City -----
            InstInfo = demoElement.getElementsByTagName("city");			// Extract component as NodeList.
            for (i = 0; i < InstInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                inst.setCity(nodeList.item(0).getNodeValue());
//                InstitutionInfo.add(Institution_City, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
            } // end i (loop through elements)

            // ----- Population -----
            InstInfo = demoElement.getElementsByTagName("pop");			// Extract component as NodeList.
            for (i = 0; i < InstInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                inst.setPopulation(Integer.parseInt(nodeList.item(0).getNodeValue()));
//                InstitutionInfo.add(Institution_Pop, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
            } // end i (loop through elements)

            // ----- Year Started -----
            InstInfo = demoElement.getElementsByTagName("yearStarted");	// Extract component as NodeList.
            for (i = 0; i < InstInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                inst.setStartingYear(Integer.parseInt(nodeList.item(0).getNodeValue()));
//                InstitutionInfo.add(Institution_StYr, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
            } // end i (loop through elements)

            // ----- Year Ended -----
            InstInfo = demoElement.getElementsByTagName("yearEnded");	// Extract component as NodeList.
            for (i = 0; i < InstInfo.getLength(); i++) {
                Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
                NodeList nodeList = demoRaceCatElement.getChildNodes();
                String tmp = nodeList.item(0).getNodeValue();

                // Determine actual endYear
                if (tmp.equals("-")) {
                    // If a "-" is given in file, then school is currently running, so set endYear as society's current year.
                    // EDIT: Rather than current year, use the MaxYear here. This is so in the simulation, they are initialized beyond the current year.
                    inst.setEndingYear(Configuration.MaxYear);
//                    InstitutionInfo.add(Institution_EndYr, String.valueOf(Configuration.MaxYear));	// Add this school info to the array for this school.
                } else {
                    // Parse int year given in file.
                    inst.setEndingYear(Integer.parseInt(nodeList.item(0).getNodeValue()));
//                    InstitutionInfo.add(Institution_EndYr, nodeList.item(0).getNodeValue());			// Add this school info to the array for this school.
                } // end if (checking if endYear == "-")


            } // end i (loop through race elements)

            institutionMapper.insertSelective(inst);
            System.out.println(inst.getId());
            // Add this school array to large array.
//            AllInstInSet.add(InstitutionInfo);

        } // end d (demographics loop)
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
