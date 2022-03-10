package org.pitest.mutationtest.build.intercept.equivalent;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.pitest.bytecode.analysis.InstructionMatchers.aVariableAccess;
import static org.pitest.bytecode.analysis.InstructionMatchers.anyInstruction;
import static org.pitest.bytecode.analysis.InstructionMatchers.getStatic;
import static org.pitest.bytecode.analysis.InstructionMatchers.isA;
import static org.pitest.bytecode.analysis.InstructionMatchers.isInstruction;
import static org.pitest.bytecode.analysis.InstructionMatchers.methodCallNamed;
import static org.pitest.bytecode.analysis.InstructionMatchers.methodCallTo;
import static org.pitest.bytecode.analysis.InstructionMatchers.notAnInstruction;
import static org.pitest.bytecode.analysis.InstructionMatchers.opCode;
import static org.pitest.bytecode.analysis.InstructionMatchers.variableMatches;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.bytecode.analysis.MethodMatchers;
import org.pitest.bytecode.analysis.MethodTree;
import org.pitest.classinfo.ClassName;
import org.pitest.functional.FCollection;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.CompoundMutationInterceptor;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.build.MutationInterceptorFactory;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.gregor.mutators.returns.BooleanFalseReturnValsMutator;
import org.pitest.mutationtest.engine.gregor.mutators.returns.BooleanTrueReturnValsMutator;
import org.pitest.mutationtest.engine.gregor.mutators.returns.EmptyObjectReturnValsMutator;
import org.pitest.mutationtest.engine.gregor.mutators.returns.NullReturnValsMutator;
import org.pitest.mutationtest.engine.gregor.mutators.returns.PrimitiveReturnsMutator;
import org.pitest.plugin.Feature;
import org.pitest.sequence.Context;
import org.pitest.sequence.Match;
import org.pitest.sequence.QueryParams;
import org.pitest.sequence.QueryStart;
import org.pitest.sequence.SequenceMatcher;
import org.pitest.sequence.SequenceQuery;
import org.pitest.sequence.Slot;

/**
 * Tightly coupled to the PrimitiveReturnsMutator and EmptyObjectReturnValsMutator
 *   - removes trivially equivalent mutants generated by these.
 * operator
 *
 */
public class EquivalentReturnMutationFilter implements MutationInterceptorFactory {
    
  @Override
  public String description() {
    return "Trivial return vals equivalence filter";
  }

  @Override
  public Feature provides() {
    return Feature.named("FRETEQUIV")
        .withOnByDefault(true)
        .withDescription("Filters return vals mutants with bytecode equivalent to the unmutated class");

  }

  @Override
  public MutationInterceptor createInterceptor(InterceptorParameters params) {
    return new CompoundMutationInterceptor(Arrays.asList(new PrimitiveEquivalentFilter(),
        new NullReturnsFilter(),
        new EmptyReturnsFilter(),
        new HardCodedTrueEquivalentFilter())) {
      @Override
      public InterceptorType type() {
        return InterceptorType.FILTER;
      }
    };
  }

}

class HardCodedTrueEquivalentFilter implements MutationInterceptor {   
  private static final Slot<AbstractInsnNode> MUTATED_INSTRUCTION = Slot.create(AbstractInsnNode.class);

  static final SequenceQuery<AbstractInsnNode> BOXED_TRUE = QueryStart
      .match(opCode(Opcodes.ICONST_1))
      .then(methodCallNamed("valueOf"));

  static final SequenceQuery<AbstractInsnNode> CONSTANT_TRUE = QueryStart
          .match(getStatic("java/lang/Boolean","TRUE"));

  static final SequenceMatcher<AbstractInsnNode> EQUIVALENT_TRUE = QueryStart
          .any(AbstractInsnNode.class)
          .zeroOrMore(QueryStart.match(anyInstruction()))
          .then(BOXED_TRUE.or(CONSTANT_TRUE))
          .then(isInstruction(MUTATED_INSTRUCTION.read()))
          .zeroOrMore(QueryStart.match(anyInstruction()))
          .compile(QueryParams.params(AbstractInsnNode.class)
                  .withIgnores(notAnInstruction().or(isA(LabelNode.class)))
          );

  private static final Set<String> MUTATOR_IDS = new HashSet<>();

  static {
     MUTATOR_IDS.add(BooleanTrueReturnValsMutator.TRUE_RETURNS.getGloballyUniqueId());
  }

  private ClassTree currentClass;

  @Override
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  @Override
  public void begin(ClassTree clazz) {
    this.currentClass = clazz;
  }

