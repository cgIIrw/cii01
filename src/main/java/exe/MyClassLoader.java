package exe;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader {
    String absDir;

    MyClassLoader() {
        super(MyClassLoader.class.getClassLoader());

    }

    MyClassLoader(String absDir) {
        this.absDir = absDir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = readClass(absDir, name);
        return defineClass(name, data, 0, data.length);
    }

    public Class loadByte(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }


    public byte[] readClass(String absDir, String className) {
        byte[] buf = new byte[1024];
        byte[] b = null;
        File f;
        if (absDir != null) {
            f = new File(absDir, className);
            if (!f.exists()) {
                return null;
            }
        } else {
            f = new File(className);
        }

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int bufread = 0;
            while ((bufread = in.read(buf)) != -1) {
                out.write(buf, 0, bufread);
            }
            b = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
