package com.google.ar.core.examples.java.common.colladaParser;

import com.google.ar.core.examples.java.common.samplerender.Joint;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.ar.core.examples.java.common.colladaParser.dataTypes.MeshData;
import com.google.ar.core.examples.java.common.colladaParser.dataTypes.SkeletonData;
import com.google.ar.core.examples.java.common.colladaParser.dataTypes.SkinningData;

public class ColladaParser {

    private IntBuffer fvi = null;
    private FloatBuffer vertices = null;
    private FloatBuffer texCoords = null;
    private FloatBuffer normals = null;
    private IntBuffer jointIDs = null;
    private FloatBuffer vw = null;
    private Joint rootJoint = null;

    private int maxWeights = 3;

    public ColladaParser(InputStream inputStream){
        try {
            // Assign the Class Variables the values from the Collada DAE file.
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);


            NodeList nodeList = doc.getElementsByTagName("library_controllers");
            SkinLoader skinLoader = new SkinLoader(nodeList, maxWeights);
            SkinningData skinningData = skinLoader.extractSkinData();

            nodeList = doc.getElementsByTagName("library_visual_scenes");
            SkeletonLoader jointsLoader = new SkeletonLoader(nodeList, skinningData.jointOrder);
            SkeletonData jointsData = jointsLoader.extractBoneData();

            nodeList = doc.getElementsByTagName("library_geometries");
            GeometryLoader g = new GeometryLoader(nodeList, skinningData.verticesSkinData);
            MeshData meshData = g.extractModelData();

        } catch (Exception e) {e.printStackTrace();}

    }

    public IntBuffer getFaceVertexIndices(int vertPerFace){
        return fvi;
    }
    public FloatBuffer getVertices(){
        return vertices;
    }
    public FloatBuffer getTexCoords(int d){
        return texCoords;
    }
    public FloatBuffer getNormals(){
        return normals;
    }
    public IntBuffer getJointIDs(){
        return jointIDs;
    }
    public FloatBuffer getVertexWeights(){
        return vw;
    }

    public Joint getRootJoint() {
        return rootJoint;
    }
    public int getJointCount() {
        return 0;
    }
}
