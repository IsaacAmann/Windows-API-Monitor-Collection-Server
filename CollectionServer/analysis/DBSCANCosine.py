import sys, getopt
import seaborn as sns
import pandas as panda

from sklearn import metrics
from sklearn.cluster import DBSCAN

def main():
	outputFile = "clusterOut.csv"
	inputFile = ""
	epsilon = 0
	minPoints = 0
	
	
	opts, args = getopt.getopt(sys.argv[1:], "i:m:e:")
	for o, a in opts:
		if o == "-i":
			inputFile = a
		elif o == "-m":
			minPoints = int(a)
		elif o == "-e":
			epsilon = float(a)
	
	dataset = panda.read_csv(inputFile)
	
	db = DBSCAN(eps=epsilon, min_samples=minPoints, metric="cosine").fit(dataset);
	
	
	for x in db.labels_:
		print(x)
		
	outData = panda.DataFrame(data=db.labels_)
	
	outData.to_csv(outputFile, index=False, header=False)
		
	
	
	
	
		
		


if __name__ == "__main__":
    main()