  @Override
  public Collection<MutationDetails> intercept(
      Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isEquivalent(m)));
  }

  private Predicate<MutationDetails> isEquivalent(Mutater m) {
    return new Predicate<MutationDetails>() {
      @Override
      public boolean test(MutationDetails a) {
        if (!MUTATOR_IDS.contains(a.getMutator())) {
          return false;
        }
        final int instruction = a.getInstructionIndex();
        final MethodTree method = HardCodedTrueEquivalentFilter.this.currentClass.methods().stream()
            .filter(MethodMatchers.forLocation(a.getId().getLocation()))
            .findFirst().get();
        return primitiveTrue(instruction, method) || boxedTrue(instruction, method);
      }

      private boolean primitiveTrue(int instruction, MethodTree method) {
        return method.realInstructionBefore(instruction).getOpcode() == Opcodes.ICONST_1;
      }

      private boolean boxedTrue(int instruction, MethodTree method) {
          final Context<AbstractInsnNode> context = Context.start(method.instructions(), false);
          context.store(MUTATED_INSTRUCTION.write(), method.instruction(instruction));
          return EQUIVALENT_TRUE.matches(method.instructions(), context);
      }
    };
  }

  @Override
  public void end() {
    this.currentClass = null;
  }

}


class PrimitiveEquivalentFilter implements MutationInterceptor {

  private static final Set<String> MUTATOR_IDS = new HashSet<>();
  private static final Set<Integer> ZERO_CONSTANTS = new HashSet<>();
  static {
    ZERO_CONSTANTS.add(Opcodes.ICONST_0);
    ZERO_CONSTANTS.add(Opcodes.LCONST_0);
    ZERO_CONSTANTS.add(Opcodes.FCONST_0);
    ZERO_CONSTANTS.add(Opcodes.DCONST_0);

    MUTATOR_IDS.add(PrimitiveReturnsMutator.PRIMITIVE_RETURNS.getGloballyUniqueId());
    MUTATOR_IDS.add(BooleanFalseReturnValsMutator.FALSE_RETURNS.getGloballyUniqueId());
  }

  private ClassTree currentClass;

  @Override
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  @Override
  public void begin(ClassTree clazz) {
    this.currentClass = clazz;
  }

  @Override
  public Collection<MutationDetails> intercept(
      Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isEquivalent(m)));
  }

  private Predicate<MutationDetails> isEquivalent(Mutater m) {
    return a -> {
      if (!MUTATOR_IDS.contains(a.getMutator())) {
        return false;
      }
      final MethodTree method = PrimitiveEquivalentFilter.this.currentClass.methods().stream()
          .filter(MethodMatchers.forLocation(a.getId().getLocation()))
          .findFirst()
          .get();
      return ZERO_CONSTANTS.contains(method.realInstructionBefore(a.getInstructionIndex()).getOpcode());
    };
  }

  @Override
  public void end() {
    this.currentClass = null;
  }

}

/**
 * Handles methods already returning a 0 value, and also
 * those returning Boolean.FALSE
 */
class EmptyReturnsFilter implements MutationInterceptor {

  private static final Slot<AbstractInsnNode> MUTATED_INSTRUCTION = Slot.create(AbstractInsnNode.class);
  private static final Slot<Integer> LOCAL_VAR = Slot.create(Integer.class);

  static final SequenceQuery<AbstractInsnNode> CONSTANT_ZERO = QueryStart
          .match(isZeroConstant())
          .then(methodCallNamed("valueOf"));

  static final SequenceQuery<AbstractInsnNode> CONSTANT_FALSE = QueryStart
          .match(getStatic("java/lang/Boolean","FALSE"));

  static final SequenceMatcher<AbstractInsnNode> ZERO_VALUES = QueryStart
          .any(AbstractInsnNode.class)
          .zeroOrMore(QueryStart.match(anyInstruction()))
          .then(CONSTANT_ZERO.or(CONSTANT_FALSE).or(QueryStart.match(loadsEmptyReturnOntoStack())))
          .then(isInstruction(MUTATED_INSTRUCTION.read()))
          .zeroOrMore(QueryStart.match(anyInstruction()))
          .compile(QueryParams.params(AbstractInsnNode.class)
                  .withIgnores(notAnInstruction().or(isA(LabelNode.class)))
          );

  static final SequenceMatcher<AbstractInsnNode> INDIRECT_ZERO_VALUES = QueryStart
          .any(AbstractInsnNode.class)
          .zeroOrMore(QueryStart.match(anyInstruction()))
          .then(loadsEmptyReturnOntoStack())
          .then(aStoreTo(LOCAL_VAR))
          // match anything that doesn't overwrite the local var
          // possible we will get issues here if there is a jump instruction
          // to get to the point that the empty value is returned.
          .zeroOrMore(QueryStart.match(aStoreTo(LOCAL_VAR).negate()))
          .then(opCode(ALOAD).and(variableMatches(LOCAL_VAR.read())))
          .then(isInstruction(MUTATED_INSTRUCTION.read()))
          .zeroOrMore(QueryStart.match(anyInstruction()))
          .compile(QueryParams.params(AbstractInsnNode.class)
                  .withIgnores(notAnInstruction().or(isA(LabelNode.class)))
          );

