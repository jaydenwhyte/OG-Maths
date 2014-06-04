#
# Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
#
# Please see distribution for license.
#

dispatch_header = """\
/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 *
 * This file is autogenerated during the DOGMA2 build process - src/generator/generator.py
 */

#ifndef _DISPATCH_HH
#define _DISPATCH_HH

#include "numerictypes.hh"
#include "expression.hh"
#include "terminal.hh"
#include "warningmacros.h"
#include "jvmmanager.hh"
#include "exceptions.hh"
#include "debug.h"
#include "convertto.hh"

namespace librdag {

%(dispatcher_definition)s

%(dispatchop_definition)s

%(dispatchunary_definition)s

%(dispatchbinary_definition)s

// typedef the void dispatches
typedef DispatchOp<void*> DispatchVoidOp;
typedef DispatchUnaryOp<void*> DispatchVoidUnaryOp;
typedef DispatchBinaryOp<void*> DispatchVoidBinaryOp;

/**
 * Template instantiations
 */

extern template class DispatchOp<void*>;
extern template class DispatchUnaryOp<void*>;
extern template class DispatchBinaryOp<void*>;

} // end namespace librdag

# endif // _DISPATCH_HH
"""

dispatcher_class = """\
// Forward declarations
%(dispatcher_forward_decls)s

/**
 * The class for dispatching execution based on OGNumeric type
 */
class Dispatcher
{
  public:
    Dispatcher();
    virtual ~Dispatcher();
    void dispatch(OGNumeric::Ptr thing) const;

    // Specific terminal dispatches
%(dispatcher_terminal_dispatches)s

    // Specific node dispatches
%(dispatcher_node_dispatches)s

  private:
%(dispatcher_private_members)s
};
"""

dispatcher_forward_decl = """\
class %(nodetype)sRunner;
"""

dispatcher_dispatch_prototype = """\
    virtual void dispatch(%(nodetype)s::Ptr thing) const;
"""

dispatcher_private_member = """\
    %(nodetype)sRunner* _%(nodetype)sRunner;
"""

dispatchop_class = """\
template <typename T>
class DispatchOp
{
  static_assert(is_pointer<T>::value, "Type T must be a pointer");
  public:
    DispatchOp();
    virtual ~DispatchOp();
    const ConvertTo * getConvertTo() const;
  private:
    const ConvertTo * _convert;
};
"""

dispatchunaryop_class = """\
/**
 * For dispatching operations of the form "T foo(Register * register1, OGTerminal * arg)"
 */
template<typename T> class DispatchUnaryOp: public DispatchOp<T>
{
  public:
    using DispatchOp<T>::getConvertTo;
    virtual ~DispatchUnaryOp();

    // will run the operation
    T eval(RegContainer& reg, OGTerminal::Ptr arg) const;
    // Methods for specific terminals
%(dispatchunaryop_terminal_methods)s
    // Backstop methods for generic implementation
    virtual T run(RegContainer& reg, OGRealMatrix::Ptr arg) const = 0;
    virtual T run(RegContainer& reg, OGComplexMatrix::Ptr arg) const = 0;
};
"""

dispatchunaryop_run = """\
    virtual T run(RegContainer& reg, %(nodetype)s::Ptr arg) const;
"""

dispatchbinaryop_class = """\
/**
 * For dispatching operations of the form "T foo(Register * register1, OGTerminal * arg0, OGTerminal * arg1)"
 */
template<typename T> class  DispatchBinaryOp: public DispatchOp<T>
{
  public:
    using DispatchOp<T>::getConvertTo;
    virtual ~DispatchBinaryOp();
    // will run the operation
    T eval(RegContainer& reg0, OGTerminal::Ptr arg0, OGTerminal::Ptr arg1) const;

    // Methods for specific terminals
%(dispatchbinaryop_terminal_methods)s
    // Required backstop impls
    virtual T run(RegContainer& reg0, OGComplexMatrix::Ptr arg0, OGComplexMatrix::Ptr arg1) const = 0;
    virtual T run(RegContainer& reg0, OGRealMatrix::Ptr arg0, OGRealMatrix::Ptr arg1) const = 0;
};
"""

dispatchbinaryop_run = """\
    virtual T run(RegContainer& reg0, %(node0type)s::Ptr arg0, %(node1type)s::Ptr arg1) const;
"""

# Dispatch cc file

