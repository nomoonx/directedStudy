package thesis_network_growth;

import java.util.ArrayList;

public class CreateJUNGGraph {
	/*
	public static Graph<String,String> createJUNGGraph () {
		
		Graph<String, String> graph = new DirectedSparseGraph<String, String>();
		
		graph = populateGraph(graph);
		
		
		return graph;
		
	} // end createJUNGGraph()

	
	private static Graph<String, String> populateGraph (Graph<String, String> g) {
		int i, j;
		int f_index = 0;
		int numPersons = ArtificialSociety.getSociety().size();
		//ArrayList<String[]> allConnections = new ArrayList<String[]>();
		Person person;
		Friendship friendship;
		
		for (i = 0; i < numPersons; i++) {
			//g.addVertex(String.valueOf(i));
			person = ArtificialSociety.getPersonByIndex(i);
			g.addVertex(String.valueOf(person.getID()));	// Add person as node.
			
			
			for (j = 0; j < person.getFriends().size(); j++) {
				friendship = person.getFriends().get(j);
				//allConnections.add(new String[] { String.valueOf(person.getID()), String.valueOf(friendship.getFriendID()) });
				g.addEdge(String.valueOf(f_index), String.valueOf(person.getID()), String.valueOf(friendship.getFriendID()));
				
				f_index++; // Used as index for friendships.
				
			} // end for j (friends of person)
			
		} // end for i (nodes)

		
		

		//graph = g;
		return g;
	}
	
	
	*/
} // end CreateJUNGGraph class
