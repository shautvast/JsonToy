package nl.sanderhautvast.json.ser;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

public class MapperFactory extends ClassVisitor {

    public static final String MAPPERBASECLASS = javaName(BaseMapper.class.getName());

    public static final String INIT = "<init>";
    public static final String ZERO_ARGS_VOID = "()V";
    public static final String APPEND = "append";
    public static final String STRINGBUILDER = "java/lang/StringBuilder";
    public static final String MAPPER = "nl/sanderhautvast/json/ser/Mapper";
    public static final String APPEND_SIGNATURE = "(Ljava/lang/String;)Ljava/lang/StringBuilder;";
    public static final String OBJECT = "Ljava/lang/Object;";

    public MapperFactory() {
        super(ASM7);
        init();
    }

    final ClassNode classNode = new ClassNode();

    private String classToMap;

    private MethodNode jsonMethod;

    private boolean firstGetter = true;

    private final LinkedList<AbstractInsnNode> getterInsnList = new LinkedList<>();

    private void init() {
        classNode.version = V1_5;
        classNode.access = ACC_PUBLIC;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.classToMap = name;
        classNode.name = "Mapper" + UUID.randomUUID();
        classNode.superName = MAPPERBASECLASS;
        MethodNode constructor = new MethodNode(ACC_PUBLIC, INIT, ZERO_ARGS_VOID, null, null);
        constructor.instructions.add(new VarInsnNode(ALOAD, 0));
        constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, MAPPERBASECLASS, INIT, ZERO_ARGS_VOID));
        constructor.instructions.add(new InsnNode(RETURN));
        classNode.methods.add(constructor);

        jsonMethod = new MethodNode(ACC_PROTECTED,
                "json", "(Ljava/lang/StringBuilder;Ljava/lang/Object;)V", null, null);
        classNode.methods.add(jsonMethod);
        add(new VarInsnNode(ALOAD, 2));
        add(new TypeInsnNode(CHECKCAST, name));
        add(new VarInsnNode(ASTORE, 3));
        add(new VarInsnNode(ALOAD, 1));
        add(new LdcInsnNode("{"));
        add(new MethodInsnNode(INVOKEVIRTUAL, STRINGBUILDER, APPEND, APPEND_SIGNATURE));
        add(new InsnNode(POP));
    }

    public MethodVisitor visitMethod(int access, String methodname,
                                     String desc, String signature, String[] exceptions) {
        if (!hasArgs(desc) && access == Modifier.PUBLIC &&
                (methodname.startsWith("get") || (methodname.startsWith("is")) && desc.equals("()Z"))) {
            visitGetter(methodname, getReturnType(desc));
        }
        return null;
    }

    private void visitGetter(String getterMethodName, String returnType) {
        int startIndex;
        if (getterMethodName.startsWith("is")) {
            startIndex = 2;
        } else {
            startIndex = 3;
        }

        if (!firstGetter) {
            getterInsnList.add(new VarInsnNode(ALOAD, 1));
            getterInsnList.add(new LdcInsnNode(","));
            getterInsnList.add(new MethodInsnNode(INVOKEVIRTUAL, STRINGBUILDER, APPEND, APPEND_SIGNATURE));
            getterInsnList.add(new InsnNode(POP));
        } else {
            firstGetter = false;
        }

        getterInsnList.add(new VarInsnNode(ALOAD, 1));
        getterInsnList.add(new LdcInsnNode("\"" + correctName(getterMethodName, startIndex) + "\":"));
        getterInsnList.add(new MethodInsnNode(INVOKEVIRTUAL, STRINGBUILDER, APPEND, APPEND_SIGNATURE));
        getterInsnList.add(new InsnNode(POP));
        getterInsnList.add(new VarInsnNode(ALOAD, 1));
        getterInsnList.add(new VarInsnNode(ALOAD, 3));
        getterInsnList.add(new MethodInsnNode(INVOKEVIRTUAL, classToMap, getterMethodName, "()" + returnType));
        getterInsnList.add(new MethodInsnNode(INVOKESTATIC, MAPPER, "json", "(Ljava/lang/StringBuilder;" + genericReturnType(returnType) + ")V"));
    }

    private String correctName(String getterMethodName, int startIndex) {
        String tmp = getterMethodName.substring(startIndex);
        return tmp.substring(0, 1).toLowerCase() + tmp.substring(1);
    }

    private static String genericReturnType(String returnType) {
        char firstChar = returnType.charAt(0);
        if (firstChar == 'L' || firstChar == '[') {
            return OBJECT;
        } else {
            return returnType;
        }
    }

    @Override
    public void visitEnd() {
        for (AbstractInsnNode insn : getterInsnList) {
            add(insn);
        }
        add(new VarInsnNode(ALOAD, 1));
        add(new LdcInsnNode("}"));
        add(new MethodInsnNode(INVOKEVIRTUAL, STRINGBUILDER, APPEND, APPEND_SIGNATURE));
        add(new InsnNode(RETURN));
    }

    private void add(AbstractInsnNode ins) {
        jsonMethod.instructions.add(ins);
    }

    private String getReturnType(String desc) {
        return desc.substring(2);
    }

    private boolean hasArgs(String desc) {
        return desc.charAt(1) != ')';
    }

    private static String javaName(String className) {
        return className.replaceAll("\\.", "/");
    }

}
