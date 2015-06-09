/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.classifier.df.node;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.classifier.df.data.Data;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.df.data.Instance;
import org.apache.mahout.math.Arrays;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Represents an abstract node of a decision tree
 */
public abstract class Node implements Writable {
  
  protected enum Type {
    LEAF,
    NUMERICAL,
    CATEGORICAL
  }
	     
  /**
   * predicts the label for the instance
   * 
   * @return -1 if the label cannot be predicted
   */  
  public abstract double [] classify(Dataset dataset, Instance instance);
  
  /**
   * @return the total number of nodes of the tree
   */
  public abstract long nbNodes();
  
  /**
   * @return the maximum depth of the tree
   */
  public abstract long maxDepth();
  
  protected abstract Type getType();
  
  public static Node read(DataInput in) throws IOException {
    Type type = Type.values()[in.readInt()];
    Node node;
    
    switch (type) {
      case LEAF:
        node = new Leaf();
        break;
      case NUMERICAL:
        node = new NumericalNode();
        break;
      case CATEGORICAL:
        node = new CategoricalNode();
        break;
      default:
        throw new IllegalStateException("This implementation is not currently supported");
    }
    
    node.readFields(in);
    
    return node;
  }
  
  @Override
  public final String toString() {
    return getType() + ":" + getString() + ';';
  }
  
  protected abstract String getString();
  
  @Override
  public final void write(DataOutput out) throws IOException {
    out.writeInt(getType().ordinal());
    writeNode(out);
  }
  
  protected abstract void writeNode(DataOutput out) throws IOException;
  
  public void computeLeafWeights() {
	// Node is a leaf
	if (this instanceof Leaf) {
      double instanceClassLeaf = ((Leaf)this).getInstanceClassLeaf();
      double instancesLeaf = ((Leaf)this).getInstancesLeaf();
	  double leafWeight = instanceClassLeaf/instancesLeaf;
      ((Leaf)this).setLeafWeight(leafWeight);
	} 
    else if (this instanceof CategoricalNode) {
	  // For nominal attributes
	  for (int i = 0; i < ((CategoricalNode)this).getChilds().length; i++) {
	    ((CategoricalNode)this).getChilds()[i].computeLeafWeights();
	  }
	  
	} else {
	  // For numeric attributes
	  ((NumericalNode)this).getLoChild().computeLeafWeights();
	  ((NumericalNode)this).getHichild().computeLeafWeights();
    }
  }
  
  public void computeLeafMeasures (Data data, Instance instance, double [] cWeights) {	
    Dataset dataset = data.getDataset();
	// Node is not a leaf
	if (this instanceof CategoricalNode || this instanceof NumericalNode) {	      
	  if (this instanceof CategoricalNode) {
	  // For nominal attributes            	  	    		    		      	      	      
	    int index=-1;
		double [] values = ((CategoricalNode)this).getValues();
		boolean found = false;
		for (int i=0 ; i<values.length && !found; i++){
		  if (instance.get(((CategoricalNode)this).getAttr()) == values[i]){
		    found = true;
		    index = i;
		  }
		}	      	      
		if(index==-1){		    	
	      for (int i = 0; i < ((CategoricalNode)this).getChilds().length; i++) {
		    ((CategoricalNode)this).getChilds()[i].computeLeafMeasures(data,instance,cWeights);
		  }		    	
		}else{	
		  ((CategoricalNode)this).getChilds()[index].computeLeafMeasures(data,instance,cWeights);
		}	    	   
	  } else {
	  // For numeric attributes		  		 
	    if (instance.get(((NumericalNode)this).getAttr()) < ((NumericalNode)this).getSplit()) {
		  ((NumericalNode)this).getLoChild().computeLeafMeasures(data,instance,cWeights);	
	    } else {
	      ((NumericalNode)this).getHichild().computeLeafMeasures(data,instance,cWeights);	
	    }
	  }
	}
	// Node is a leaf
	if (this instanceof Leaf) {      
	  ((Leaf)this).setInstancesLeaf(((Leaf)this).getInstancesLeaf()+1);
      ((Leaf)this).setInstanceClassLeaf(((Leaf)this).getInstanceClassLeaf() + cWeights[(int) dataset.getLabel(instance)]);
	}
  }
  
}