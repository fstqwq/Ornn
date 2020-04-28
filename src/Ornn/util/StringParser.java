package Ornn.util;

public class StringParser {
    public static String parse(String in, Position position) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            if (in.charAt(i) == '\\') {
                assert i + 1 < in.length();
                switch (in.charAt(i + 1)) {
                    case '\\':
                        ret.append('\\');
                        break;
                    case 't':
                        ret.append('\t');
                        break;
                    case 'n':
                        ret.append('\n');
                        break;
                    case '"':
                        ret.append('"');
                        break;
                    default:
                        throw new CompilationError("unsupported \\" + in.charAt(i + 1), position);
                }
                i++;
            }
            else ret.append(in.charAt(i));
        }
        ret.append('\0');
        return ret.toString();
    }
    public static String llvmTransform(String in) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            switch (in.charAt(i)) {
                case '\n':
                    ret.append("\\0A");
                    break;
                case '\t':
                    ret.append("\\09");
                    break;
                case '\\':
                    ret.append("\\5C");
                    break;
                case '"':
                    ret.append("\\22");
                    break;
                case '\0':
                    ret.append("\\00");
                    break;
                default:
                    ret.append(in.charAt(i));
                    break;
            }
        }
        return ret.toString();
    }
    public static String asmTransform(String in) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            switch (in.charAt(i)) {
                case '\n':
                    ret.append("\\n");
                    break;
                case '\t':
                    ret.append("\\t");
                    break;
                case '\\':
                    ret.append("\\\\");
                    break;
                case '"':
                    ret.append("\\\"");
                    break;
                case '\0':
                    break;
                default:
                    ret.append(in.charAt(i));
                    break;
            }
        }
        return ret.toString();
    }
}
