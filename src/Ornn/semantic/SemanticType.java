package Ornn.semantic;

import Ornn.util.Position;

public interface SemanticType {
    String getTypeName();

    void compatible(SemanticType type, Position position);

    void equable(SemanticType type, Position position);
}
