package gabrielmendessc.com.atlas.engine.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {

    private int shaderProgramID;
    private boolean beingUsed;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filePath) {

        this.filePath = filePath;

        try {

            String source = new String(Files.readAllBytes(Paths.get(this.getClass().getResource(filePath).toURI())));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, endOfLine).trim();

            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, endOfLine).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }

            System.out.println(vertexSource);
            System.out.println(fragmentSource);

        } catch (IOException | URISyntaxException e) {

            throw new RuntimeException("Could not open file for shader: '" + filePath + "'", e);

        }

    }

    public void compile() {

        int vertexID, fragmentID;

        //Load and compile the vertex shader
        vertexID = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);
        GL46.glShaderSource(vertexID, vertexSource);
        GL46.glCompileShader(vertexID);

        if (GL46.glGetShaderi(vertexID, GL46.GL_COMPILE_STATUS) == GL46.GL_FALSE) {
            System.err.println("ERROR: '" + filePath + "' - Vertex Shader compilation failed.");
            throw new RuntimeException(GL46.glGetShaderInfoLog(vertexID, GL46.glGetShaderi(vertexID, GL46.GL_INFO_LOG_LENGTH)));
        }

        //Load and compile the fragment shader
        fragmentID = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);
        GL46.glShaderSource(fragmentID, fragmentSource);
        GL46.glCompileShader(fragmentID);

        if (GL46.glGetShaderi(fragmentID, GL46.GL_COMPILE_STATUS) == GL46.GL_FALSE) {
            System.err.println("ERROR: '" + filePath + "' - Fragment Shader compilation failed.");
            throw new RuntimeException(GL46.glGetShaderInfoLog(fragmentID, GL46.glGetShaderi(fragmentID, GL46.GL_INFO_LOG_LENGTH)));
        }

        //Link Shaders
        shaderProgramID = GL46.glCreateProgram();
        GL46.glAttachShader(shaderProgramID, vertexID);
        GL46.glAttachShader(shaderProgramID, fragmentID);
        GL46.glLinkProgram(shaderProgramID);

        if (GL46.glGetProgrami(shaderProgramID, GL46.GL_LINK_STATUS) == GL46.GL_FALSE) {
            System.err.println("ERROR: '" + filePath + "' - Linking Shaders failed.");
            throw new RuntimeException(GL46.glGetProgramInfoLog(shaderProgramID, GL46.glGetProgrami(shaderProgramID, GL46.GL_INFO_LOG_LENGTH)));
        }

    }

    public void use() {

        if (beingUsed) {

            return;

        }

        //Bind shader program
        GL46.glUseProgram(shaderProgramID);

        beingUsed = true;

    }

    public void detach() {

        GL46.glUseProgram(0);

        beingUsed = false;

    }

    public void uploadMat4f(String varName, Matrix4f val) {

        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);

        use();

        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        val.get(matrixBuffer);
        GL46.glUniformMatrix4fv(varLocation, false, matrixBuffer);

    }

    public void uploadMat3f(String varName, Matrix3f val) {

        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);

        use();

        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(9);
        val.get(matrixBuffer);
        GL46.glUniformMatrix3fv(varLocation, false, matrixBuffer);

    }

    public void uploadVec4f(String varName, Vector4f val) {

        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);

        use();

        GL46.glUniform4f(varLocation, val.x, val.y, val.z, val.w);

    }

    public void uploadVec4f(String varName, Vector3f val) {

        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);

        use();

        GL46.glUniform3f(varLocation, val.x, val.y, val.z);

    }

    public void uploadFloat(String varName, float val) {

        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);

        use();

        GL46.glUniform1f(varLocation, val);

    }

    public void uploadInt(String varName, int val) {

        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);

        use();

        GL46.glUniform1i(varLocation, val);

    }

    public void uploadTexture(String varName, int slot) {

        int varLocation = GL46.glGetUniformLocation(shaderProgramID, varName);

        use();

        GL46.glUniform1i(varLocation, slot);

    }

}
