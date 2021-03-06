/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.datacontainers.matrix;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.opengamma.maths.datacontainers.ExprEnum;
import com.opengamma.maths.datacontainers.matrix.OGRealDenseMatrix;
import com.opengamma.maths.exceptions.MathsExceptionIllegalArgument;
import com.opengamma.maths.exceptions.MathsExceptionNullPointer;
import com.opengamma.maths.helpers.DenseMemoryManipulation;
import com.opengamma.maths.helpers.FuzzyEquals;

/**
 * Tests the OGRealDenseMatrix Class
 */
public class OGRealDenseMatrixTest {

  double[][] data4x3 = new double[][] { { 1.00, 2.00, 3.00 }, { 4.00, 5.00, 6.00 }, { 7.00, 8.00, 9.00 }, { 10.00, 11.00, 12.00 } };
  double[] data4x3unwound = new double[] { 1.00, 4.00, 7.00, 10.00, 2.00, 5.00, 8.00, 11.00, 3.00, 6.00, 9.00, 12.00 };
  OGRealDenseMatrix getRow = new OGRealDenseMatrix(new double[] { 7, 8, 9 }, 1, 3);
  OGRealDenseMatrix getCol = new OGRealDenseMatrix(new double[] { 2, 5, 8, 11 }, 4, 1);

  OGRealDenseMatrix defaultVal = new OGRealDenseMatrix(data4x3);
  OGRealDenseMatrix same = new OGRealDenseMatrix(data4x3);
  OGRealDenseMatrix diffrows = new OGRealDenseMatrix(data4x3unwound, 3, 4);
  OGRealDenseMatrix diffcols = new OGRealDenseMatrix(new double[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 4, 2);
  OGRealDiagonalMatrix diffclass = new OGRealDiagonalMatrix(1);
  OGRealDenseMatrix diffnumber = new OGRealDenseMatrix(new double[][] { { 1337.00, 2.00, 3.00 }, { 4.00, 5.00, 6.00 }, { 7.00, 8.00, 9.00 }, { 10.00, 11.00, 12.00 } });
  OGRealDenseMatrix withindiffnumber = new OGRealDenseMatrix(new double[][] { { 1.00 + FuzzyEquals.getDefaultTolerance() * 0.9, 2.00, 3.00 }, { 4.00, 5.00, 6.00 }, { 7.00, 8.00, 9.00 },
    { 10.00, 11.00, 12.00 } });
  OGComplexDenseMatrix diffdomain = new OGComplexDenseMatrix(data4x3);

  // sending in null ptr double[][] constructor
  @Test(expectedExceptions = MathsExceptionNullPointer.class)
  public void testDoublePtrPtrConstructorNullPtrTest() {
    double[][] tmp = null;
    new OGRealDenseMatrix(tmp);
  }

  // sending in embedded null ptr double[][] constructor
  @Test(expectedExceptions = MathsExceptionNullPointer.class)
  public void testDoublePtrPtrConstructorOKThenNullPtrTest() {
    double[][] tmp = new double[2][1];
    tmp[0] = new double[] { 1 };
    tmp[1] = null;
    new OGRealDenseMatrix(tmp);
  }

  // sending in null ptr double[] constructor  
  @Test(expectedExceptions = MathsExceptionNullPointer.class)
  public void testDoublePtrConstructorNullPtrTest() {
    double[] tmp = null;
    new OGRealDenseMatrix(tmp, 1, 1);
  }

  // sending in ragged[][] double[][] constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoublePtrPtrConstructorRaggedTest() {
    double[][] tmp = new double[2][];
    tmp[0] = new double[] { 1 };
    tmp[1] = new double[] { 1, 2, 3 };
    new OGRealDenseMatrix(tmp);
  }

  // sending in ok double[][] constructor
  @Test
  public void testDoublePtrPtrConstructorInternalDataTest() {
    OGRealDenseMatrix D = new OGRealDenseMatrix(data4x3);
    assertTrue(D.getClass() == OGRealDenseMatrix.class);
    assertTrue(Arrays.equals(D.getData(), DenseMemoryManipulation.convertRowMajorDoublePointerToColumnMajorSinglePointer(data4x3)));
    assertTrue(D.getRows() == 4);
    assertTrue(D.getCols() == 3);
  }

