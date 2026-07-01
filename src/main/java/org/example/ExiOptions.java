package org.example;

import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.word.PointerBase;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@CContext(ExiOptions.Directives.class)
@CStruct("ExiOptions")
public interface ExiOptions extends PointerBase {

    final class Directives implements CContext.Directives {
        @Override
        public List<String> getHeaderFiles() {
            try (InputStream in = Directives.class.getClassLoader().getResourceAsStream("exi_options.h")) {
                if (in == null) throw new RuntimeException("exi_options.h not found on classpath");
                Path tmp = Files.createTempFile("exi_options", ".h");
                tmp.toFile().deleteOnExit();
                Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
                return Collections.singletonList("\"" + tmp.toAbsolutePath() + "\"");
            } catch (IOException e) {
                throw new RuntimeException("Failed to extract exi_options.h", e);
            }
        }
    }

    @CField("schema_path")
    CCharPointer schema_path();
}
