
# RF-BigDataCS: A cost-sensitive approach for Random Forest Partial MapReduce algorithm to deal with Imbalanced Big Data.

Inspired by the Mahout Random Forest Partial implementation we build a new Random Forest version that can be used to classify imbalanced big data. The Mahout Partial implementation (RF-BigData) is an algorithm that builds multiple trees for different portions of the data. This algorithm is divided into two different phases: the first phase is devoted to the creation of the model and the second phase is dedicated to the estimation of the classes associated with the dataset using the previous learned model. In the first phase, the Random Forest is built from the original training set following a MapReduce procedure. This process consists of three steps: Initial, Map and Final. When the building of the forest is finished, the classification phase is initiated to estimate the class associated to a data sample set. This process also consists of three steps: Initial, Map and Final. To adapt the cost-sensitive learning based version of Random Forest to the Mahout environment, we need to include the cost-sensitive operations into the basic Random Forest implementation.

Figure 1: A flowchart of how the building of the Random Forest is organized in the RF-BigDataCS algorithm.

<img src=http://sci2s.ugr.es/sites/default/files/files/TematicWebSites/BigData/rf_cs_big.png width=598 height=437 />

Figure 2: A flowchart of how the classifying step is organized in the RF-BigDataCS algorithm.

<img src=http://sci2s.ugr.es/sites/default/files/files/TematicWebSites/BigData/rf_cs_bigclas.png width=507 height=319 />

# References

S. Río, V. López, J.M. Benítez, F. Herrera. On the use of MapReduce for Imbalanced Big Data using Random Forest. Information Sciences 285 (2014) 112-137. doi: 10.1016/j.ins.2014.03.043

http://sci2s.ugr.es/sites/default/files/ficherosPublicaciones/1742_2014-delRio-INS.pdf

