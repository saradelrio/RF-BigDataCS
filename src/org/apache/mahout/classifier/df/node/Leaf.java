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

import org.apache.mahout.classifier.df.data.Data;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.df.data.Instance;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents a Leaf node
 */
public class Leaf extends Node {
  private static final double EPSILON = 1.0e-6;

  private double label;
  
  /**
   * voting final
   */
  protected double leafWeight = 0.0;
  
  protected double instancesLeaf = 0.0;
  
  protected double instanceClassLeaf = 0.0;
  
  Leaf() { }
  
  public Leaf(double label) {
    this.label = label;
  }
  
  public void setLeafWeight(double leafWeight){
	this.leafWeight = leafWeight; 
  }
  
  public void setInstancesLeaf(double instancesLeaf){
	this.instancesLeaf = instancesLeaf; 
  }
  
  public void setInstanceClassLeaf(double instanceClassLeaf){
	this.instanceClassLeaf = instanceClassLeaf; 
  }  
  
  public double getLeafWeight(){
    return this.leafWeight; 
  }
	  
  public double getInstancesLeaf(){
    return this.instancesLeaf; 
  }
	  
  public double getInstanceClassLeaf(){
    return this.instanceClassLeaf; 
  }  
  
  @Override
  public double[] classify(Dataset dataset, Instance instance) {
	double[] nodeWeight = null;
	nodeWeight = new double[dataset.nblabels()];
	Arrays.fill(nodeWeight, 0.0);
	nodeWeight[(int)label] = leafWeight;	
    return nodeWeight;
  }
  
  @Override
  public long maxDepth() {
    return 1;
  }
  
  @Override
  public long nbNodes() {
    return 1;
  }
  
  @Override
  protected Type getType() {
    return Type.LEAF;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Leaf)) {
      return false;
    }
    
    Leaf leaf = (Leaf) obj;
    
    return Math.abs(label - leaf.label) < EPSILON;
  }
  
  @Override
  public int hashCode() {
    long bits = Double.doubleToLongBits(label);
    return (int)(bits ^ (bits >>> 32));
  }
  
  @Override
  protected String getString() {
    return "";
  }
  
  @Override
  public void readFields(DataInput in) throws IOException {
    label = in.readDouble();
    leafWeight = in.readDouble();
  }
  
  @Override
  protected void writeNode(DataOutput out) throws IOException {
    out.writeDouble(label);
    out.writeDouble(leafWeight);
  }
}
