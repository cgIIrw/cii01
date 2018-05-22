package exe;

public class ClassModifier {
    /**
     * 跳过咖啡北鼻和版本号开始索引的位置
     */
//    private static final int CONSTANT_POOL_COUNT_INDEX = 8;

    /**
     * CONSTANT_Utf8_info对应tag值
     */
    private static final int CONSTANT_Utf8_info = 1;

    /**
     * 将不是定长的CONSTANT_Utf8_info的tag和没有对应常量的tag设为－1
     */
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};

    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    public byte[] modifyUTF8Constant(String oldStr, String newStr) {
        ClassReader cr = new ClassReader(classByte);
        cr.readUint64();
        int cpc = cr.readUint16();

        for (int i = 1; i < cpc; i++) {
            int tag = cr.readUint8();
            if (tag == CONSTANT_Utf8_info) {
                int len = cr.readUint16();
                byte[] bytes = cr.readBytes(len);
                String str = new String(bytes, 0, len);
                if (str.equalsIgnoreCase(oldStr)) {
                    byte[] strBytes = newStr.getBytes();
                    byte[] strlen = new byte[2];
                    strlen[1] = (byte) (newStr.length() & 0xff);
                    strlen[0] = (byte) ((newStr.length() >> 8) & 0xff);
                    classByte = bytesReplace(classByte, cr.cursor - 2 - len, 2, strlen);
                    classByte = bytesReplace(classByte, cr.cursor - len, len, strBytes);
                    return classByte;
                }
            } else {
                cr.cursor += CONSTANT_ITEM_LENGTH[tag] - 1;
            }
        }
        return classByte;
    }
    // 字节替换术
    public byte[] bytesReplace(byte[] bytes01, int offset, int len, byte[]bytes02) {
        byte[] newBytes = new byte[bytes01.length + bytes02.length - len];
        System.arraycopy(bytes01, 0, newBytes, 0, offset);
        System.arraycopy(bytes02, 0, newBytes, offset, bytes02.length);
        System.arraycopy(bytes01, offset + len, newBytes, offset
                + bytes02.length, bytes01.length - offset - len);
        return newBytes;
    }
}
