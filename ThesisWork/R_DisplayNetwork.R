library(network)
library(statnet)
library(igraph)

readInNetwork1 <- function () {		# Using igraph.
	# Set current directory.
	setwd("C:/Users/Bryan/workspace/ThesisWork/Output")
	
	# Load in nodes and edges from file.
	nodeinfo <- read.table("nodeList.txt", head=T)
	myedges <- read.graph("edgeList.txt")


	require(igraph)
#	plot.igraph(myedges, vertex.size=6, vertex.frame.color=nodeinfo$Family)
	plot.igraph(myedges, vertex.size=6, vertex.frame.color='red')

}



readInNetwork1()