dispatch_cc = """\
/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for licence.
 *
 * This file is autogenerated during the DOGMA2 build process - src/generator/generator.py
 */

#include "dispatch.hh"
#include "runners.hh"
#include "expression.hh"
#include "terminal.hh"
#include "warningmacros.h"
#include "uncopyable.hh"
#include <iostream>
#include <sstream>

namespace librdag {

/**
 *  Dispatcher
 */

%(dispatcher_methods)s

/**
 * DispatchOp
 */

%(dispatchop_methods)s

/**
 * DispatchUnaryOp
 */

%(dispatchunaryop_methods)s

/**
 * DispatchBinaryOp
 */

%(dispatchbinaryop_methods)s

/**
 * Template instantiations
 */

template class DispatchOp<void*>;
template class DispatchUnaryOp<void*>;
template class DispatchBinaryOp<void*>;

} // namespace librdag
"""
# Dispatcher methods

dispatcher_methods = """\
%(dispatcher_constructor)s

%(dispatcher_destructor)s

%(dispatcher_dispatch)s

// Specific terminal dispatches

%(dispatcher_terminal_dispatches)s

// Specific node dispatches

%(dispatcher_node_dispatches)s
"""

dispatcher_constructor = """\
Dispatcher::Dispatcher()
{
%(member_initialisers)s\
}
"""

dispatcher_member_initialiser = """\
  _%(nodetype)sRunner = new %(nodetype)sRunner();
"""

dispatcher_member_deleter = """\
  delete _%(nodetype)sRunner;
"""

dispatcher_destructor = """\
Dispatcher::~Dispatcher(){
%(member_deleters)s\
}
"""

dispatcher_dispatch_numeric = """\
void
Dispatcher::dispatch(OGNumeric::Ptr thing) const
{
  DEBUG_PRINT("Dispatching...\\n");
  ExprType_t ID = thing->getType();
  DEBUG_PRINT("TYPE IS %%d\\n", ID);
  bool isTerminalType = false;
  if (ID & IS_NODE_MASK)
  {
    DEBUG_PRINT("is node...\\n");
  }
  else
  {
    isTerminalType = true;
    DEBUG_PRINT("is terminal...\\n");
  }

  // branch switch on isTerminalType?
  if(isTerminalType) {
    switch(ID)
    {
%(dispatch_terminal_cases)s
      default:
        throw rdag_error("Unknown terminal type in dispatch");
    }
  }
  else
  {
    switch(ID)
    {
%(dispatch_expr_cases)s
      default:
        throw rdag_error("Unknown expression type in dispatch");
    }
  }
}
"""

dispatcher_case = """\
      case %(nodeenumtype)s:
        dispatch(thing->as%(nodetype)s());
        break;
"""

# The SUPPRESS_UNUSED is needed because not all nodes are implemented yet.
dispatcher_dispatch = """\
void
Dispatcher::dispatch(%(nodetype)s::Ptr SUPPRESS_UNUSED thing) const
{
%(dispatch_implementation)s
}
"""

dispatcher_binary_implementation = """\
  const ArgContainer& args = thing->getArgs();
  RegContainer& regs = thing->getRegs();
  OGNumeric::Ptr arg0 = args[0];
  OGNumeric::Ptr arg1 = args[1];
  OGTerminal::Ptr arg0t = arg0->asOGTerminal();
  OGTerminal::Ptr arg1t = arg1->asOGTerminal();
  if (arg0t == OGTerminal::Ptr{})
  {
    arg0t = arg0->asOGExpr()->getRegs()[0]->asOGTerminal();
  }
  if (arg1t == OGTerminal::Ptr{})
  {
    arg1t = arg1->asOGExpr()->getRegs()[0]->asOGTerminal();
  }
  this->_%(nodetype)sRunner->eval(regs, arg0t, arg1t);
"""

dispatcher_unary_implementation = """\
  const ArgContainer& args = thing->getArgs();
  RegContainer& regs = thing->getRegs();
  OGNumeric::Ptr arg = args[0];
  OGTerminal::Ptr argt = arg->asOGTerminal();
  if (argt == OGTerminal::Ptr{})
  {
    argt = arg->asOGExpr()->getRegs()[0]->asOGTerminal();
  }
  _%(nodetype)sRunner->eval(regs, argt);
"""

