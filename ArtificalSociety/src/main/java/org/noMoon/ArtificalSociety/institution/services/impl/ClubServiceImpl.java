package org.noMoon.ArtificalSociety.institution.services.impl;

import org.noMoon.ArtificalSociety.institution.DAO.ClubMapper;
import org.noMoon.ArtificalSociety.institution.DO.Club;
import org.noMoon.ArtificalSociety.institution.DTO.ClubDTO;
import org.noMoon.ArtificalSociety.institution.services.ClubService;

/**
 * Created by noMoon on 2015-09-08.
 */
public class ClubServiceImpl implements ClubService {

    ClubMapper clubMapper;

    public ClubDTO selectClubDTOByPk(Long id) {
        return new ClubDTO( clubMapper.selectByPrimaryKey(id));
    }

    public void loadClubsFromFile(String filePath) {
//        try {
//            File file = new File(filePath);
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbFactory.newDocumentBuilder();
//            Document doc = db.parse(file);
//            doc.getDocumentElement().normalize();
//
//            // ----------------------------------------------------------------------------------------------------
//            // Get all club nodes.
//            // ----------------------------------------------------------------------------------------------------
//            NodeList clubList = doc.getElementsByTagName(InstitutionEnum.CLUB.getElementName());
//
//            int i, d;
//            ArrayList<ArrayList<Object>> ClubsList = new ArrayList<ArrayList<Object>>();
//            ArrayList<Object> Club;
//            String tmp;
//
//
//
//
//            Element clubCategoryElement;
//            Element trait;
//
//            String traitType;
//            String traitTypeValue;
//            String traitReq;
//            NodeList traitList;
//            ArrayList<String[]> traitSet;
//            String[] traitPair;
//
//            for (d = 0; d < clubList.getLength(); d++) {
//
//                ClubDTO clubDTO = new ClubDTO();
//
//                // --------------------------------------------------
//                // Get section root.
//                // --------------------------------------------------
//                Node clubNode = clubList.item(d);                                    // Get element as Node.
//                Element clubElement = (Element) clubNode;                                // Convert to Element.
//                NodeList SchInfo;
//                NodeList nodeList;
//
//                // ----- ID -----
////                SchInfo = clubElement.getElementsByTagName("id");                // Extract component as NodeList.
////                for (i = 0; i < SchInfo.getLength(); i++) {
////                    clubCategoryElement = (Element) SchInfo.item(i);                // Convert to element.
////                    nodeList = clubCategoryElement.getChildNodes();
////                    clubDTO.
//////                    Club.add(nodeList.item(0).getNodeValue());                    // Add this info to the array for this element.
////                } // end i (loop through elements)
//
//                // ----- Title -----
//                SchInfo = clubElement.getElementsByTagName("title");            // Extract  component as NodeList.
//                for (i = 0; i < SchInfo.getLength(); i++) {
//                    clubCategoryElement = (Element) SchInfo.item(i);                // Convert to element.
//                    nodeList = clubCategoryElement.getChildNodes();
//                    clubDTO.setTitle(nodeList.item(0).getNodeValue());
////                    Club.add(nodeList.item(0).getNodeValue());                    // Add this info to the array for this element.
//                } // end i (loop through elements)
//
//                // ----- Type -----
//                SchInfo = clubElement.getElementsByTagName("type");                // Extract component as NodeList.
//                for (i = 0; i < SchInfo.getLength(); i++) {
//                    clubCategoryElement = (Element) SchInfo.item(i);                // Convert to element.
//                    nodeList = clubCategoryElement.getChildNodes();
//                    clubDTO.setType(nodeList.item(0).getNodeValue());
////                    Club.add(nodeList.item(0).getNodeValue());                    // Add this info to the array for this element.
//                } // end i (loop through elements)
//
//                // ----- Traits -----
//                traitList = clubElement.getElementsByTagName("trait");            // Extract component as NodeList.
//                traitSet = new ArrayList<String[]>();
//
//                for (i = 0; i < traitList.getLength(); i++) {
//                    trait = (Element) traitList.item(i);
//                    traitType = trait.getAttribute("type");                        // Get attribute "type".
//                    traitTypeValue = trait.getFirstChild().getNodeValue();        // Get value.
//                    if (trait.hasAttribute("req")) {
//                        traitReq = trait.getAttribute("req");                        // Get attribute "req".
//                    } else {
//                        traitReq = "";
//                    } // end if (check if club has "req" attribute)
//                    // Add both the type and value to a string array and add that array to the club's arraylist.
//                    traitPair = new String[]{traitType, traitTypeValue, traitReq};
//                    traitSet.add(traitPair);                                    // Add this info to the array for this element.
//                } // end i (loop through elements)
//                Club.add(traitSet);
//
//                // ----- City -----
//                SchInfo = clubElement.getElementsByTagName("city");                // Extract component as NodeList.
//                for (i = 0; i < SchInfo.getLength(); i++) {
//                    clubCategoryElement = (Element) SchInfo.item(i);                // Convert to element.
//                    nodeList = clubCategoryElement.getChildNodes();
//                    Club.add(nodeList.item(0).getNodeValue());                    // Add this info to the array for this element.
//                } // end i (loop through elements)
//
//                // ----- School Population -----
//                SchInfo = clubElement.getElementsByTagName("pop");                // Extract component as NodeList.
//                for (i = 0; i < SchInfo.getLength(); i++) {
//                    clubCategoryElement = (Element) SchInfo.item(i);                // Convert to element.
//                    nodeList = clubCategoryElement.getChildNodes();
//                    Club.add(nodeList.item(0).getNodeValue());                    // Add this info to the array for this element.
//                } // end i (loop through elements)
//
//                // ----- Year Started -----
//                SchInfo = clubElement.getElementsByTagName("yearStarted");        // Extract component as NodeList.
//                for (i = 0; i < SchInfo.getLength(); i++) {
//                    clubCategoryElement = (Element) SchInfo.item(i);                // Convert to element.
//                    nodeList = clubCategoryElement.getChildNodes();
//                    Club.add(nodeList.item(0).getNodeValue());                    // Add this info to the array for this element.
//                } // end i (loop through elements)
//
//                // ----- Year Ended -----
//                SchInfo = clubElement.getElementsByTagName("yearEnded");        // Extract component as NodeList.
//                for (i = 0; i < SchInfo.getLength(); i++) {
//                    clubCategoryElement = (Element) SchInfo.item(i);                // Convert to element.
//                    nodeList = clubCategoryElement.getChildNodes();
//                    tmp = nodeList.item(0).getNodeValue();
//
//                    // Determine actual endYear
//                    if (tmp.equals("-")) {
//                        // If a "-" is given in file, then school is currently running, so set endYear as society's current year.
//                        Club.add(String.valueOf(Configuration.SocietyYear));    // Add this info to the array for this element.
//                    } else {
//                        // Parse int year given in file.
//                        Club.add(nodeList.item(0).getNodeValue());                // Add this info to the array for this element.
//                    } // end if (checking if endYear == "-")
//
//
//                } // end i (loop through elements)
//
//                // Add this club array to large array.
//                ClubsList.add(Club);
//
//            } // end d (all clubs loop)
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public void insertClubDTO(ClubDTO clubDTO) {
        Club club=clubDTO.convertToClub();
        clubMapper.insertSelective(club);
        clubDTO.setId(club.getId());

    }

    public void setClubMapper(ClubMapper clubMapper) {
        this.clubMapper = clubMapper;
    }
}
