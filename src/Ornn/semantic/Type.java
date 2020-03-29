package Ornn.semantic;

import Ornn.util.Position;

public interface Type  {
    String getTypeName();

    void compatible(Type type, Position position);

    void equable(Type type, Position position);
}