dispatcher_select_implementation = """\
  const ArgContainer& args = thing->getArgs();
  RegContainer& regs = thing->getRegs();
  OGNumeric::Ptr arg0 = args[0];
  OGNumeric::Ptr arg1 = args[1];
  const RegContainer& arg0r = arg0->asOGExpr()->getRegs();
  OGIntegerScalar::Ptr arg1i = arg1->asOGIntegerScalar();
  this->_%(nodetype)sRunner->eval(regs, arg0r, arg1i);
"""

# DispatchOp methods

dispatchop_methods = """\
template <typename T>
DispatchOp<T>::DispatchOp()
{
  _convert = new ConvertTo();
}

template <typename T>
const ConvertTo *
DispatchOp<T>::getConvertTo() const
{
  return _convert;
}

template <typename T>
DispatchOp<T>::~DispatchOp()
{
  delete _convert;
}
"""

# DispatchUnaryOp methods

dispatchunaryop_methods = """\
%(dispatchunaryop_destructor)s

%(dispatchunaryop_eval)s

%(dispatchunaryop_terminals)s
"""

dispatchunaryop_destructor = """\
template <typename T>
DispatchUnaryOp<T>::~DispatchUnaryOp() {}
"""

dispatchunaryop_eval = """\
template<typename T>
T
DispatchUnaryOp<T>::eval(RegContainer& reg, OGTerminal::Ptr arg) const
{
  ExprType_t argID = arg->getType();
  T ret = nullptr;
  switch(argID)
  {
%(eval_cases)s
    default:
        throw rdag_error("Unknown type in dispatch on arg");
  }
  return ret;
}
"""

dispatchunaryop_eval_case = """\
    case %(nodeenumtype)s:
      DEBUG_PRINT("running with run(reg, arg->as%(nodetype)s);\\n");
      ret = run(reg, arg->as%(nodetype)s());
      break;
"""

dispatchunaryop_terminal_method = """\
template<typename T>
T
DispatchUnaryOp<T>::run(RegContainer& reg, %(nodetype)s::Ptr arg) const
{
  %(typetoconvertto)s::Ptr conv = this->getConvertTo()->convertTo%(typetoconvertto)s(arg);
  T ret = run(reg, conv);
  return ret;
}
"""

# DispatchBinaryOp methods

dispatchbinaryop_methods = """\
%(dispatchbinaryop_destructor)s

%(dispatchbinaryop_eval)s

%(dispatchbinaryop_terminals)s
"""

dispatchbinaryop_destructor = """\
template <typename T>
DispatchBinaryOp<T>::~DispatchBinaryOp(){}
"""

dispatchbinaryop_eval = """\
template <typename T>
T
DispatchBinaryOp<T>::eval(RegContainer& reg0, OGTerminal::Ptr arg0, OGTerminal::Ptr arg1) const
{
  ExprType_t arg0ID = arg0->getType();
  ExprType_t arg1ID = arg1->getType();
  T ret = nullptr;
  // MASSIVE SWITCH TABLE
  switch(arg0ID)
  {
%(eval_cases)s
    default:
        stringstream message;
        message << "Unknown type in dispatch on arg0. Type is: " << arg0->getType() << ".";
        throw rdag_error(message.str());
  }
  return ret;
}
"""

dispatchbinaryop_eval_case_arg0 = """\
    case %(node0enumtype)s:
      switch(arg1ID)
      {
%(eval_arg1_cases)s
        default:
        stringstream message;
        message << "Unknown type in dispatch on arg1. Type is: " << arg1->getType() << ".";
        throw rdag_error(message.str());
      }
      break;
"""

dispatchbinaryop_eval_case_arg1 = """\
        case %(node1enumtype)s:
          DEBUG_PRINT("running with run(reg0, arg0->as%(node0type)s(), arg1->as%(node1type)s());\\n");
          ret = run(reg0, arg0->as%(node0type)s(), arg1->as%(node1type)s());
          break;
"""

dispatchbinaryop_terminal_method = """\
template<typename T>
T
DispatchBinaryOp<T>::run(RegContainer& reg0,
                         %(node0type)s::Ptr arg0,
                         %(node1type)s::Ptr arg1) const
{
%(conv0)s
%(conv1)s
  T ret = run(reg0, conv0, conv1);
  return ret;
}
"""

dispatchbinaryop_conv_arg = """\
  %(typetoconvertto)s::Ptr conv%(argno)s = this->getConvertTo()->convertTo%(typetoconvertto)s(arg%(argno)s);
"""

dispatchbinaryop_noconv_arg = """\
  %(nodetype)s::Ptr conv%(argno)s = arg%(argno)s;
"""