  //sending in bad rows double[] constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoublePtrConstructorBadRowsDataTest() {
    new OGRealDenseMatrix(data4x3unwound, -1, 3);
  }

  //sending in bad rows double[] constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoublePtrConstructorBadColsDataTest() {
    new OGRealDenseMatrix(data4x3unwound, 3, -1);
  }

  //sending in bad data count double[] constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoublePtrConstructorDataCountDataTest() {
    new OGRealDenseMatrix(data4x3unwound, 3, 17);
  }

  // sending in ok double[] constructor
  @Test
  public void testDoublePtrConstructorInternalDataTest() {
    OGRealDenseMatrix D = new OGRealDenseMatrix(data4x3unwound, 4, 3);
    assertTrue(D.getClass() == OGRealDenseMatrix.class);
    assertTrue(Arrays.equals(D.getData(), DenseMemoryManipulation.convertRowMajorDoublePointerToColumnMajorSinglePointer(data4x3)));
    assertTrue(D.getRows() == 4);
    assertTrue(D.getCols() == 3);
  }

  // sending in single double for a 1x1 element array
  @Test
  public void testDoubleConstructorInternalDataTest() {
    OGRealDenseMatrix D = new OGRealDenseMatrix(7.);
    assertTrue(D.getClass() == OGRealDenseMatrix.class);
    assertTrue(Arrays.equals(D.getData(), new double[] { 7. }));
    assertTrue(D.getRows() == 1);
    assertTrue(D.getCols() == 1);
  }

  // sending in single double for a 1xn element array
  @Test
  public void testDoubleRowPtrConstructorInternalDataTest() {
    double[] data = new double[] { 1, 2, 3, 4 };
    OGRealDenseMatrix D = new OGRealDenseMatrix(data);
    assertTrue(D.getClass() == OGRealDenseMatrix.class);
    assertTrue(Arrays.equals(D.getData(), data));
    assertTrue(D.getRows() == 1);
    assertTrue(D.getCols() == 4);
  }

  // test get rows
  @Test
  public void testGetRowsTest() {
    OGRealDenseMatrix D = new OGRealDenseMatrix(data4x3unwound, 4, 3);
    assertTrue(D.getRows() == 4);
  }

  // test get cols
  @Test
  public void testGetColumnsTest() {
    OGRealDenseMatrix D = new OGRealDenseMatrix(data4x3unwound, 4, 3);
    assertTrue(D.getCols() == 3);
  }

  @Test
  public void testGetTypeEnum() {
    OGRealDenseMatrix D = new OGRealDenseMatrix(data4x3unwound, 4, 3);
    assertTrue(D.getType().equals(ExprEnum.OGRealDenseMatrix));
  }

  @Test
  public void toStringTest() {
    OGRealDenseMatrix D = new OGRealDenseMatrix(data4x3unwound, 4, 3);
    D.toString();
  }

  @Test
  public void testHashCode() {
    assertTrue(defaultVal.hashCode() == same.hashCode());
    assertFalse(defaultVal.hashCode() == diffnumber.hashCode());
  }

  @Test
  public void testEquals() {
    assertTrue(defaultVal.equals(defaultVal));
    assertTrue(defaultVal.equals(same));
    assertFalse(defaultVal.equals(diffrows));
    assertFalse(defaultVal.equals(diffcols));
    assertFalse(defaultVal.equals(diffclass));
    assertFalse(defaultVal.equals(diffnumber));
  }

  @Test
  public void testFuzzyEquals() {
    assertTrue(defaultVal.fuzzyequals(defaultVal));
    assertTrue(defaultVal.fuzzyequals(same));
    assertTrue(defaultVal.fuzzyequals(withindiffnumber));
    assertFalse(defaultVal.fuzzyequals(diffrows));
    assertFalse(defaultVal.fuzzyequals(diffcols));
    assertFalse(defaultVal.fuzzyequals(diffclass));
    assertFalse(defaultVal.fuzzyequals(diffnumber));
    assertFalse(defaultVal.fuzzyequals(diffdomain));
  }

  @Test
  public void testMathsEquals() {
    assertTrue(defaultVal.mathsequals(defaultVal));
    assertTrue(defaultVal.mathsequals(same));
    assertTrue(defaultVal.mathsequals(withindiffnumber));
    assertFalse(defaultVal.mathsequals(diffclass));
    assertFalse(defaultVal.mathsequals(diffrows));
    assertFalse(defaultVal.mathsequals(diffcols));
    assertFalse(defaultVal.mathsequals(diffnumber));
    assertTrue(defaultVal.mathsequals(diffdomain));
  }

}
