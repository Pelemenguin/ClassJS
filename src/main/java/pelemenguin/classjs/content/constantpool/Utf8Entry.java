package pelemenguin.classjs.content.constantpool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Utf8Entry extends ConstantEntry {

    public static final byte TAG = 1;

    private String content;

    public Utf8Entry(String content) {
        this.content = content;
    }

    @Override
    public byte[] generateByteCode() {
        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos)
        ) {

            dos.write(TAG);
            dos.writeUTF(this.content);
            dos.flush();

            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Unable to generate byte code for " + this.toString() + " due to an IOException", e);
        }
    }

    @Override
    public String toString() {
        return "Utf8(" + this.content + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Utf8Entry utf8) {
            return this.content.equals(utf8.content);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

}
