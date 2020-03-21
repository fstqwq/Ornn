package Ornn.semantic;

import Ornn.util.Position;

public interface Type  {
    String getTypeName();

    void compatible(Type type, Position position);

    boolean isPrimitiveType();

    boolean isClassType();

    boolean isArrayType();

    boolean isNullType();

    boolean isPointerType();

    /* TODO:
    int getTypeSize();
 */
}
