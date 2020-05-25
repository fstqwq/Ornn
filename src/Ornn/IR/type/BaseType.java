package Ornn.IR.type;


abstract public class BaseType {
    public int getDim() {
        return 0;
    }
    public abstract int size();
    public abstract String toString();
    public abstract boolean isSameWith(BaseType type);
}
