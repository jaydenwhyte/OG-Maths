/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */

#include <iostream>
#include "expressionbase.hh"
#include "expression.hh"
#include "terminal.hh"
#include "exceptions.hh"

using namespace std;

namespace librdag
{

/*
 * OGExpr
 */

OGExpr::OGExpr()
{
  _args = nullptr;
  _regs = new RegContainer();
}

OGExpr::~OGExpr()
{
  if (_args != nullptr)
  {
    delete _args;
  }
  delete _regs;
}

const ArgContainer*
OGExpr::getArgs() const
{
  return _args;
}

size_t
OGExpr::getNArgs() const
{
  return _args->size();
}

void
OGExpr::accept(Visitor &v) const
{
  v.visit(this);
}

const OGExpr*
OGExpr::asOGExpr() const
{
  return this;
}

const RegContainer *
OGExpr::getRegs() const
{
  return _regs;
}

void OGExpr::debug_print() const
{
  cout << "OGExpr::debug_print()" << std::endl;
}


/**
 * Things that extend OGExpr
 */

OGUnaryExpr::OGUnaryExpr(const OGNumeric* arg): OGExpr{}
{
  if (arg == nullptr)
  {
    throw rdag_error("Null operand passed to unary expression");
  }
  _args = new ArgContainer();
  _args->push_back(arg);
}

OGBinaryExpr::OGBinaryExpr(const OGNumeric* arg0, const OGNumeric* arg1): OGExpr{}
{
  if (arg0 == nullptr)
  {
    throw rdag_error("Null operand passed to binary expression in arg0");
  }
  if (arg1 == nullptr)
  {
    throw rdag_error("Null operand passed to binary expression in arg1");
  }
  _args = new ArgContainer();
  _args->push_back(arg0);
  _args->push_back(arg1);
}

/**
 * Non autogenerated nodes
 */

/**
 * COPY node
 */

COPY::COPY(const OGNumeric* arg): OGUnaryExpr{arg} {}

OGNumeric*
COPY::copy() const
{
  return new COPY((*_args)[0]->copy());
}

const COPY*
COPY::asCOPY() const
{
  return this;
}

void
COPY::debug_print() const
{
        cout << "COPY base class" << endl;
}

ExprType_t
COPY::getType() const
{
  return COPY_ENUM;
}

/**
 * SELECTRESULT node
 */
SELECTRESULT::SELECTRESULT(const OGNumeric* arg0, const OGNumeric* arg1): OGExpr{}
{
  if (arg0 == nullptr)
  {
    throw rdag_error("Null operand passed to binary expression in arg0");
  }
  if (arg1 == nullptr)
  {
    throw rdag_error("Null operand passed to binary expression in arg1");
  }
  if (arg1->getType() != INTEGER_SCALAR_ENUM)
  {
    throw rdag_error("Second argument of SelectResult is not an integer");
  }
  _args = new ArgContainer();
  _args->push_back(arg0);
  _args->push_back(arg1);
}

OGNumeric*
SELECTRESULT::copy() const
{
  return new SELECTRESULT((*_args)[0]->copy(), (*_args)[1]->copy());
}

const SELECTRESULT*
SELECTRESULT::asSELECTRESULT() const
{
  return this;
}

void
SELECTRESULT::debug_print() const
{
        printf("SELECTRESULT base class\n");
}

ExprType_t
SELECTRESULT::getType() const
{
  return SELECTRESULT_ENUM;
}


/**
 * NORM2 node
 */

NORM2::NORM2(const OGNumeric* arg): OGUnaryExpr{arg} {}

OGNumeric*
NORM2::copy() const
{

  return new NORM2((*_args)[0]->copy());
}

const NORM2*
NORM2::asNORM2() const
{
  return this;
}

void
NORM2::debug_print() const
{
        cout << "NORM2 base class" << endl;
}

ExprType_t
NORM2::getType() const
{
  return NORM2_ENUM;
}

/**
 * SVD node
 */

SVD::SVD(const OGNumeric* arg): OGUnaryExpr{arg} {}

OGNumeric*
SVD::copy() const
{
  return new SVD((*_args)[0]->copy());
}

const SVD*
SVD::asSVD() const
{
  return this;
}

void
SVD::debug_print() const
{
  cout << "SVD node (functionality not yet implemented)" << endl;
}

ExprType_t
SVD::getType() const
{
  return SVD_ENUM;
}

/**
 * MTIMES node
 */

MTIMES::MTIMES(const OGNumeric* arg0, const OGNumeric* arg1): OGBinaryExpr{arg0, arg1} {}

OGNumeric*
MTIMES::copy() const
{
  return new MTIMES((*_args)[0]->copy(), (*_args)[1]->copy());
}

const MTIMES*
MTIMES::asMTIMES() const
{
  return this;
}

void
MTIMES::debug_print() const
{
        cout << "MTIMES base class" << endl;
}

ExprType_t
MTIMES::getType() const
{
  return MTIMES_ENUM;
}

} // namespace librdag
