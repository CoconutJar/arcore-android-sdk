package com.google.ar.core.examples.java.common.colladaParser;

import java.nio.FloatBuffer;
import java.util.List;

import com.google.ar.core.examples.java.common.colladaParser.dataTypes.SkeletonData;
import com.google.ar.core.examples.java.common.colladaParser.dataTypes.SkinningData;
import com.google.ar.core.examples.java.common.colladaParser.dataTypes.JointData;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SkeletonLoader {

    private NodeList armatureData;

    private List<String> boneOrder;

    private int jointCount = 0;

    private static final float[] CORRECTION = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));

    public SkeletonLoader(NodeList visualSceneNode, List<String> boneOrder) {
        this.armatureData = visualSceneNode.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
        this.boneOrder = boneOrder;
    }

    public SkeletonData extractBoneData(){
        Node headNode = armatureData.getChild("node");
        JointData headJoint = loadJointData(headNode, true);
        return new SkeletonData(jointCount, headJoint);
    }

    private JointData loadJointData(Node jointNode, boolean isRoot){
        JointData joint = extractMainJointData(jointNode, isRoot);
        for(Node childNode : jointNode.getChildren("node")){
            joint.addChild(loadJointData(childNode, false));
        }
        return joint;
    }

    private JointData extractMainJointData(Node jointNode, boolean isRoot){
        String nameId = jointNode.getAttribute("id");
        int index = boneOrder.indexOf(nameId);
        String[] matrixData = jointNode.getChild("matrix").getData().split(" ");
        //Matrix4f matrix = new Matrix4f();
        float[] matrix = convertData(matrixData);
        matrix.transpose();
        if(isRoot){
            //because in Blender z is up, but in our game y is up.
            Matrix4f.mul(CORRECTION, matrix, matrix);
        }
        jointCount++;
        return new JointData(index, nameId, matrix);
    }

    private float[] convertData(String[] rawData){
        float[] matrixData = new float[16];
        for(int i=0;i<matrixData.length;i++){
            matrixData[i] = Float.parseFloat(rawData[i]);
        }

        return matrixData;
    }

}