  private static final Set<String> MUTATOR_IDS = new HashSet<>();
  private static final Set<Integer> ZERO_CONSTANTS = new HashSet<>();
  static {
    ZERO_CONSTANTS.add(Opcodes.ICONST_0);
    ZERO_CONSTANTS.add(Opcodes.LCONST_0);
    ZERO_CONSTANTS.add(Opcodes.FCONST_0);
    ZERO_CONSTANTS.add(Opcodes.DCONST_0);

    MUTATOR_IDS.add(EmptyObjectReturnValsMutator.EMPTY_RETURNS.getGloballyUniqueId());
    MUTATOR_IDS.add(BooleanFalseReturnValsMutator.FALSE_RETURNS.getGloballyUniqueId());
  }

  private ClassTree currentClass;

  @Override
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  @Override
  public void begin(ClassTree clazz) {
    this.currentClass = clazz;
  }

  @Override
  public Collection<MutationDetails> intercept(
      Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isEquivalent(m)));
  }

  private static Match<AbstractInsnNode> aStoreTo(Slot<Integer> variable) {
    return opCode(ASTORE).and(aVariableAccess(variable.write()));
  }

  private static Match<AbstractInsnNode> isZeroConstant() {
      return (context,node) -> ZERO_CONSTANTS.contains(node.getOpcode());
  }
  
  private Predicate<MutationDetails> isEquivalent(Mutater m) {
    return new Predicate<MutationDetails>() {
      @Override
      public boolean test(MutationDetails a) {
        if (!MUTATOR_IDS.contains(a.getMutator())) {
          return false;
        }

        final MethodTree method = EmptyReturnsFilter.this.currentClass.methods().stream()
            .filter(MethodMatchers.forLocation(a.getId().getLocation()))
            .findFirst()
            .get();
        final int mutatedInstruction = a.getInstructionIndex();
        return returnsZeroValue(ZERO_VALUES, method, mutatedInstruction)
            || returnsZeroValue(INDIRECT_ZERO_VALUES, method, mutatedInstruction)
            || returnsEmptyString(method, mutatedInstruction);
      }

      private Boolean returnsZeroValue(SequenceMatcher<AbstractInsnNode> sequence, MethodTree method,
                                       int mutatedInstruction) {
          final Context<AbstractInsnNode> context = Context.start(method.instructions(), false);
          context.store(MUTATED_INSTRUCTION.write(), method.instruction(mutatedInstruction));
          return sequence.matches(method.instructions(), context);
      }

      private boolean returnsEmptyString(MethodTree method,
          int mutatedInstruction) {
        final AbstractInsnNode node = method.realInstructionBefore(mutatedInstruction);
        if (node instanceof LdcInsnNode ) {
          final LdcInsnNode ldc = (LdcInsnNode) node;
          return "".equals(ldc.cst);
        }
        return false;
      }

    };
  }

  private static Match<AbstractInsnNode> loadsEmptyReturnOntoStack() {
    return noArgsCall("java/util/Optional", "empty")
            .or(noArgsCall("java/util/stream/Stream", "empty"))
            .or(noArgsCall("java/util/Collections", "emptyList"))
            .or(noArgsCall("java/util/Collections", "emptyMap"))
            .or(noArgsCall("java/util/Collections", "emptySet"))
            .or(noArgsCall("java/util/List", "of"))
            .or(noArgsCall("java/util/Set", "of"));
  }

  private static Match<AbstractInsnNode> noArgsCall(String owner, String name) {
    return methodCallTo(ClassName.fromString(owner), name).and(takesNoArgs());
  }

  private static Match<AbstractInsnNode> takesNoArgs() {
    return (c,node) -> {
      if (node instanceof MethodInsnNode ) {
        final MethodInsnNode call = (MethodInsnNode) node;
        return Type.getArgumentTypes(call.desc).length == 0;
      }
      return false;
    };
  }

  @Override
  public void end() {
    this.currentClass = null;
  }

}

class NullReturnsFilter implements MutationInterceptor {

  private static final String MUTATOR_ID = NullReturnValsMutator.NULL_RETURNS.getGloballyUniqueId();

  private ClassTree currentClass;

  @Override
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  @Override
  public void begin(ClassTree clazz) {
    this.currentClass = clazz;
  }

  @Override
  public Collection<MutationDetails> intercept(
      Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isEquivalent(m)));
  }

  private Predicate<MutationDetails> isEquivalent(Mutater m) {
    return new Predicate<MutationDetails>() {
      @Override
      public boolean test(MutationDetails a) {
        if (!MUTATOR_ID.equals(a.getMutator())) {
          return false;
        }

        final MethodTree method = NullReturnsFilter.this.currentClass.methods().stream()
            .filter(MethodMatchers.forLocation(a.getId().getLocation()))
            .findFirst()
            .get();
        final int mutatedInstruction = a.getInstructionIndex();
        return returnsNull(method, mutatedInstruction);
      }

      private Boolean returnsNull(MethodTree method,
          int mutatedInstruction) {
        return method.realInstructionBefore(mutatedInstruction).getOpcode() == Opcodes.ACONST_NULL;
      }
    };
  }

  @Override
  public void end() {
    this.currentClass = null;
  }

}
