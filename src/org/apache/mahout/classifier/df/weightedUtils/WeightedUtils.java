/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.mahout.classifier.df.weightedUtils;

public class WeightedUtils {
	
 /**
  * Normalizes the doubles in the array by their sum.
  *
  * @param doubles the array of double
  * @exception IllegalArgumentException if sum is Zero or NaN
  */
  public static void normalize(double[] doubles) {

  double sum = 0;
    for (int i = 0; i < doubles.length; i++) {
	  sum += doubles[i];
	}
	normalize(doubles, sum);
  }

 /**
  * Normalizes the doubles in the array using the given value.
  *
  * @param doubles the array of double
  * @param sum the value by which the doubles are to be normalized
  * @exception IllegalArgumentException if sum is zero or NaN
  */
  public static void normalize(double[] doubles, double sum) {
    if (Double.isNaN(sum)) {
	  throw new IllegalArgumentException("Can't normalize array. Sum is NaN.");
	}
	if (sum == 0) {
	// Maybe this should just be a return.
	  throw new IllegalArgumentException("Can't normalize array. Sum is zero.");
	}
	for (int i = 0; i < doubles.length; i++) {
	  doubles[i] /= sum;
	}
  }
}