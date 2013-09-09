package com.opengamma.longdog.datacontainers.matrix;

import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.opengamma.longdog.datacontainers.ExprTypeEnum;
import com.opengamma.longdog.exceptions.MathsExceptionIllegalArgument;
import com.opengamma.longdog.exceptions.MathsExceptionNullPointer;

public class OGRealDiagonalMatrixTest {

  double[] data4x3diagd = new double[] { 1, 2, 3 };
  double[][] data4x3full = new double[][] { { 1.00, 0.00, 0.00 }, { 0.00, 2.00, 0.00 }, { 0.00, 0.00, 3.00 }, { 0.00, 0.00, 0.00 } };

  // sending in null ptr double[] constructor
  @Test(expectedExceptions = MathsExceptionNullPointer.class)
  public void testDoublePtrConstructorNullPtrTest() {
    double[] tmp = null;
    new OGRealDiagonalMatrix(tmp);
  }

  // sending in null ptr double[], int, int constructor
  @Test(expectedExceptions = MathsExceptionNullPointer.class)
  public void testDoublePtrIntIntConstructorNullPtrTest() {
    double[] tmp = null;
    new OGRealDiagonalMatrix(tmp, 1, 1);
  }

  // sending in ok double[] constructor
  @Test
  public void testDoublePtrConstructorInternalDataTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(data4x3diagd);
    assertTrue(D.getClass() == OGRealDiagonalMatrix.class);
    assertTrue(Arrays.equals(D.getData(), data4x3diagd));
    assertTrue(D.getRows() == 3);
    assertTrue(D.getCols() == 3);
  }

  // sending in bad rows double[], int, int constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoublePtrIntIntConstructorBadRowsDataTest() {
    new OGRealDiagonalMatrix(data4x3diagd, -1, 3);
  }

  // sending in bad rows double[], int, int  constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoublePtrIntIntConstructorBadColsDataTest() {
    new OGRealDiagonalMatrix(data4x3diagd, 3, -1);
  }

  // sending in ok double[], int, int constructor
  @Test
  public void testDoublePtrIntIntConstructorInternalDataTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(data4x3diagd, 16, 32);
    assertTrue(D.getClass() == OGRealDiagonalMatrix.class);
    assertTrue(Arrays.equals(D.getData(), data4x3diagd));
    assertTrue(D.getRows() == 16);
    assertTrue(D.getCols() == 32);
  }

  // sending in bad rows double, int, int constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoubleIntIntConstructorBadRowsDataTest() {
    new OGRealDiagonalMatrix(1, -1, 3);
  }

  // sending in bad rows double, int, int  constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testDoubleIntIntConstructorBadColsDataTest() {
    new OGRealDiagonalMatrix(1, 3, -1);
  }

  // sending in ok double, int, int  constructor
  @Test
  public void testDoubleIntIntConstructorInternalDataTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(3, 13, 37);
    assertTrue(D.getClass() == OGRealDiagonalMatrix.class);
    assertTrue(Arrays.equals(D.getData(), new double[] { 3 }));
    assertTrue(D.getRows() == 13);
    assertTrue(D.getCols() == 37);
  }

  // sending in ok double constructor
  @Test
  public void testDoubleConstructorInternalDataTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(3.d);
    assertTrue(D.getClass() == OGRealDiagonalMatrix.class);
    assertTrue(Arrays.equals(D.getData(), new double[] { 3 }));
    assertTrue(D.getRows() == 1);
    assertTrue(D.getCols() == 1);
  }

  // sending in ok int constructor
  @Test
  public void testIntConstructorInternalDataTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix((int) 3);
    assertTrue(D.getClass() == OGRealDiagonalMatrix.class);
    assertTrue(Arrays.equals(D.getData(), new double[] { 3 }));
    assertTrue(D.getRows() == 1);
    assertTrue(D.getCols() == 1);
  }

  // test get rows
  @Test
  public void testGetRowsTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(data4x3diagd, 4, 3);
    assertTrue(D.getRows() == 4);
  }

  // test get cols
  @Test
  public void testGetNumColumnsTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(data4x3diagd, 4, 3);
    assertTrue(D.getCols() == 3);
  }

  @Test
  public void testGetTypeEnum() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(data4x3diagd, 4, 3);
    assertTrue(D.getType().equals(ExprTypeEnum.OGRealDiagonalMatrix));
  }

  @Test
  public void toStringTest() {
    OGRealDiagonalMatrix D = new OGRealDiagonalMatrix(data4x3diagd, 4, 3);
    D.toString();
  }
  
}