package gabrielmendessc.com.atlas.engine.renderer;

import lombok.SneakyThrows;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;
import java.util.Objects;

public class Texture {

    private String filePath;
    private int texID;

    @SneakyThrows
    public Texture(String filePath) {

        this.filePath = Paths.get(this.getClass().getResource(filePath).toURI()).toString();

        // Generated a TEXTURE_2D ID
        texID = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, texID);

        // Set texture parameters (repeating image em both directions)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT);
        // When stretching, pixelate
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        // When shrinking, pixelate
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);

        IntBuffer widthBuff = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuff = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuff = BufferUtils.createIntBuffer(1);
        ByteBuffer imageBuff = STBImage.stbi_load(this.filePath, widthBuff, heightBuff, channelsBuff, 0);

        if (Objects.isNull(imageBuff)) {

            throw new RuntimeException("Error: (Texture) Could not load image '" + this.filePath + "'");

        }

        if (channelsBuff.get(0) == 3) {

            GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGB, widthBuff.get(0), heightBuff.get(0), 0, GL46.GL_RGB, GL46.GL_UNSIGNED_BYTE, imageBuff);

            STBImage.stbi_image_free(imageBuff);

            return;

        }

        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, widthBuff.get(0), heightBuff.get(0), 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, imageBuff);

        STBImage.stbi_image_free(imageBuff);

    }

    public void bind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }

}
