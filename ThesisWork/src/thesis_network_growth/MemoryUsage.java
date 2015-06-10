package thesis_network_growth;

public class MemoryUsage {
	
	
	public static long GetMemoryUsage () {
		
		// Run garbage collector to ensure all garbage is taken away first.
		System.gc();
		
		
		int mb = 1024*1024;
        
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
         
        //System.out.println("##### Heap utilization statistics [MB] #####");
         
        //Print used memory
        //System.out.println("Used Memory:"
        //    + (runtime.totalMemory() - runtime.freeMemory()) / mb);
 
        //Print free memory
        //System.out.println("Free Memory:"
        //    + runtime.freeMemory() / mb);
         
        //Print total available memory
        //System.out.println("Total Memory:" + runtime.totalMemory() / mb);
 
        //Print Maximum available memory
        //System.out.println("Max Memory:" + runtime.maxMemory() / mb);
        
        
        //System.out.println("*****************************************************");
        
        return (runtime.totalMemory() - runtime.freeMemory()) / mb;
	} // end GetMemoryUsage()
	
	
	public static void cleanup() {
		ArtificialSociety.DestroySociety();
		
		Configuration.DestroyConfiguration(); // No effect.
		
		GroupGenerator.DestroyGroups();
		
		// Begin garbage collection.
		System.gc();
	}
	

} // end MemoryUsage class
